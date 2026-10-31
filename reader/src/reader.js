import 'core-js/actual/array/at'
import 'core-js/actual/array/flat'
import 'core-js/actual/array/find-last-index'
import 'core-js/actual/object/from-entries'
import 'core-js/actual/object/group-by'
import 'core-js/actual/map/group-by'
import 'core-js/actual/global-this'
import 'core-js/actual/string/match-all'
import 'core-js/actual/string/replace-all'
import 'core-js/actual/string/trim-start'
import 'core-js/actual/string/trim-end'
import 'core-js/actual/symbol/async-iterator'
import 'core-js/actual/promise/finally'
import ResizeObserver from 'resize-observer-polyfill'
import { unzlibSync } from 'fflate'
import { EPUB } from '../vendor/foliate/epub.js'
import { MOBI } from '../vendor/foliate/mobi.js'
import { makeFB2 } from '../vendor/foliate/fb2.js'
import { logicalFraction } from './progress.mjs'
import '../vendor/foliate/view.js'

window.ResizeObserver ||= ResizeObserver
window.visualViewport ||= { scale: 1 }
if (!Element.prototype.toggleAttribute) Element.prototype.toggleAttribute = function (name, force) {
    const next = arguments.length > 1 ? Boolean(force) : !this.hasAttribute(name)
    if (next) this.setAttribute(name, ''); else this.removeAttribute(name)
    return next
}
for (const prototype of [Element.prototype, DocumentFragment.prototype]) {
    if (!prototype.replaceChildren) prototype.replaceChildren = function (...nodes) {
        while (this.firstChild) this.removeChild(this.firstChild)
        for (const node of nodes) this.appendChild(typeof node === 'string' ? document.createTextNode(node) : node)
    }
}
if (!Blob.prototype.arrayBuffer) Blob.prototype.arrayBuffer = function () { return new Promise((resolve, reject) => { const reader = new FileReader(); reader.onload = () => resolve(reader.result); reader.onerror = reject; reader.readAsArrayBuffer(this) }) }
if (!Blob.prototype.text) Blob.prototype.text = function () { return new Promise((resolve, reject) => { const reader = new FileReader(); reader.onload = () => resolve(reader.result); reader.onerror = reject; reader.readAsText(this) }) }

let port, view, publication, preferences = {}, lastLocation, searchEpoch = 0
let navigation = Promise.resolve(), navigationEpoch = 0, queuedNavigation = 0
let documentOffsets = new WeakMap()
const state = document.getElementById('state')
const notify = (type, value = {}) => {
    let raw = JSON.stringify({ type, ...value })
    if (raw.length > 60000 && Array.isArray(value.results)) {
        while (raw.length > 60000 && value.results.length) { value.results.pop(); raw = JSON.stringify({ type, ...value }) }
    }
    if (raw.length <= 60000) port?.postMessage(raw)
}
function boundedToc(items) {
    let remaining = 50000, count = 0
    const walk = (values, depth) => {
        const result = []
        if (depth > 10) return result
        for (const item of values || []) {
            if (++count > 500 || remaining <= 0) break
            const node = { label: String(item.label || '').slice(0, 200), href: String(item.href || '').slice(0, 4096) }
            remaining -= JSON.stringify(node).length + 32
            if (remaining < 0) break
            node.subitems = walk(item.subitems, depth + 1)
            result.push(node)
        }
        return result
    }
    return walk(items, 0)
}
const fail = error => { state.style.display = 'block'; state.textContent = error.message || 'This publication could not be opened.'; notify('error', { message: state.textContent }) }
window.addEventListener('error', event => fail(event.error || new Error(event.message)))
window.addEventListener('unhandledrejection', event => fail(event.reason || new Error('Reader failed')))

const fetchLocal = async url => {
    const parsed = new URL(url, location.href)
    if (parsed.origin !== location.origin) throw new Error('External book resources stay offline')
    return fetch(parsed)
}

function applyAppearance(prefs) {
    preferences = { ...preferences, ...prefs }
    const dark = preferences.theme === 'Dark'
    const sepia = preferences.theme === 'Sepia'
    const background = dark ? '#141916' : sepia ? '#f4ecd8' : '#ffffff'
    const foreground = dark ? '#e4e9e5' : '#17231e'
    document.body.style.background = background
    document.body.style.color = foreground
    const size = Math.max(12, Math.min(40, Number(preferences.fontSize) || 18))
    const height = Math.max(1.2, Math.min(2.2, Number(preferences.lineHeight) || 1.6))
    const font = ['serif', 'sans-serif', 'monospace'].includes(preferences.font) ? preferences.font : 'serif'
    if (publication?.rendition?.layout === 'pre-paginated') {
        view?.renderer?.setAttribute('zoom', ['fit-page', 'fit-width', '1', '1.5', '2'].includes(preferences.fixedZoom) ? preferences.fixedZoom : 'fit-page')
        return
    }
    view?.renderer?.setStyles?.(`html{color:${foreground}!important;background:${background}!important;color-scheme:${dark ? 'dark' : 'light'}}body{font-family:${font}!important;font-size:${size}px!important;line-height:${height}!important}a{color:${dark ? '#91d5b6' : '#366a59'}}`)
    if (view?.renderer) view.renderer.setAttribute('flow', preferences.scrolling ? 'scrolled' : 'paginated')
    if (view?.renderer) view.renderer.toggleAttribute('animated', !preferences.reducedMotion)
}

async function open(config) {
    view?.close?.(); view?.remove()
    publication?.destroy?.()
    documentOffsets = new WeakMap()
    lastLocation = null
    if (config.format === 'EPUB') {
        const metadata = await (await fetchLocal(`${config.base}/manifest`)).json()
        const entryURL = name => `${config.base}/entry?name=${encodeURIComponent(name)}`
        publication = await new EPUB({
            loadText: async name => { const response = await fetchLocal(entryURL(name)); return response.ok ? response.text() : null },
            loadBlob: async name => { const response = await fetchLocal(entryURL(name)); return response.ok ? response.blob() : null },
            getSize: name => metadata.entries[name] || 0
        }).init()
    } else if (config.format === 'MOBI') {
        const file = { size: config.size, name: config.name, type: config.mime,
            slice(start = 0, end = config.size) { return { arrayBuffer: async () => {
                const response = await fetchLocal(`${config.base}/range?start=${start}&end=${end}`)
                if (!response.ok) throw new Error('A book resource exceeds the safe reading limit')
                return response.arrayBuffer()
            } } } }
        publication = await new MOBI({ unzlib: unzlibSync }).open(file)
    } else if (config.format === 'FB2') {
        const response = await fetchLocal(`${config.base}/file`)
        if (!response.ok) throw new Error('This FB2 needs offline normalization before reading')
        publication = await makeFB2(await response.blob())
    } else throw new Error('Unsupported reflowable format')

    view = document.createElement('foliate-view')
    document.body.append(view)
    view.addEventListener('relocate', event => {
        const location = event.detail
        const fixed = publication.rendition?.layout === 'pre-paginated'
        let fraction = fixed ? ((location.section?.current ?? 0) + 1) / Math.max(1, publication.sections.length) : 0
        const range = location.range
        if (range && !fixed) {
            const doc = range.startContainer.ownerDocument
            let cached = documentOffsets.get(doc)
            if (!cached) {
                cached = { offsets: new WeakMap(), total: 0 }
                const walker = doc.createTreeWalker(doc.body, NodeFilter.SHOW_TEXT)
                for (let node = walker.nextNode(); node; node = walker.nextNode()) {
                    cached.offsets.set(node, cached.total)
                    cached.total += node.textContent.length
                }
                documentOffsets.set(doc, cached)
            }
            let offset = cached.offsets.get(range.startContainer)
            if (offset !== undefined) offset += range.startOffset
            else { const prefix = doc.createRange(); prefix.selectNodeContents(doc.body); prefix.setEnd(range.startContainer, range.startOffset); offset = prefix.toString().length }
            fraction = logicalFraction(location.section?.current ?? 0, offset, cached.total, publication.sections.map(section => section.linear === 'no' ? 0 : Math.max(1, section.size || 1)))
        }
        lastLocation = { locator: location.cfi || '', fraction, section: location.section?.current ?? 0,
            chapter: location.tocItem?.label || '', quote: location.range?.toString?.().slice(0, 160) || '' }
        notify('location', lastLocation)
    })
    view.addEventListener('load', event => {
        const doc = event.detail.doc
        let lastInteraction = 0
        const interact = () => { const now = Date.now(); if (now - lastInteraction > 1000) { lastInteraction = now; notify('interact') } }
        for (const type of ['click', 'touchstart', 'touchmove', 'wheel', 'keydown']) doc.addEventListener(type, interact, { passive: true })
        doc.addEventListener('selectionchange', () => {
            const text = doc.getSelection()?.toString() || ''
            if (text && text.length <= 1600) notify('selection', { text })
        })
    })
    await view.open(publication)
    applyAppearance(config.preferences || {})
    if (config.locator) await view.goTo(config.locator)
    else await view.init({ lastLocation: null })
    state.style.display = 'none'
    const author = publication.metadata?.author
    const authorName = typeof author === 'string' ? author : Array.isArray(author) ? author.map(value => typeof value === 'string' ? value : value.name || '').join(', ') : author?.name || ''
    const title = publication.metadata?.title
    notify('opened', { title: typeof title === 'string' ? title : config.title || config.name, author: authorName, toc: boundedToc(publication.toc), fixedLayout: publication.rendition?.layout === 'pre-paginated', sections: publication.sections.length })
}

async function command(message) {
    switch (message.type) {
        case 'open': await open(message); break
        case 'next': await view?.next(); notify('interact'); break
        case 'previous': await view?.prev(); notify('interact'); break
        case 'goTo': await view?.goTo(message.locator); notify('interact'); break
        case 'appearance': applyAppearance(message.preferences); break
        case 'bookmark': if (lastLocation) notify('bookmark', lastLocation); break
        case 'search': {
            const epoch = ++searchEpoch
            const query = String(message.query || '').trim().slice(0, 200)
            const results = []
            if (query) for await (const found of view.search({ query })) {
                if (epoch !== searchEpoch) return
                for (const match of found.subitems || []) {
                    if (results.length >= 100) break
                    const excerpt = match.excerpt
                    results.push({ label: found.label || '', locator: match.cfi, excerpt: typeof excerpt === 'string' ? excerpt : `${excerpt?.pre || ''}${excerpt?.match || ''}${excerpt?.post || ''}` })
                }
                if (results.length >= 100) break
            }
            if (epoch === searchEpoch) notify('search', { results }); break
        }
        case 'cancelSearch': ++searchEpoch; break
        case 'close': ++searchEpoch; view?.close?.(); publication?.destroy?.(); break
    }
}

window.addEventListener('message', event => {
    if (port || event.data !== 'prospacehub-reader-channel' || !event.ports?.[0]) return
    port = event.ports[0]
    port.onmessage = event => {
        try {
            const message = JSON.parse(event.data)
            if (message.type === 'close') ++navigationEpoch
            if (['open', 'next', 'previous', 'goTo', 'appearance'].includes(message.type)) {
                if (queuedNavigation >= 16) return
                const epoch = navigationEpoch
                ++queuedNavigation
                navigation = navigation.then(() => epoch === navigationEpoch ? command(message) : undefined)
                    .catch(fail).finally(() => { --queuedNavigation })
            } else command(message).catch(fail)
        } catch (error) { fail(error) }
    }
    port.start?.()
    notify('ready')
})
