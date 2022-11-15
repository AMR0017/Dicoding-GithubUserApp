package com.example.githubappsub2.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.ActivityFavoriteBinding
import com.example.githubappsub2.ui.detail.DetailViewModelFactory
import com.example.githubappsub2.ui.view.DetailView

class FavoriteActivity : AppCompatActivity() {
    private var _binding : ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter


    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getFavorite().observe(this) { githubUserList ->
            if (githubUserList != null) {
                adapter.setFavorite(githubUserList)

            }
        }
        adapter = FavoriteAdapter()
        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.setHasFixedSize(false)
        binding?.rvFavorite?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object{
        const val EXTRA_DATA = "extra_data"
    }

}