package com.harsh.walkie_talkie.ui.presentation.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.harsh.walkie_talkie.db.Watabase
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = Watabase.getDatabase(application).userDao()
    val users: LiveData<List<User>> = userDao.getAllUsers().asLiveData()

    fun addUser(uid: String, name: String, token: String) {
        viewModelScope.launch {
            userDao.insertUser(User(uid = uid, name = name, token = token))
        }
    }

    fun findUser(uid: String): Flow<User?> {
        return userDao.findUserById(uid)
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }
}
