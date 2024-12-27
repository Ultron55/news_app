package example.news.app.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import example.news.app.R
import example.news.app.databinding.FragmentSavedNewsBinding
import example.news.app.ui.adapter.NewsAdapter
import example.news.app.ui.main.MainViewModel

@AndroidEntryPoint
class SavedNewsFragment : Fragment()  {
    private val viewModel: MainViewModel by activityViewModels()

    private var _binding : FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedNews()
        initUI()
        initObservers()
    }

    private fun initUI() {
        adapter = NewsAdapter(false) {
            val savedNewsContentFragment = SavedNewsContentFragment(it)
            requireParentFragment().parentFragmentManager.beginTransaction()
                .replace(R.id.main_activity_container, savedNewsContentFragment)
                .addToBackStack(null)
                .commit()

        }
        binding.savedNewsRv.adapter = adapter
    }

    private fun initObservers() {
        viewModel.savedNews.observe(viewLifecycleOwner) { adapter.update(it) }
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.loader.root.isVisible = it }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}