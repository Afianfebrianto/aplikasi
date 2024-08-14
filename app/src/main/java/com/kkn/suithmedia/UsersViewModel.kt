package com.kkn.suithmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkn.suithmedia.model.RetrofitClient
import com.kkn.suithmedia.model.User
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> get() = _isEmpty

    private var page = 1
    private var isLastPage = false

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        if (isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.getUsers(page, 10)
                val fetchedUsers = response.data
                if (page == 1) {
                    _isEmpty.value = fetchedUsers.isEmpty()
                }
                _users.value = if (page == 1) fetchedUsers else _users.value.orEmpty() + fetchedUsers
                isLastPage = fetchedUsers.isEmpty()
                page++
            } catch (e: Exception) {
                _isEmpty.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        page = 1
        isLastPage = false
        fetchUsers()
    }
}