package com.example.logiratha.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope

import com.example.logiratha.R
import com.example.logiratha.data.UserDatabase
import com.example.logiratha.model.UserInfo
import com.example.logiratha.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(private val application: Context) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    val readAllData: LiveData<List<UserInfo>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository= UserRepository(userDao)
        readAllData = repository.readAllUserInfoData
    }

    fun verifyMobileNumber(mobileNo: String){
        viewModelScope.async {
            val result = repository.checkMobileNumber(mobileNo)
            if (result != null) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(mobileNo = result.userMobileNo, password = result.userPassword))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }
    fun addMobileNumber(mobileNo: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserInfo(UserInfo(0,mobileNo,"","","",false))
        }

    }

    fun updateIsRegisteredFlag(isRegistered:Boolean, mobileNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIsRegisteredFlag(isRegistered,mobileNo)
        }
    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }



}