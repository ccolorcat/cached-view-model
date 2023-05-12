package cc.colorcat.viewmodel.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cc.colorcat.viewmodel.cachedViewModels
import cc.colorcat.viewmodel.sample.databinding.ActivityContainerBinding
import cc.colorcat.xlogger.XLogger
import kotlinx.coroutines.launch

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
class ContainerActivity : AppCompatActivity() {
    private val vm: MainViewModel by cachedViewModels()
    private val binding by lazy {
        ActivityContainerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = "Container"

        XLogger.getLogger("CachedViewModel").d { "$this ViewModel=$vm" }

        lifecycleScope.launch {
            vm.count.collect {
                binding.count.text = it.toString()
            }
        }

    }
}