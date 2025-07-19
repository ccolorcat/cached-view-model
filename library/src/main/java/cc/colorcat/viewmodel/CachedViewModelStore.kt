package cc.colorcat.viewmodel

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal object CachedViewModelStore {
    private val viewModelCache: MutableMap<String, CachedViewModelHolder<*>> = hashMapOf()

    internal fun put(key: String, holder: CachedViewModelHolder<*>) {
        viewModelCache.put(key, holder)?.forceDestroy()
    }

    internal fun get(key: String): CachedViewModelHolder<*>? {
        return viewModelCache[key]
    }

    internal fun remove(key: String, holder: CachedViewModelHolder<*>) {
        if (viewModelCache[key] === holder) {
            viewModelCache.remove(key)
        }
    }
}
