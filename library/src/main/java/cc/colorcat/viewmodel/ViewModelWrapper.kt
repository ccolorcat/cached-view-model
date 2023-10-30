package cc.colorcat.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal class ViewModelWrapper<VM : ViewModel>(
    private val key: String,
    val viewModel: VM
) : DefaultLifecycleObserver {
    private val handler = Handler(Looper.getMainLooper())

    private var count = 0

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        ++count
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        --count
        owner.lifecycle.removeObserver(this)
        if (count == 0) {
            handler.post(this::destroy)
        }
    }

    private fun destroy() {
        if (count == 0) {
            CachedViewModelStore.remove(key, this)
            clear()
        }
    }

    fun clear() {
        ViewModelStore().run {
            put(key, viewModel)
            clear()
        }
    }
}
