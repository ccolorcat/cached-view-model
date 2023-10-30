package cc.colorcat.viewmodel

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Author: ccolorcat
 * Date: 2023-06-06
 * GitHub: https://github.com/ccolorcat
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object GlobalViewModelOwner : ViewModelStoreOwner {
    private val store = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = store
}
