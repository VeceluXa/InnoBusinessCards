package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.CardViewBindingAdapter
import com.danilovfa.innowisedatatransfer.databinding.FragmentCardViewBinding
import com.danilovfa.innowisedatatransfer.databinding.FragmentMoreBinding

class CardView : Fragment() {
    private lateinit var binding: FragmentCardViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).title = "Your card"

        binding = FragmentCardViewBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inflate the layout for this fragment
        return view
    }
}