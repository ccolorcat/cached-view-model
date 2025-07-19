package cc.colorcat.viewmodel

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import java.lang.reflect.Method

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal class CachedViewModelHolder<VM : ViewModel>(
    private val key: String,
    val viewModel: VM
) : DefaultLifecycleObserver {
    private val handler = Handler(Looper.getMainLooper())
    private var referenceCount = 0
    private var isCleared = false

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        ++referenceCount
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        --referenceCount
        owner.lifecycle.removeObserver(this)
        if (referenceCount == 0) {
            handler.post(this::tryDestroy)
        }
    }

    private fun tryDestroy() {
        if (referenceCount == 0 && !isCleared) {
            CachedViewModelStore.remove(key, this)
            clearViewModel()
        }
    }

    private fun clearViewModel() {
        if (!isCleared) {
            isCleared = true
            try {
                val clear = method
                if (clear == null) {
                    fallbackClear()
                } else {
                    clear.invoke(viewModel)
                }
            } catch (e: Throwable) {
                fallbackClear()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun fallbackClear() {
        runCatching {
            ViewModelStore().run {
                put(key, viewModel)
                clear()
            }
        }
    }

    fun forceDestroy() {
        handler.removeCallbacksAndMessages(null)
        clearViewModel()
    }

    private companion object {
        private val method: Method? by lazy {
            kotlin.runCatching {
                ViewModel::class.java.getDeclaredMethod("clear").apply { isAccessible = true }
            }.getOrNull()
        }
    }
}
