package com.danilovfa.innowisedatatransfer.authentication.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.R
import com.danilovfa.innowisedatatransfer.authentication.presenter.IAuthPresenter
import com.danilovfa.innowisedatatransfer.authentication.presenter.AuthPresenter
import com.danilovfa.innowisedatatransfer.databinding.FragmentAuthLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthLoginFragment : Fragment(), IAuthView {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAuthLoginBinding
    private lateinit var authPresenter: IAuthPresenter

    private lateinit var auth: FirebaseAuth

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
        binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        authPresenter = AuthPresenter(this)

        // Open registration fragment if register button is clicked
        val navController = NavHostFragment.findNavController(this)
        binding.buttonRegister.setOnClickListener {
            navController.navigate(R.id.action_authLoginFragment_to_authRegistrationFragment)
        }

        // Open main fragment
        binding.buttonLogin.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (authPresenter.isEmailValid(email) && authPresenter.isPasswordValid(password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            navController.popBackStack()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Incorrect email or password",
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



//            if (loginPresenter.doLogin(requireActivity(), email, password)) {
//                // Return to main fragment
//                navController.popBackStack()
//            } else {
//                if (!loginPresenter.isEmailValid(email))
//                    Toast.makeText(activity, "Wrong email format",
//                        Toast.LENGTH_SHORT).show()
//                else if (!loginPresenter.isPasswordValid(password))
//                    Toast.makeText(activity, "Wrong password format",
//                        Toast.LENGTH_SHORT).show()
//                else
//                    Toast.makeText(activity, "Incorrect email or password",
//                        Toast.LENGTH_SHORT).show()
//            }
        }

        // Inflate the layout for this fragment
        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}