package me.daffakurnia.android.storyapp.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.daffakurnia.android.storyapp.R
import me.daffakurnia.android.storyapp.data.Stories
import me.daffakurnia.android.storyapp.databinding.ItemRowStoriesBinding
import me.daffakurnia.android.storyapp.response.ListStoryItem
import me.daffakurnia.android.storyapp.response.StoriesResponse

class ListStoriesAdapter:
    PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(private var binding: ItemRowStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
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
        val binding = ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}