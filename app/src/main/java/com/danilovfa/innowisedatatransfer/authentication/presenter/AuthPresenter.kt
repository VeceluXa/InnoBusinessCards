package com.danilovfa.innowisedatatransfer.authentication.presenter

import com.danilovfa.innowisedatatransfer.authentication.model.IUser
import com.danilovfa.innowisedatatransfer.authentication.model.UserModel
import com.danilovfa.innowisedatatransfer.authentication.view.IAuthView

class AuthPresenter: IAuthPresenter {


    var user: IUser
    var iAuthView: IAuthView

    constructor(iAuthView: IAuthView) {
        this.iAuthView = iAuthView
        user = UserModel()
    }


//    override fun doLogin(activity: Activity, email: String, password: String): Boolean {
//
//        var isLoggedIn = false
//
//        if (isEmailValid(email) && isPasswordValid(password)) {
//            user.email = email
//            user.password = password
//
//            isLoggedIn = user.doLogIn(activity)
//        }
//
//        return isLoggedIn
//    }


    override fun isPasswordValid(password: String): Boolean {
        return user.isPasswordValid(password)
    }


    override fun isEmailValid(email: String): Boolean {
        return user.isEmailValid(email)
    }


}