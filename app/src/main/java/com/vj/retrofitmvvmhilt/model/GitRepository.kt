package com.vj.retrofitmvvmhilt.model

import com.vj.retrofitmvvmhilt.network.GithubEndpoint
import javax.inject.Inject

class GitRepository @Inject constructor(private val endpoint: GithubEndpoint) {
    suspend fun callUserData() = endpoint.requestUserData()
}