package com.example.githubappsub2.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubappsub2.R
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.data.remote.response.favoriteResponse
import com.example.githubappsub2.databinding.ActivityDetailUserBinding
import com.example.githubappsub2.ui.adapter.SectionPagerAdapter
import com.example.githubappsub2.ui.detail.DetailUser
import com.example.githubappsub2.ui.detail.DetailViewModelFactory
import com.example.githubappsub2.ui.main.MainActivity
import com.example.githubappsub2.ui.view.DetailView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class detailFavorite : AppCompatActivity(){
    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailView
    private lateinit var viewModel2: FavoriteViewModel
    private var detailUser = GitDetailResponse()

    private var ivFavorite: Boolean = false
    private var favoriteUser: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@detailFavorite)
        val user = intent.getStringExtra(DetailUser.EXTRA_FAVORITE)

        if (user != null){
            viewModel.getDetailUser(user)
        }

        viewModel.listUser.observe(this){
                detailList ->
            detailUser = detailList

            favoriteUser = UserEntity(detailUser.id, detailUser.login, detailUser.avatarUrl)
            binding?.let {
                Glide.with(this)
                    .load(detailUser.avatarUrl)
                    .circleCrop()
                    .into(it.imgDetailPhoto)
            }
            binding?.apply {
                tvDetailName.text = detailUser.name
                tvDetailUsername.text = detailUser.login
                if (detailUser.company != null){
                    tvDetailCompany.text = detailUser.company
                } else {
                    tvDetailCompany.text = "-"
                }
                if (detailUser.location != null){
                    tvDetailLocation.text = detailUser.location
                } else{
                    tvDetailLocation.text = "-"
                }
                tvDetailFollowers.text = detailUser.followers.toString()
                tvDetailFollowing.text = detailUser.following.toString()
                tvDetailRepository.text = detailUser.publicRepos.toString()
            }


            viewModel.getFavorite().observe(this){ userFavorite ->
                if (userFavorite != null){
                    for (data in userFavorite){
                        if (detailUser.id == data.id){
                            ivFavorite = true
                            binding?.ivfavorite?.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }
                    }
                }
            }

            binding?.ivfavorite?.setOnClickListener {
                if (!ivFavorite){
                    ivFavorite = true
                    binding!!.ivfavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    insertToDatabase(detailUser)
                }else{
                    ivFavorite = false
                    binding!!.ivfavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "Delete Favorite", Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.loading.observe(this){
            showLoading(it)
        }

        viewModel.error.observe(this){
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }

    }

    private fun insertToDatabase(detailList: GitDetailResponse){
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id
            favoriteUser?.login = detailList.login
            favoriteUser?.imageUrl = detailList.avatarUrl
            viewModel.insert(favoriteUser as UserEntity)
            Toast.makeText(this, "Favorited", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailView{
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailView::class.java]
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding?.progressBar3?.visibility = View.VISIBLE
        } else {
            binding?.progressBar3?.visibility = View.GONE
        }
    }
    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

}