package cc.colorcat.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.VIEW_MODEL_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
internal class CachedViewModelProvider(
    private val factory: ViewModelProvider.Factory,
    private val defaultCreationExtras: CreationExtras = CreationExtras.Empty,
) {
    private val store = CachedViewModelStore

    @MainThread
    fun <T : ViewModel> get(modelClass: Class<T>): ViewModelWrapper<T> {
        val canonicalName = modelClass.canonicalName
            ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")
        return get("$DEFAULT_KEY:$canonicalName", modelClass)
    }

    @Suppress("UNCHECKED_CAST")
    @MainThread
    fun <T : ViewModel> get(key: String, modelClass: Class<T>): ViewModelWrapper<T> {
        val wrapper = store.get(key)
        val viewModel = wrapper?.viewModel
        if (modelClass.isInstance(viewModel)) {
            (factory as? ViewModelProvider.OnRequeryFactory)?.onRequery(viewModel!!)
            return wrapper as ViewModelWrapper<T>
        } else {
            if (viewModel != null) {
                Log.w(
                    "CachedViewModelProvider",
                    "Key($key) and ViewModel($viewModel) are associated, but required type: $modelClass"
                )
            }
        }
        val extras = MutableCreationExtras(defaultCreationExtras)
        extras[VIEW_MODEL_KEY] = key
        return try {
            factory.create(modelClass, extras)
        } catch (e: AbstractMethodError) {
            factory.create(modelClass)
        }.let { ViewModelWrapper(key, it) }.also { store.put(key, it) }
    }

    private companion object {
        const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"
    }
}
