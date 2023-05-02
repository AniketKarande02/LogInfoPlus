package com.example.logiratha.repository

import androidx.lifecycle.LiveData
import com.example.logiratha.data.UserDao
import com.example.logiratha.model.UserInfo

// User Repository abstracts access to multiple data sources. However this is not the part of the Architecture Component libraries.

class UserRepository(private val userDao: UserDao) {
    val readAllUserInfoData: LiveData<List<UserInfo>> = userDao.readAllUserInfoData()

    suspend fun addUserInfo(userInfo: UserInfo) {
        userDao.addUserInfo(userInfo)
    }

    suspend fun updateUserInfo(userInfo: UserInfo) {
       userDao.updateUserInfo(userInfo.userName,userInfo.userEmail,userInfo.userPassword,userInfo.userMobileNo)
    }
    suspend fun updateIsRegisteredFlag(isRegistered: Boolean, mobileNo: String) {
       userDao.updateIsRegisteredFlag(isRegistered, mobileNo )
    }

    suspend fun checkMobileNumber( username: String): UserInfo {
      return userDao.checkMobileNumber(username)

    }
}