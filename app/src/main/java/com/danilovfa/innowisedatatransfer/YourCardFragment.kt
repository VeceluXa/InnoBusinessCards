package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.databinding.FragmentYourCardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class YourCardFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentYourCardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourCardBinding.inflate(inflater, container, false)
        val view = binding.root

        showNavBar()

        // Set title of tool bar
        (requireActivity() as MainActivity).title = "Your card"

        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser

        // If user is not signed in call Authentication activity
        if (currentUser == null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_yourCardFragment_to_auth_graph)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_your_card_main, menu)
    }

    // TODO Implement edit and share buttons
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_your_card_edit -> {
                true
            }
            R.id.menu_your_card_share -> {
                true
            }
            else -> false
        }
    }

    // Show Bottom Navigation
    private fun showNavBar() {
        val activity = requireActivity() as MainActivity
        val bottomNavigation = activity.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE
    }
}
