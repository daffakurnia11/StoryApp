package me.daffakurnia.android.storyapp.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.daffakurnia.android.storyapp.databinding.ItemRowStoriesBinding
import me.daffakurnia.android.storyapp.response.StoriesResponseItem

class ListStoriesAdapter :
    PagingDataAdapter<StoriesResponseItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(private var binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoriesResponseItem) {
            with(binding) {
                Glide.with(imgItemPhoto.context)
                    .load(data.photoUrl)
                    .into(imgItemPhoto)
                imgName.text = data.name
                imgItemPhoto.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.PHOTO_URL, data.photoUrl)
                    intent.putExtra(DetailActivity.NAME, data.name)
                    intent.putExtra(DetailActivity.DESCRIPTION, data.description)

                    val optionCombat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgItemPhoto, "image"),
                            Pair(imgName, "name")
                        )

                    itemView.context.startActivity(intent, optionCombat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesResponseItem>() {
            override fun areItemsTheSame(
                oldItem: StoriesResponseItem,
                newItem: StoriesResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoriesResponseItem,
                newItem: StoriesResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}