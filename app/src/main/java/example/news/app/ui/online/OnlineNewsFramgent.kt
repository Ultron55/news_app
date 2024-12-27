package example.news.app.ui.online

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import example.news.app.databinding.FragmentOnlineNewsBinding
import example.news.app.ui.main.MainViewModel
import example.news.app.ui.online.adapter.NewsAdapter
import example.news.data.model.request.EverythingNewsRequest
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
        adapter = NewsAdapter()
        binding.newsRv.adapter = adapter
        initUI()
        initObservers()
        initClickListener()
    }

    private fun initUI() {
        binding.searchEt.setText(viewModel.everythingNewsRequest.searchRequest)
        val calendar = Calendar.getInstance()
        binding.toDateTv.text = viewModel.everythingNewsRequest.toDate ?: getDateString(calendar)
        binding.fromDateTv.text = viewModel.everythingNewsRequest.fromDate ?: calendar.let {
            it.add(Calendar.MONTH, -1)
            getDateString(calendar)
        }
    }

    private fun initClickListener() {
        binding.fromDateTv.setOnClickListener { dataPicker(it as TextView, true)}
        binding.toDateTv.setOnClickListener { dataPicker(it as TextView, false)}
        binding.searchBtn.setOnClickListener { search() }
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