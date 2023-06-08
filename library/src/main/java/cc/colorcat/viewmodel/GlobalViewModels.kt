package cc.colorcat.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * Author: ccolorcat
 * Date: 2023-06-06
 * GitHub: https://github.com/ccolorcat
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.globalViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(
        VM::class,
        { GlobalViewModelOwner.viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}


@MainThread
inline fun <reified VM : ViewModel> Fragment.globalViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = viewModels(
    ownerProducer = { GlobalViewModelOwner },
    extrasProducer = extrasProducer,
    factoryProducer = factoryProducer,
)
