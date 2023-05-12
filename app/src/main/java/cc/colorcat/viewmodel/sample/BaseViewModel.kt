package cc.colorcat.viewmodel.sample

import androidx.lifecycle.ViewModel
import cc.colorcat.xlogger.XLogger

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
open class BaseViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        XLogger.getLogger("CachedViewModel").d { "onCleared..." }
    }
}
