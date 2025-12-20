export function deepMerge(target: any, source: any): any {
    if (source === null || source === undefined) return target;
    if (typeof source !== "object") return source;
    if (Array.isArray(source)) return source;

    const result = { ...target };

    for (const key in source) {
        if (Object.hasOwn(source, key)) {
            if (typeof source[key] === "object" && source[key] !== null && !Array.isArray(source[key])) {
                result[key] = deepMerge(result[key] || {}, source[key]);
            } else {
                result[key] = source[key];
            }
        }
    }

    return result;
}