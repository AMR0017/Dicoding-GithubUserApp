package com.example.githubappsub2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubappsub2.ui.view.DetailView
import com.example.githubappsub2.R
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.ui.adapter.SectionPagerAdapter
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.ActivityDetailUserBinding
import com.example.githubappsub2.ui.favorite.FavoriteActivity
import com.example.githubappsub2.ui.favorite.FavoriteViewModel
import com.example.githubappsub2.ui.main.MainActivity
import com.example.githubappsub2.ui.view.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.log

class DetailUser : AppCompatActivity() {
    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailView
    private lateinit var viewModel2: FavoriteViewModel

    private var ivFavorite: Boolean = false
    private var favoriteUser: UserEntity? = null
    private var detailUser = GitDetailResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

       viewModel = obtainViewModel(this@DetailUser)
        val user = intent.getParcelableExtra<GitDetailResponse>(MainActivity.EXTRA_DATA)

        if (user != null) {
            user.login?.let { viewModel.getDetailUser(it) }
        }

        viewModel.listUser.observe(this){
            detailList ->
            detailUser = detailList

            if (detailList != null) {
                binding?.let {
                    Glide.with(this)
                        .load(detailList.avatarUrl)
                        .circleCrop()
                        .into(it.imgDetailPhoto)
                }
            }
            binding?.apply {
                tvDetailName.text = detailList.name
                tvDetailUsername.text = detailList.login
                if (detailList.company != null){
                    tvDetailCompany.text = detailList.company
                } else {
                    tvDetailCompany.text = "-"
                }
                if (detailList.location != null){
                    tvDetailLocation.text = detailList.location
                } else{
                    tvDetailLocation.text = "-"
                }
                tvDetailFollowers.text = detailList.followers.toString()
                tvDetailFollowing.text = detailList.following.toString()
                tvDetailRepository.text = detailList.publicRepos.toString()
            }

            favoriteUser = UserEntity(detailList.id, detailList.login, detailList.avatarUrl)
            viewModel.getFavorite().observe(this){ userFavorite ->
                if (userFavorite != null){
                    for (data in userFavorite){
                        if (detailList.id == data.id){
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


            val sectionsPagerAdapter = SectionPagerAdapter(this)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
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
        const val EXTRA_FAVORITE = "extra_favorite"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}