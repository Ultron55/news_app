package example.news.app.ui.online

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import example.news.app.databinding.FragmentOnlineNewsBinding
import example.news.app.ui.adapter.NewsAdapter
import example.news.app.ui.main.MainViewModel
import example.news.data.data.model.request.EverythingNewsRequest
import java.util.Calendar

@AndroidEntryPoint
class OnlineNewsFragment : Fragment() {
    private var _binding: FragmentOnlineNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var adapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnlineNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter (true) { viewModel.saveNews(listOf(it)) }
        binding.newsRv.adapter = adapter
        initUI()
        initObservers()
        initClickListener()
    }

    private fun initUI() {
        binding.searchEt.setText(viewModel.everythingNewsRequest.searchRequest)
        val calendar = Calendar.getInstance()
        binding.toDateTv.text = viewModel.everythingNewsRequest.toDate.let {
            if (it.isNullOrEmpty()) getDateString(calendar) else it
        }
        binding.fromDateTv.text = viewModel.everythingNewsRequest.fromDate.let {
            if (it.isNullOrEmpty()) {
                calendar.add(Calendar.MONTH, -1)
                getDateString(calendar)
            } else it
        }
        val index = viewModel.everythingNewsRequest.sortBy?.let {
            EverythingNewsRequest.sortBy.indexOf(it) } ?: 0
        binding.sortByRg.check(binding.sortByRg[index].id)
    }

    private fun initClickListener() {
        binding.fromDateTv.setOnClickListener { dataPicker(it as TextView, true)}
        binding.toDateTv.setOnClickListener { dataPicker(it as TextView, false)}
        binding.searchBtn.setOnClickListener { search() }
        binding.sortByRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.publishedAtRb.id -> viewModel.everythingNewsRequest.sortBy = "publishedAt"
                binding.relevancyRb.id -> viewModel.everythingNewsRequest.sortBy = "relevancy"
                binding.popularityRb.id -> viewModel.everythingNewsRequest.sortBy = "popularity"
            }
        }
    }

    private fun dataPicker(textView: TextView, isFrom: Boolean) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textView.text = getDateString(calendar)
                if (isFrom) viewModel.everythingNewsRequest.fromDate = textView.text.toString()
                else viewModel.everythingNewsRequest.toDate = textView.text.toString()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        .show()
    }

    private fun getDateString(calendar: Calendar) =
        EverythingNewsRequest.dateFormat.format(calendar.time)

    private fun search() {
        val query = binding.searchEt.text.toString()
        binding.searchTil.error =
            if (query.isEmpty()) "Query is empty".also { return } else null
        viewModel.everythingNewsRequest.searchRequest = query
        viewModel.getEverythingNews()
    }

    private fun initObservers() {
        viewModel.news.observe(viewLifecycleOwner) { adapter.update(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}