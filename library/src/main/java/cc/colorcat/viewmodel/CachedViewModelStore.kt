package cc.colorcat.viewmodel

import cc.colorcat.xlogger.XLogger

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal object CachedViewModelStore {
    private val logger = XLogger.getLogger("CachedViewModel")

    private val cachedViewModel = HashMap<String, ViewModelWrapper<*>>()

    internal fun put(key: String, wrapper: ViewModelWrapper<*>) {
        cachedViewModel.put(key, wrapper)?.clear()
    }

    internal fun get(key: String): ViewModelWrapper<*>? {
        return cachedViewModel[key]
    }

    internal fun remove(key: String, wrapper: ViewModelWrapper<*>) {
        if (cachedViewModel[key] === wrapper) {
            cachedViewModel.remove(key)
            logger.w { "key=$key, wrapper=$wrapper removed, size=${cachedViewModel.size}" }
        }
    }
}
