package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.databinding.FragmentYourCardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class YourCardFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentYourCardBinding
    private lateinit var auth: FirebaseAuth

    private var isView = true

    // Menu
    private lateinit var menu: Menu
    private lateinit var menuInflater: MenuInflater

    // Nested nav controller
    private lateinit var cardNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourCardBinding.inflate(inflater, container, false)
        val view = binding.root

        // Save nested fragment state
        if (savedInstanceState != null)
            isView = savedInstanceState.getBoolean("IsView")

        showNavBar()

        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser

        // If user is not signed in call Authentication activity
        if (currentUser == null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_yourCardFragment_to_auth_graph)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Get nested nav controller
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.cardContainer) as NavHostFragment
        cardNavController = nestedNavHostFragment.navController

        // Inflate the layout for this fragment
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("IsView", isView)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        this.menu = menu
        this.menuInflater = menuInflater
        if (isView)
            menuInflater.inflate(R.menu.toolbar_your_card_view, menu)
        else
            menuInflater.inflate(R.menu.toolbar_your_card_edit, menu)
    }

    // TODO Implement edit and share buttons
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_your_card_edit -> {
                viewToEdit()
                true
            }
            R.id.menu_your_card_share -> {
                true
            }
            R.id.menu_your_card_save -> {
                editToView()
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

    private fun viewToEdit() {
        isView = false
        menu.clear()
        menuInflater.inflate(R.menu.toolbar_your_card_edit, menu)
        cardNavController.navigate(R.id.action_cardView_to_cardEdit)
    }

    private fun editToView() {
        isView = true
        menu.clear()
        menuInflater.inflate(R.menu.toolbar_your_card_view, menu)
        cardNavController.navigate(R.id.action_cardEdit_to_cardView)
    }
}
