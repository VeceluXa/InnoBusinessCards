package com.danilovfa.innowisedatatransfer.authentication.model

import android.app.Activity

interface IUser {
    var email: String
    var password: String
//    fun doLogIn(activity: Activity): Boolean
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}