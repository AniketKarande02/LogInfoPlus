package com.example.logiratha.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.logiratha.model.UserInfo

// UserDao contains the methods used for accessing the database, including queries.
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // <- Annotate the 'addUser' function below. Set the onConflict strategy to IGNORE so if exactly the same user exists, it will just ignore it.
    suspend fun addUserInfo(user: UserInfo)

    @Query("UPDATE userinfo SET userName=:username, userEmail=:email, userPassword=:password WHERE userMobileNo =:mobileNo")
    suspend fun updateUserInfo(username:String, email:String, password:String,mobileNo:String)

    @Query("UPDATE userinfo SET isRegistered=:isRegistered WHERE userMobileNo =:mobileNo")
    suspend fun updateIsRegisteredFlag(isRegistered: Boolean, mobileNo:String)

    @Query("SELECT * from userinfo ORDER BY userid ASC") // <- Add a query to fetch all users (in user_table) in ascending order by their IDs.
    fun readAllUserInfoData(): LiveData<List<UserInfo>> // <- This means function return type is List. Specifically, a List of Users.

    @Query("Select * FROM userinfo WHERE userMobileNo=:mobileNo LIMIT 1")
    suspend fun checkMobileNumber(mobileNo:String):UserInfo

}