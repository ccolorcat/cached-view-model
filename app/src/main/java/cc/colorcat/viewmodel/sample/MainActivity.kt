package cc.colorcat.viewmodel.sample

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cc.colorcat.viewmodel.sample.databinding.ActivityMainBinding
import cc.colorcat.xlogger.XLogger
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val vm by viewModels<MainViewModel>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        title = "MainActivity"

        XLogger.getLogger("CachedViewModel").d { "$this ViewModel=$vm" }


        binding.increase.setOnClickListener {
            vm.increase()
        }
        binding.decrease.setOnClickListener {
            vm.decrease()
        }
        binding.jump.setOnClickListener {
            startActivity(Intent(this, BActivity::class.java))
        }

        lifecycleScope.launch {
            vm.count.collect {
                binding.text.text = it.toString()
            }
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
    }
}