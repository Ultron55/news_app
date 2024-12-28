package example.news.app.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import example.news.app.ui.online.OnlineNewsFragment
import example.news.app.ui.saved.SavedNewsFragment

class TabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) =
        when (position) {
            0 -> OnlineNewsFragment()
            1 -> SavedNewsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
}