import { build, transform } from 'esbuild'
import { readFile, writeFile, mkdir } from 'node:fs/promises'
import { dirname, resolve } from 'node:path'
import { patchSearch } from './src/search-compat.mjs'

const destination = resolve('../app/src/main/assets/reader')
await mkdir(destination, { recursive: true })

// Keep upstream source intact; all reviewed Android-specific patches live here.
const androidCompatibility = {
    name: 'android-offline-compatibility',
    setup(builder) {
        builder.onLoad({ filter: /vendor[\\/]foliate[\\/].*\.js$/ }, async ({ path }) => {
            let contents = await readFile(path, 'utf8')
            if (path.endsWith('search.js')) contents = patchSearch(contents)
            if (path.endsWith('view.js')) {
                const start = contents.indexOf('const isZip = async file => {')
                const end = contents.indexOf('class CursorAutohider', start)
                if (start < 0 || end < 0) throw new Error('Reviewed Foliate dispatch patch no longer matches')
                contents = contents.slice(0, start) + "export const makeBook = async () => { throw new Error('Use the native offline publication loader') }\n\n" + contents.slice(end)
                contents = contents.replace('new WeakRef(el)', '({ deref: () => el })')
            }
            if (path.endsWith('paginator.js') || path.endsWith('fixed-layout.js')) {
                if (!contents.includes("'allow-same-origin allow-scripts'")) throw new Error('Reader iframe sandbox patch no longer matches')
                contents = contents.replaceAll("'allow-same-origin allow-scripts'", "'allow-same-origin'")
            }
            if (path.endsWith('paginator.js')) {
                // JS transpilation does not touch CSS inside template literals. Chrome58
                // also needs nesting flattened and container queries replaced by media queries.
                const start = contents.indexOf('<style>') + '<style>'.length
                const end = contents.indexOf('</style>', start)
                if (start < 7 || end < start) throw new Error('Reviewed paginator CSS patch no longer matches')
                const css = contents.slice(start, end).replaceAll('@container (orientation:', '@media (orientation:')
                    .replaceAll(':is(#header, #footer) > * > *', '#header > * > *, #footer > * > *')
                    .replaceAll(':is(#header, #footer) > *', '#header > *, #footer > *')
                const compatible = await transform(css, { loader: 'css', target: ['chrome58'], supported: { nesting: false } })
                contents = contents.slice(0, start) + compatible.code + contents.slice(end)
            }
            if (path.endsWith('fixed-layout.js')) {
                contents = contents.replace('const sheet = new CSSStyleSheet()', "const androidStyle = document.createElement('style'); this._androidStyle = androidStyle")
                    .replace('this.#root.adoptedStyleSheets = [sheet]', 'this.#root.append(androidStyle)')
                    .replace('sheet.replaceSync(', 'androidStyle.textContent = (')
                    .replaceAll('this.#root.replaceChildren()', 'this.#root.replaceChildren(this._androidStyle)')
            }
            if (path.endsWith('mobi.js')) {
                const start = contents.indexOf('const read32Bits = (byteArray, from) => {')
                const end = contents.indexOf('const huffcdic', start)
                if (start < 0 || end < 0) throw new Error('Reviewed MOBI compatibility patch no longer matches')
                contents = "import { read32BitsCompat } from '../../src/compat.mjs'\n" + contents.slice(0, start) + 'const read32Bits = read32BitsCompat\n\n' + contents.slice(end)
            }
            return { contents, loader: 'js', resolveDir: dirname(path) }
        })
    }
}

await build({ entryPoints: ['src/reader.js'], outfile: resolve(destination, 'reader.js'), bundle: true,
    format: 'iife', target: ['chrome58'], minify: false, legalComments: 'linked',
    plugins: [androidCompatibility], logLevel: 'info' })
await writeFile(resolve(destination, 'reader.html'), await readFile('src/reader.html'))
const licenses = await Promise.all([
    ['Foliate JS (MIT), revision 78914aef4466eb960965702401634c2cb348e9b1', 'vendor/foliate/LICENSE'],
    ['fflate 0.8.2 (MIT)', 'node_modules/fflate/LICENSE'],
    ['core-js 3.46.0 (MIT)', 'node_modules/core-js/LICENSE'],
    ['ResizeObserver polyfill 1.5.1 (MIT)', 'node_modules/resize-observer-polyfill/LICENSE']
].map(async ([name, path]) => `${name}\n${await readFile(path, 'utf8')}`))
await writeFile(resolve(destination, 'LICENSES.txt'), licenses.join('\n\n----------------\n\n'))
