import { test } from 'node:test'
import assert from 'node:assert/strict'
import { readFile } from 'node:fs/promises'
import { patchSearch } from '../src/search-compat.mjs'

const source = patchSearch(await readFile(new URL('../vendor/foliate/search.js', import.meta.url), 'utf8'))
const { search } = await import(`data:text/javascript;base64,${Buffer.from(source).toString('base64')}`)
test('legacy matcher finds a query ending at the document boundary', () => {
    const results = [...search(['Beginning ', 'the end'], 'the end', { sensitivity: 'accent' })]
    assert.equal(results.length, 1)
    assert.equal(results[0].excerpt.match, 'the end')
    assert.equal(results[0].range.endOffset, 7)
})
test('legacy excerpts preserve text crossing multiple nodes', () => {
    const results = [...search(['before hello', ' beautiful ', 'world after'], 'hello beautiful world', { sensitivity: 'accent' })]
    assert.equal(results[0].excerpt.match, 'hello beautiful world')
    assert.equal(results[0].excerpt.pre, 'before ')
    assert.equal(results[0].excerpt.post, ' after')
})
test('no Unicode property escape remains in the API26 search runtime', () => assert.equal(source.includes('\\p{'), false))
