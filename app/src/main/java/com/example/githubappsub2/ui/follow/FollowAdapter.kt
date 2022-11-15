package com.example.githubappsub2.ui.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.ItemRowFollowBinding

class followAdapter(private val gitUserList: List<GitDetailResponse>) : RecyclerView.Adapter<followAdapter.ViewHolder>(){
    class ViewHolder(val binding: ItemRowFollowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitUSer = gitUserList[position]
        Glide.with(holder.itemView.context)
            .load(gitUSer.avatarUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(holder.binding.imgItemPhotoFollow)
        holder.apply {
            binding.apply {
                tvItemNameFollow.text = gitUSer.login
            }
        }
    }

    override fun getItemCount(): Int = gitUserList.size
}