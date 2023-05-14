package cc.colorcat.viewmodel.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cc.colorcat.viewmodel.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val vm by viewModels<MainViewModel>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = this::class.java.simpleName
        binding.info.text = "$this\n$vm"

        binding.increase.setOnClickListener {
            vm.increase()
        }
        binding.decrease.setOnClickListener {
            vm.decrease()
        }
        binding.next.setOnClickListener {
            startActivity(Intent(this, AActivity::class.java))
        }

        lifecycleScope.launch {
            vm.count.collect {
                binding.count.text = it.toString()
            }
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
    }
}