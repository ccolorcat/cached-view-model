package cc.colorcat.viewmodel.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cc.colorcat.viewmodel.cachedViewModels
import cc.colorcat.viewmodel.sample.databinding.ActivityBaseBinding
import cc.colorcat.xlogger.XLogger
import kotlinx.coroutines.launch

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
abstract class BaseActivity : AppCompatActivity() {
    private val logger = XLogger.getLogger(this::class.java.simpleName)
    private val vm by cachedViewModels<MainViewModel>()

    private val binding by lazy {
        ActivityBaseBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = this::class.java.simpleName

        XLogger.getLogger("CachedViewModel").d { "$this ViewModel=$vm" }


        binding.increase.setOnClickListener {
            vm.increase()
            logger.d { "increase clicked..." }
        }
        binding.decrease.setOnClickListener {
            vm.decrease()
            logger.d { "decrease clicked..." }
        }

        binding.jump.setOnClickListener {
            val next = nextActivity ?: return@setOnClickListener
            startActivity(Intent(this, next))
        }

        lifecycleScope.launch {
            vm.count.collect {
                logger.d { "count changed: $it" }
                binding.text.text = it.toString()
            }
        }
    }

    open val nextActivity: Class<out AppCompatActivity>? = null
}
