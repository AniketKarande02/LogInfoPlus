package com.example.logiratha.ui.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.logiratha.R
import com.example.logiratha.ui.home.HomeActivity
import com.example.logiratha.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers .Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()


        activityScope.launch {
            delay(3000)
            val sharedPreference =  getSharedPreferences("logiratha_pref", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreference.getBoolean("isloggedIn",false)
            if(isLoggedIn){
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            }else {
                var intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}