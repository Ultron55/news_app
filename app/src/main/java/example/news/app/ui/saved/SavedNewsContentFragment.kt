package example.news.app.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import example.news.app.R
import example.news.app.databinding.FragmentSavedNewsContentBinding
import example.news.data.domain.model.News

class SavedNewsContentFragment(val news: News) : Fragment() {
    private var _binding : FragmentSavedNewsContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
        binding.titleTv.text = news.title
        binding.authorNameTv.text = news.author
        binding.timeTv.text = news.publishedAt
        binding.descriptionTv.text = news.description
        binding.url.text = news.url
        Glide
            .with(requireContext())
            .load(news.imageUrl)
            .fitCenter()
            .placeholder(R.drawable.image_placeholder)
            .into(binding.previewImv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}