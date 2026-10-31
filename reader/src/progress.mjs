/** A logical location uses content offsets; font size and viewport pages are irrelevant. */
export function logicalFraction(index, offset, totalInSection, weights) {
    const total = weights.reduce((sum, value) => sum + value, 0)
    if (!total || index < 0 || index >= weights.length) return 0
    const before = weights.slice(0, index).reduce((sum, value) => sum + value, 0)
    const within = totalInSection > 0 ? Math.min(1, Math.max(0, offset / totalInSection)) : 0
    return Math.min(1, Math.max(0, (before + weights[index] * within) / total))
}
