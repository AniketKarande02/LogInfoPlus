package com.example.logiratha.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.logiratha.databinding.ActivityLoginBinding

import com.example.logiratha.model.UserInfo
import com.example.logiratha.ui.home.HomeActivity
import com.example.logiratha.ui.registration.RegistrationActivity
import com.example.logiratha.utilities.Validation.isPasswordValid

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var dbMobileNumber:String
    lateinit var dbPassword:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mobileNo = binding.edtMobileNumber
        val password = binding.password
        val login = binding.login
        val loading = binding.loading


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(this))
            .get(LoginViewModel::class.java)

        loginViewModel.readAllData.observe(this, Observer { user: List<UserInfo> ->
            Log.d("DATA",user.toString())
        })
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                if (mobileNo != null) {
                    mobileNo.error = getString(loginState.usernameError)
                }
            }

            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult.error != null) {
                Toast.makeText(this@LoginActivity,"Mobile Number Not Exist",Toast.LENGTH_LONG).show()
                loginViewModel.addMobileNumber(mobileNo?.text.toString())
                startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java)
                    .putExtra("mobileNo",mobileNo?.text.toString()))
            }
            if (loginResult.success != null) {
                dbMobileNumber = loginResult.success.mobileNo
                dbPassword = loginResult.success.password
                password.visibility = View.VISIBLE
            }
            setResult(Activity.RESULT_OK)
        })


            login.setOnClickListener {
                if(!isMobileNumberValid(mobileNo.text.toString())){
                    mobileNo.error="Please Enter Correct Mobile Number"
                }else if(password.visibility == View.VISIBLE){
                    if(isPasswordValid(password.text.toString())) {
                        if (password.text.toString() == dbPassword) {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            val sharedPreference =
                                getSharedPreferences("logiratha_pref", Context.MODE_PRIVATE)
                            var editor = sharedPreference.edit()
                            editor.putBoolean("isloggedIn", true)
                            editor.commit()
                            loginViewModel.updateIsRegisteredFlag(true, dbMobileNumber)
                            finish()
                        } else {
                            password.error = "Please enter Valid password"
                        }
                    }else{
                        password.error = "Please enter Valid password"
                    }
                }else{
                    loading.visibility = View.VISIBLE
                    loginViewModel.verifyMobileNumber(mobileNo?.text.toString())
                }
            }
        }

    private fun isMobileNumberValid(mobileNumber: String): Boolean {
        val mobilePattern = "^[+]?[0-9]{10,13}$".toRegex()

        return mobilePattern.matches(mobileNumber)
    }

    }
