// Reviewed fixes to upstream's fallback matcher, shared by the bundle and Node checks.
export function patchSearch(source) {
    return source.replace('strs.slice(start + 1, end)', 'strs.slice(startIndex + 1, endIndex)')
        .replace('while (sum <= end) sum += strs[++strIndex].length', 'while (sum < end && strIndex + 1 < strs.length) sum += strs[++strIndex].length')
        .replace('/[^\\p{Format}]/u', '/[^\\u00AD\\u061C\\u180E\\u200B-\\u200F\\u202A-\\u202E\\u2060-\\u206F\\uFEFF]/')
}
