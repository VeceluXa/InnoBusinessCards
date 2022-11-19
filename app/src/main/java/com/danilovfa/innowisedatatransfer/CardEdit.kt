package com.danilovfa.innowisedatatransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.core.view.size
import com.danilovfa.innowisedatatransfer.databinding.CardItemEditBinding
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

        addContact()
        onAddContact()
        addSocial()
        onAddSocial()

        // Inflate the layout for this fragment
        return view
    }

    private fun onAddContact() {
        binding.buttonAddContact.setOnClickListener {
            addContact()
        }
    }

    private fun addContact() {
        val entries = listOf("Address", "Phone", "Email")

        val view = layoutInflater.inflate(R.layout.card_item_edit, null, false)
        val rowBinding = CardItemEditBinding.bind(view)

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, entries)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rowBinding.spinner.adapter = arrayAdapter

        rowBinding.buttonDelete.setOnClickListener {
            removeContact(view)
        }

        if (binding.contactsContainer.size == 2) {
            binding.buttonAddContact.visibility = View.GONE
        }

        binding.contactsContainer.addView(view)
    }

    private fun removeContact(view: View) {
        binding.contactsContainer.removeView(view)
        binding.buttonAddContact.visibility = View.VISIBLE
    }

    private fun onAddSocial() {
        binding.buttonAddSocial.setOnClickListener {
            addSocial()
        }
    }

    private fun addSocial() {
        val entries = listOf("Twitter", "GitHub", "LinkedIn", "Reddit")

        val view = layoutInflater.inflate(R.layout.card_item_edit, null, false)
        val rowBinding = CardItemEditBinding.bind(view)

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, entries)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rowBinding.spinner.adapter = arrayAdapter

        rowBinding.buttonDelete.setOnClickListener {
            removeSocial(view)
        }

        if (binding.socialsContainer.size == 3) {
            binding.buttonAddSocial.visibility = View.GONE
        }

        binding.socialsContainer.addView(view)
//        binding.buttonAddSocial.requestFocus(View.FOCUS_DOWN)
        binding.scrollCard.scrollTo(0, binding.scrollCard.bottom)
    }

    fun onAvatarClick(view: View) {

    }

    private fun removeSocial(view: View) {
        binding.socialsContainer.removeView(view)
        binding.buttonAddSocial.visibility = View.VISIBLE
    }
}