package com.example.githubappsub2.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.githubappsub2.ui.detail.DetailUser
import com.example.githubappsub2.ui.main.MainActivity
import com.example.githubappsub2.utils.DiffCallback

class FavoriteAdapter : ListAdapter<UserEntity, FavoriteAdapter.FavoriteViewHolder>(
    DIFF_CALLBACK) {

    private val listFavorite = ArrayList<UserEntity>()
    private val listFavorite2 = ArrayList<GitDetailResponse>()

    private lateinit var onItemClickCallBack: OnItemClickCallback


    fun setFavorite(favorites : List<UserEntity>){
        val diffCallback = DiffCallback(this.listFavorite, favorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(favorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemRowSearchuserBinding = ItemRowSearchuserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(itemRowSearchuserBinding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorites = listFavorite[position]
        holder.bind(favorites)
    }

    class FavoriteViewHolder (val binding : ItemRowSearchuserBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(favoriteUser: UserEntity){
                with(binding){
                    tvItemName.text = favoriteUser.login
                    itemView.setOnClickListener{
                        val intent = Intent(itemView.context, detailFavorite::class.java)
                        intent.putExtra(DetailUser.EXTRA_FAVORITE, favoriteUser.login)
                        itemView.context.startActivity(intent)
                        println(favoriteUser.login)
                    }
                }
                Glide.with(itemView.context)
                    .load(favoriteUser.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .circleCrop()
                    .into(binding.imgItemPhoto)
            }
        }

    override fun getItemCount(): Int = listFavorite.size



    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
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