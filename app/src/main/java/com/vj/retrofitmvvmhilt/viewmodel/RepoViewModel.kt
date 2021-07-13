package com.vj.retrofitmvvmhilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vj.retrofitmvvmhilt.model.GitRepository
import com.vj.retrofitmvvmhilt.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(private val gitRepository: GitRepository): ViewModel() {

    fun getRepoList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = gitRepository.callUserData()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}