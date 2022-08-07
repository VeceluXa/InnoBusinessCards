package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.danilovfa.innowisedatatransfer.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         // Set title of tool bar
        (requireActivity() as MainActivity).title = "More"

        binding = FragmentMoreBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            NavHostFragment.findNavController(this).navigate(R.id.action_moreFragment_to_auth_graph)
        }

        // Inflate the layout for this fragment
        return view
    }
}