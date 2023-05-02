package com.example.logiratha.ui.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.logiratha.databinding.ActivityRegistrationBinding
import com.example.logiratha.model.UserInfo
import com.example.logiratha.ui.home.HomeActivity
import com.example.logiratha.utilities.Validation
import com.example.logiratha.utilities.Validation.isValidEmail

class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.edtUsername
        val useremail = binding.edtEmail
        val password = binding.edtPassword
        val mobileNo=  intent.getStringExtra("mobileNo")
        binding.txtUserMobileNumber.text= mobileNo
        val login = binding.login
        val loading = binding.loading

        registrationViewModel = ViewModelProvider(this, RegistrationViewModelFactory(this))
            .get(RegistrationViewModel::class.java)

        registrationViewModel.readAllData.observe(this, Observer { user: List<UserInfo> ->
            Log.d("DATA",user.size.toString())
            loading.visibility = View.GONE
        })


        registrationViewModel.registrationResult.observe(this@RegistrationActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {

            }
            if (loginResult.success != null) {
                startActivity(Intent(this@RegistrationActivity, HomeActivity::class.java))
                val sharedPreference =  getSharedPreferences("logiratha_pref", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putBoolean("isloggedIn",true)
                editor.commit()
                finish()

                Log.d("SUCCESS..","Navigate to home screen")
            }
            setResult(Activity.RESULT_OK)
        })

        login.setOnClickListener {
            if (!Validation.isPasswordValid(password.text.toString())) {
                password.error =
                    "Please check Password- Password should contain Capital letter, small letter, digit and special characters"
            } else if (username.text.toString().isEmpty()) {
                username.error = "Please enter username"
            } else if (!isValidEmail(useremail.text.toString())) {
                useremail.error= "Please enter correct Email"
            } else {
                loading.visibility = View.VISIBLE
                var userInfo = UserInfo(
                    0,
                    mobileNo.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    useremail.text.toString(), true
                )
                registrationViewModel.updateUserInfo(userInfo)
                Log.d("LOGINActivity", "Record Inserted Successfully!")

                registrationViewModel.getUserInfoDetails(mobileNo.toString())
            }
        }
    }
}