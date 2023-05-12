package cc.colorcat.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.cachedViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return CachedViewModelLazy(
        owner = this,
        viewModelClass = VM::class,
        factoryProducer = factoryPromise,
        extrasProducer = { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}



inline fun <reified VM : ViewModel> Fragment.cachedViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    return createCachedViewModelLazy(
        viewModelClass = VM::class,
        extrasProducer = {
            extrasProducer?.invoke()
                ?: (this as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
                ?: CreationExtras.Empty
        },
        factoryProducer = factoryProducer ?: {
            (this as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                ?: defaultViewModelProviderFactory
        }
    )
}


fun <VM : ViewModel> Fragment.createCachedViewModelLazy(
    viewModelClass: KClass<VM>,
    extrasProducer: () -> CreationExtras = { defaultViewModelCreationExtras },
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return CachedViewModelLazy(
        owner = this,
        viewModelClass = viewModelClass,
        factoryProducer = factoryPromise,
        extrasProducer = extrasProducer
    )
}
