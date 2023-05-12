package cc.colorcat.viewmodel

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
class CachedViewModelLazy<VM : ViewModel>(
    private val owner: LifecycleOwner,
    private val viewModelClass: KClass<VM>,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val extrasProducer: () -> CreationExtras = { CreationExtras.Empty }
) : Lazy<VM> {
    private var cached: ViewModelWrapper<VM>? = null

    override val value: VM
        get() {
            val wrapper = cached
            return if (wrapper == null) {
                val factory = factoryProducer()
                CachedViewModelProvider(factory, extrasProducer())
                    .get(viewModelClass.java)
                    .also { cache(it) }
                    .viewModel
            } else {
                wrapper.viewModel
            }
        }

    override fun isInitialized(): Boolean = cached != null

    private fun cache(wrapper: ViewModelWrapper<VM>) {
        cached = wrapper
        owner.lifecycle.addObserver(wrapper)
    }
}
