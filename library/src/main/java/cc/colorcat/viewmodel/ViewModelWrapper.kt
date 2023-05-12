package cc.colorcat.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cc.colorcat.xlogger.XLogger

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal class ViewModelWrapper<VM : ViewModel>(private val key: String, val viewModel: VM) : DefaultLifecycleObserver {
    private val logger = XLogger.getLogger("CachedViewModel")
    private val handler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    private var count = 0

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        ++count
        handler.removeCallbacksAndMessages(null)
        logger.d { "onCreate: action = increase: $owner, $viewModel, count=${count}" }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        --count
        owner.lifecycle.removeObserver(this)
        logger.i { "onDestroy: action = decrease: $owner, $viewModel, count=${count}" }
        if (count == 0) {
            handler.postDelayed(this::destroy, 5000)
        }
    }

    private fun destroy() {
        if (count == 0) {
            logger.w { "destroy $viewModel" }
            CachedViewModelStore.remove(key, this)
            clear()
        }
    }

    fun clear() {
        try {
            method?.invoke(viewModel)
        } catch (ignore: Throwable) {
        }
    }

    private companion object {
        private val method = kotlin.runCatching {
            ViewModel::class.java.getDeclaredMethod("clear").apply { isAccessible = true }
        }.getOrNull()
    }
}
