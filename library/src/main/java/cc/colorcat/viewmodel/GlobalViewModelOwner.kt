package cc.colorcat.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Author: ccolorcat
 * Date: 2023-06-06
 * GitHub: https://github.com/ccolorcat
 */
object GlobalViewModelOwner : ViewModelStoreOwner {
    private val store = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return store
    }
}
