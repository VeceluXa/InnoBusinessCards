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
import com.danilovfa.innowisedatatransfer.R
import com.danilovfa.innowisedatatransfer.authentication.presenter.AuthPresenter
import com.danilovfa.innowisedatatransfer.authentication.presenter.IAuthPresenter
import com.danilovfa.innowisedatatransfer.databinding.FragmentAuthRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthRegistrationFragment : Fragment(), IAuthView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAuthRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authPresenter: IAuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        authPresenter = AuthPresenter(this)
        auth = Firebase.auth

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
                                Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                } else {
                    if (!authPresenter.isEmailValid(email))
                        Toast.makeText(activity, "Wrong email format",
                            Toast.LENGTH_SHORT).show()
                    else if (!authPresenter.isPasswordValid(password))
                        Toast.makeText(activity, "Wrong password format",
                            Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(activity, "Confirmed password is not the same", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthRegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthRegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}