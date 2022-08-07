package com.danilovfa.innowisedatatransfer.authentication.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.MainActivity
import com.danilovfa.innowisedatatransfer.R
import com.danilovfa.innowisedatatransfer.authentication.presenter.AuthPresenter
import com.danilovfa.innowisedatatransfer.authentication.presenter.IAuthPresenter
import com.danilovfa.innowisedatatransfer.databinding.FragmentAuthRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthRegistrationFragment : Fragment(), IAuthView {

    private lateinit var binding: FragmentAuthRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authPresenter: IAuthPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set title of tool bar
        (requireActivity() as MainActivity).title = "Register"

        binding = FragmentAuthRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        authPresenter = AuthPresenter(this)
        auth = Firebase.auth

        buttonRegisterListener()

        return view
    }

    /**
     * This function performs registration when button "Register" is pressed.
     * It checks if email and password are in right format and then performs
     * Firebase Authentication SignUp. If operation is successful, fragment is closed.
     */
    private fun buttonRegisterListener() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.editEmail.text.toString()

            // If password and confirm password are equal
            if (binding.editPassword.text.toString() == binding.editPasswordConfirm.text.toString()) {
                val password = binding.editPassword.text.toString()

                // If email and password are formatted correctly perform sign up
                if (authPresenter.isEmailValid(email) && authPresenter.isPasswordValid(password)) {

                    // Perform sign up
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")

                                // Close sign up fragment
                                NavHostFragment.findNavController(this).popBackStack()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                } else {
                    if (!authPresenter.isEmailValid(email))
                        Toast.makeText(
                            activity, "Wrong email format",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (!authPresenter.isPasswordValid(password))
                        Toast.makeText(
                            activity, "Wrong password format",
                            Toast.LENGTH_SHORT
                        ).show()
                }

            } else {
                Toast.makeText(activity, "Confirmed password is not the same", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}