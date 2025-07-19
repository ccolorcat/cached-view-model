package cc.colorcat.viewmodel

import androidx.annotation.RestrictTo
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class CachedViewModelLazy<VM : ViewModel>(
    private val lifecycleOwnerProducer: () -> LifecycleOwner,
    private val viewModelClass: KClass<VM>,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val extrasProducer: () -> CreationExtras = { CreationExtras.Empty }
) : Lazy<VM> {
    private var cachedViewModelHolder: CachedViewModelHolder<VM>? = null
    private val lifecycleObserver: LifecycleObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            owner.lifecycle.removeObserver(this)
            cachedViewModelHolder = null
        }
    }

    override val value: VM
        get() {
            val holder = this@CachedViewModelLazy.cachedViewModelHolder
            return if (holder == null) {
                val factory = factoryProducer()
                CachedViewModelProvider(factory, extrasProducer())
                    .get(viewModelClass.java)
                    .also { cacheHolder(it) }
                    .viewModel
            } else {
                holder.viewModel
            }
        }

    override fun isInitialized(): Boolean = cachedViewModelHolder != null

    private fun cacheHolder(holder: CachedViewModelHolder<VM>) {
        cachedViewModelHolder = holder
        lifecycleOwnerProducer.invoke().lifecycle.run {
            addObserver(lifecycleObserver)
            addObserver(holder)
        }
    }
}
