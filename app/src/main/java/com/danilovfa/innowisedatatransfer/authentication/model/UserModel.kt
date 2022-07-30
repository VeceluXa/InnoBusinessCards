package com.danilovfa.innowisedatatransfer.authentication.model

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserModel: IUser {

    private val auth = Firebase.auth

    override lateinit var email: String
    override lateinit var password: String


    /*
    Moved authentication to fragment according to
    https://stackoverflow.com/questions/73171454/firebase-authentication-oncompletelistener-not-working-when-passing-fragment-act
    Perform Log In
    If log in is successful, return true
    Else, return false
    override fun doLogIn(activity: Activity): Boolean {
    var isLoggedIn = false

    // Log in
    auth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(activity) { task ->
    if (task.isSuccessful) {
    // Sign in success, update UI with the signed-in user's information
    Log.d(TAG, "signInWithEmail:success")
    isLoggedIn = true
    } else {
    // If sign in fails, display a message to the user.
    Log.w(TAG, "signInWithEmail:failure", task.exception)
    }
    }

    return isLoggedIn
    }
    */

    // Checks if email is valid
    override fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Password requires at least 8 characters
    // Including one letter and one number
    // TODO Change regex to 8-32 characters one letter, one number, one CAPS letter
    override fun isPasswordValid(password: String): Boolean {
        return Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matches(password)
    }


}