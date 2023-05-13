package cc.colorcat.viewmodel.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cc.colorcat.viewmodel.cachedViewModels
import cc.colorcat.viewmodel.sample.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
class MainFragment : Fragment() {
    private val vm: MainViewModel by cachedViewModels()

    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.increase.setOnClickListener {
            vm.increase()
        }
        binding.decrease.setOnClickListener {
            vm.decrease()
        }

        lifecycleScope.launch {
            vm.count.collect {
                binding.text.text = it.toString()
            }
        }
    }
}