package example.news.app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.news.app.R
import example.news.app.databinding.ItemNewsBinding
import example.news.data.domain.model.News

class NewsAdapter(
    private val isOnline : Boolean,
    val onClick : (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val news = mutableListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = news[position]
        holder.binding.titleTv.text = news.title
        holder.binding.url.text = news.url
        holder.binding.timeTv.text = news.publishedAt
        Glide
            .with(holder.binding.root.context)
            .load(news.imageUrl)
            .fitCenter()
            .placeholder(R.drawable.image_placeholder)
            .into(holder.binding.previewImv)
        if (isOnline) {
            holder.binding.saveTv.let {
                it.setOnClickListener { onClick(news) }
                it.isVisible = true
                holder.binding.openTv.isVisible = false
            }
        } else {
            holder.binding.openTv.let {
                it.setOnClickListener { onClick(news) }
                it.isVisible = true
                holder.binding.saveTv.isVisible = false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList : List<News>) {
        news.clear()
        news.addAll(newList)
        notifyDataSetChanged()
    }

    class NewsViewHolder(val binding : ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)
}