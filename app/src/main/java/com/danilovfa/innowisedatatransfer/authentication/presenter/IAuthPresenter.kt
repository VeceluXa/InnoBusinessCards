package com.danilovfa.innowisedatatransfer.authentication.presenter

import android.app.Activity

interface IAuthPresenter {
//    fun doLogin(activity: Activity, email: String, password: String): Boolean
    fun isPasswordValid(password: String): Boolean
    fun isEmailValid(email: String): Boolean
}