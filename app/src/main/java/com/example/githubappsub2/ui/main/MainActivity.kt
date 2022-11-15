package com.example.githubappsub2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubappsub2.R
import com.example.githubappsub2.data.local.entity.UserEntity
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.ActivityMainBinding
import com.example.githubappsub2.ui.favorite.FavoriteActivity
import com.example.githubappsub2.ui.adapter.SearchAdapter
import com.example.githubappsub2.ui.detail.DetailUser
import com.example.githubappsub2.ui.theme.SettingPreference
import com.example.githubappsub2.ui.theme.ThemeSetting
import com.example.githubappsub2.ui.view.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        viewModel.listUser.observe(this){ githubUserList ->
            showRecyclerList(githubUserList)
        }
        viewModel.loading.observe(this){
            showLoading(it)
        }
        viewModel.error.observe(this){
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }

        rvList = binding.recyclerView1
        rvList.setHasFixedSize(true)


        val pref = SettingPreference.getInstance(dataStore)

        val themeSettingView = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ThemeSettingView::class.java
        )

        themeSettingView.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showRecyclerList(gitUserList: List<GitDetailResponse> ) {
        rvList.layoutManager = LinearLayoutManager(this)
        val searchUserAdapter = SearchAdapter(gitUserList)
        rvList.adapter = searchUserAdapter

        searchUserAdapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: GitDetailResponse) {
                val intentDetail = Intent(this@MainActivity, DetailUser::class.java)
                intentDetail.putExtra(EXTRA_DATA, data)
                startActivity(intentDetail)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem : MenuItem = menu!!.findItem(R.id.search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
               viewModel.detailUser()
                return true
            }

        })

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUser(query.toString())
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.themeSetting -> {
                Intent(this@MainActivity, ThemeSetting::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }


    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}