package me.daffakurnia.android.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import me.daffakurnia.android.storyapp.R
import me.daffakurnia.android.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_story)

        Glide.with(binding.imgItemPhoto)
            .load(intent.getStringExtra(PHOTO_URL))
            .into(binding.imgItemPhoto)

        binding.apply {
            imgName.text = intent.getStringExtra(NAME)
            imgDesc.text = intent.getStringExtra(DESCRIPTION)
        }
    }

    companion object {
        const val PHOTO_URL = "photoUrl"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}