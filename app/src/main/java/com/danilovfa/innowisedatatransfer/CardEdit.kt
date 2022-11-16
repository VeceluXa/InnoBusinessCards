package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danilovfa.innowisedatatransfer.databinding.FragmentCardEditBinding

class CardEdit : Fragment() {
    private lateinit var binding: FragmentCardEditBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).title = "Edit your card"

        binding = FragmentCardEditBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inflate the layout for this fragment
        return view
    }
}