package com.example.logiratha.ui.registration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.logiratha.R
import com.example.logiratha.data.UserDatabase
import com.example.logiratha.model.UserInfo
import com.example.logiratha.repository.UserRepository
import com.example.logiratha.ui.login.LoggedInUserView
import com.example.logiratha.ui.login.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegistrationViewModel(private val application: Context) : ViewModel() {

    val readAllData: LiveData<List<UserInfo>>
    private val repository: UserRepository
    private val _registrationResult = MutableLiveData<LoginResult>()
    val registrationResult: LiveData<LoginResult> = _registrationResult

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository= UserRepository(userDao)
        readAllData = repository.readAllUserInfoData
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun addUserInfo(userinfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
           repository.addUserInfo(userinfo)
        }
    }


    fun updateUserInfo(userinfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
           repository.updateUserInfo(userinfo)
        }
    }


    fun getUserInfoDetails(mobileNo: String){
        viewModelScope.async {
            val result = repository.checkMobileNumber(mobileNo)
            if (result != null) {
                _registrationResult.value =
                    LoginResult(success = LoggedInUserView(mobileNo = result.userMobileNo, password = result.userPassword))
            } else {
                _registrationResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }
    fun getLoginDetails(context: Context, username: String) : LiveData<List<UserInfo>>? {
       // liveDataLogin = repository.readAllData;// LoginRepository.getLoginDetails(context, username)
        return null
    }
}