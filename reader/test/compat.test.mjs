import { test } from 'node:test'
import assert from 'node:assert/strict'
import { read32BitsCompat } from '../src/compat.mjs'

function original(bytes, from) {
    const end = from + 32
    let bits = 0n
    for (let i = from >> 3; i <= end >> 3; i++) bits = bits << 8n | BigInt(bytes[i] ?? 0)
    return Number(bits >> (8n - BigInt(end & 7)) & 0xffffffffn)
}

test('HUFF/CDIC windows match upstream at byte boundaries and partial tails', () => {
    let seed = 0x12345678
    for (let sample = 0; sample < 200; sample++) {
        const bytes = Uint8Array.from({ length: 64 }, () => { seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0; return seed >>> 24 })
        for (let bit = 0; bit < bytes.length * 8; bit++) assert.equal(read32BitsCompat(bytes, bit), original(bytes, bit))
    }
})
