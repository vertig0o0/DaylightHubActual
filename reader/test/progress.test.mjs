import { test } from 'node:test'
import assert from 'node:assert/strict'
import { logicalFraction } from '../src/progress.mjs'

test('the same text anchor has the same progress at every font size', () => {
    const normal = logicalFraction(1, 25, 100, [100, 300, 100])
    const large = logicalFraction(1, 25, 100, [100, 300, 100])
    assert.equal(normal, .35)
    assert.equal(normal, large)
})
test('empty sections and out-of-range anchors stay finite and bounded', () => {
    assert.equal(logicalFraction(0, 0, 0, []), 0)
    assert.equal(logicalFraction(-1, 0, 10, [10]), 0)
    assert.equal(logicalFraction(0, -50, 10, [10]), 0)
    assert.equal(logicalFraction(0, 50, 10, [10]), 1)
})
