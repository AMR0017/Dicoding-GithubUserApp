package com.example.githubappsub2.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubappsub2.data.remote.response.GitDetailResponse
import com.example.githubappsub2.databinding.FragmentFollowBinding
import com.example.githubappsub2.ui.follow.followAdapter
import com.example.githubappsub2.ui.main.MainActivity
import com.example.githubappsub2.ui.view.DetailView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]

        val gitUser : GitDetailResponse = requireActivity().intent.getParcelableExtra(MainActivity.EXTRA_DATA)!!
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val git_tabTitle = arguments?.getString(TAB_TITLES)
        if (git_tabTitle == GIT_FOLLOWERS){
            gitUser.login?.let { viewModel.getFollowersList(it) }
        }else if (git_tabTitle == GIT_FOLLOWING){
            gitUser.login?.let { viewModel.getFollowingList(it) }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.listFollow.observe(viewLifecycleOwner){
            val adapter = followAdapter(it)
            binding.apply {
                rvFollow.adapter = adapter
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }

        return binding.root
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }
    companion object {
        const val TAB_TITLES = "tab_titles"
        const val GIT_FOLLOWERS = "Followers"
        const val GIT_FOLLOWING = "Following"
    }
}