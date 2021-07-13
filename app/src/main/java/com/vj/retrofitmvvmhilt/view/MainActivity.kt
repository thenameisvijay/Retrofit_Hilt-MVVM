package com.vj.retrofitmvvmhilt.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vj.retrofitmvvmhilt.databinding.ActivityMainBinding
import com.vj.retrofitmvvmhilt.helper.NetworkHelper
import com.vj.retrofitmvvmhilt.model.RepoResponse
import com.vj.retrofitmvvmhilt.network.Status
import com.vj.retrofitmvvmhilt.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Once Hilt is set up in your Application class and an application-level component is available,
 * Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation
 *
 * If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes that depend on it.
 * For example, if you annotate a fragment, then you must also annotate any activities where you use that fragment.
 *
 * Note: Hilt only supports activities that extend ComponentActivity, such as AppCompatActivity.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val repoViewModel: RepoViewModel by viewModels()
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mProgress: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        mRecyclerView = activityMainBinding.repoList
        mProgress = activityMainBinding.progress
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = RepoAdapter()
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                mRecyclerView.context,
                (mRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        mRecyclerView.adapter = mAdapter
    }

    private fun setupObservers() {
        if (NetworkHelper.hasInternet(this)) {
            repoViewModel.getRepoList().observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            mRecyclerView.visibility = View.VISIBLE
                            mProgress.visibility = View.GONE
                            resource.data?.let { gitUsers ->
                                setToAdapter(gitUsers)
                            }
                        }
                        Status.ERROR -> {
                            mRecyclerView.visibility = View.VISIBLE
                            mProgress.visibility = View.GONE
                            Log.e("ERROR", "error msg: " + it.message)
                        }
                        Status.LOADING -> {
                            mProgress.visibility = View.VISIBLE
                            mRecyclerView.visibility = View.GONE
                        }
                    }
                }
            })
        } else {
            mProgress.visibility = View.GONE
            mRecyclerView.visibility = View.GONE
            activityMainBinding.errorMesg.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setToAdapter(gitUsers: ArrayList<RepoResponse>) {
        mAdapter.apply {
            addGitUsers(gitUsers)
            notifyDataSetChanged()
        }
    }
}