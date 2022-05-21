package me.daffakurnia.android.storyapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.daffakurnia.android.storyapp.R
import me.daffakurnia.android.storyapp.data.Stories

class ListStoriesAdapter(private val listStories: ArrayList<Stories>) : RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var textViewName: TextView = itemView.findViewById(R.id.img_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_stories, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photoUrl, name, description) = listStories[position]
        Glide.with(holder.imgPhoto.context)
            .load(photoUrl)
            .into(holder.imgPhoto)
        holder.textViewName.text = name
        holder.imgPhoto.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PHOTO_URL, photoUrl)
            intent.putExtra(DetailActivity.NAME, name)
            intent.putExtra(DetailActivity.DESCRIPTION, description)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listStories.size
}