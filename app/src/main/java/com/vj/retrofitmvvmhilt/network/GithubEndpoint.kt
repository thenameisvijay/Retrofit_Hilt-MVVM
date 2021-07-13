package com.vj.retrofitmvvmhilt.network

import com.vj.retrofitmvvmhilt.model.RepoResponse
import retrofit2.http.GET

interface GithubEndpoint {

    @GET("users")
    suspend fun requestUserData(): ArrayList<RepoResponse>

}