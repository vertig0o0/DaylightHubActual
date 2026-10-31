/** Equivalent to Foliate's original BigInt bit window, including zero padding. */
export function read32BitsCompat(bytes, from) {
    let result = 0
    for (let i = 0; i < 32; i++) {
        const position = from + i
        result = ((result << 1) | (((bytes[position >>> 3] || 0) >>> (7 - (position & 7))) & 1)) >>> 0
    }
    return result
}
