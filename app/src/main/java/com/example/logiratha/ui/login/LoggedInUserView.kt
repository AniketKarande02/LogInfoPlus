package com.example.logiratha.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val mobileNo: String,
    val password: String
    //... other data fields that may be accessible to the UI
)