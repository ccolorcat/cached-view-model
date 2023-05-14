package cc.colorcat.viewmodel.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    private val vm by cachedViewModels<MainViewModel>()

    private val binding by lazy {
        ActivityBaseBinding.inflate(layoutInflater)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = this::class.java.simpleName

        binding.info.text = "$this\n$vm"
        nextActivity?.simpleName?.let {
            binding.next.isVisible = true
            binding.next.text = it
        }


        XLogger.getLogger("CachedViewModel").v { "$this ViewModel=$vm" }


        binding.increase.setOnClickListener {
            vm.increase()
        }
        binding.decrease.setOnClickListener {
            vm.decrease()
        }

        binding.next.setOnClickListener {
            val next = nextActivity ?: return@setOnClickListener
            startActivity(Intent(this, next))
        }

        lifecycleScope.launch {
            vm.count.collect {
                binding.count.text = it.toString()
            }
        }
    }

    open val nextActivity: Class<out AppCompatActivity>? = null
}
