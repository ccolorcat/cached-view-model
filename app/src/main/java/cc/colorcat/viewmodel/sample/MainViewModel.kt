package cc.colorcat.viewmodel.sample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Author: ccolorcat
 * Date: 2023-05-11
 * GitHub: https://github.com/ccolorcat
 */
class MainViewModel : BaseViewModel() {
    private val _count = MutableStateFlow(0)

    val count: StateFlow<Int> = _count.asStateFlow()

    fun increase() {
        _count.value = count.value + 1
    }

    fun decrease() {
        _count.value = count.value - 1
    }
}