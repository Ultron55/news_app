package example.news.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import example.news.app.R
import example.news.app.databinding.FragmentMainBinding
import example.news.app.ui.main.adapter.TabsPagerAdapter

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : TabsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TabsPagerAdapter(this)
        binding.mainVp2.adapter = adapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainVp2) { tab, position ->
            tab.text = getString(
                when (position) {
                0 -> R.string.news
                1 -> R.string.saved
                else -> R.string.app_name
            })
        }.attach()
    }
}