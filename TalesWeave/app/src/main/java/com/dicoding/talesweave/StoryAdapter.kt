package com.dicoding.talesweave

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.talesweave.databinding.ListDetailBinding
import com.dicoding.talesweave.response.ListStoryItem
import com.dicoding.talesweave.main.DetailAct

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    inner class StoryViewHolder(private val binding: ListDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ListStoryItem) {
            binding.nameTextview.text = user.name
            binding.tvDescription.text = user.description
            Glide.with(itemView.context).load(user.photoUrl)
                .into(binding.storyImg)

            binding.listDetail.setOnClickListener {
                val intent = Intent(itemView.context, DetailAct::class.java)
                intent.putExtra(DetailAct.EXTRA_NAME, user.name)
                intent.putExtra(DetailAct.EXTRA_DESCRIPTION, user.description)
                intent.putExtra(DetailAct.EXTRA_PHOTO, user.photoUrl)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        return StoryViewHolder(
            ListDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}