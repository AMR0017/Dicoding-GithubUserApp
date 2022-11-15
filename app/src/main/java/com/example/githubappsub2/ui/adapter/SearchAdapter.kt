package com.example.githubappsub2.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.ItemRowSearchuserBinding
import com.example.githubappsub2.utils.DiffCallback

class SearchAdapter(private val gitUserList: List<GitDetailResponse>) : ListAdapter<UserEntity, SearchAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    private lateinit var onItemClickCallBack: OnItemClickCallback
    private val listFavorite = ArrayList<UserEntity>()

    class ViewHolder(val binding: ItemRowSearchuserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowSearchuserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitUser = gitUserList[position]

        Glide.with(holder.itemView.context)
            .load(gitUser.avatarUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(holder.binding.imgItemPhoto)
        holder.apply {
            binding.apply {
                tvItemName.text = gitUser.login
                itemView.setOnClickListener{
                    onItemClickCallBack.onItemClicked(gitUserList[holder.adapterPosition])
                }
            }
        }
    }

    private fun onBookmarkClick(users: UserEntity) {
        val onBookmarkClick: ((UserEntity) -> Unit)
    }

    fun setFavorite(favorites : List<UserEntity>){
        val diffCallback = DiffCallback(this.listFavorite, favorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(favorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = gitUserList.size

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GitDetailResponse)
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}