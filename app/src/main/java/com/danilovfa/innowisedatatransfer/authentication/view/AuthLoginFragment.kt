package com.danilovfa.innowisedatatransfer.authentication.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.MainActivity
import com.danilovfa.innowisedatatransfer.R
import com.danilovfa.innowisedatatransfer.authentication.presenter.IAuthPresenter
import com.danilovfa.innowisedatatransfer.authentication.presenter.AuthPresenter
import com.danilovfa.innowisedatatransfer.databinding.FragmentAuthLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthLoginFragment : Fragment(), IAuthView {

    private lateinit var binding: FragmentAuthLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authPresenter: IAuthPresenter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set title of tool bar
        (requireActivity() as MainActivity).title = "Login"

        hideNavBar()

        auth = Firebase.auth
        authPresenter = AuthPresenter(this)

        // Open registration fragment if register button is clicked
        val navController = NavHostFragment.findNavController(this)
        buttonRegisterListener(navController)

        // Open main fragment
        buttonLoginListener(navController)

        // Inflate the layout for this fragment
        return view
    }

    /**
     * This functions performs login when button "Login" is pressed
     * It checks if email and password are in right format, then
     * performs Firebase Authentication SignIn. If login is successful,
     * authentication fragment is closed.
     */
    private fun buttonLoginListener(navController: NavController) {
        binding.buttonLogin.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (authPresenter.isEmailValid(email) && authPresenter.isPasswordValid(password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            navController.navigate(R.id.action_authLoginFragment_to_yourCardFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                activity, "Incorrect email or password",
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
        }
    }

    /**
     * This function opens registration fragment when "Registration" button is pressed
     */
    private fun buttonRegisterListener(navController: NavController) {
        binding.buttonRegister.setOnClickListener {
            navController.navigate(R.id.action_authLoginFragment_to_authRegistrationFragment)
        }
    }

    private fun hideNavBar() {
        val activity = requireActivity() as MainActivity
        val bottomNavigation = activity.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }
}