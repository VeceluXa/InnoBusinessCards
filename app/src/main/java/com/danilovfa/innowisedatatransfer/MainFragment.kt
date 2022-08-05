package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.danilovfa.innowisedatatransfer.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser

        // If user is not signed in call Authentication activity
        if (currentUser == null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_authLoginFragment)
        }

        val bottomNavigationView = binding.bottomNavigation
        val navController = getNavController()

        bottomNavigationView.setupWithNavController(navController)

        return view
    }

    // workaround for https://issuetracker.google.com/issues/142847973
    private fun getNavController(): NavController {
        val fragment: Fragment? =
            childFragmentManager.findFragmentById(R.id.fragmentContainerViewMain)
        check(fragment is NavHostFragment) {
            ("Fragment " + this
                    + " does not have a NavHostFragment")
        }
        return fragment.navController
    }
}