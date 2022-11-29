package com.danilovfa.innowisedatatransfer

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.danilovfa.innowisedatatransfer.databinding.CardItemEditBinding
import com.danilovfa.innowisedatatransfer.databinding.FragmentCardEditBinding

const val CONTENT_TAG = "edit-content"
const val EDIT_AVATAR = "edit-avatar"

class CardEdit : Fragment() {
    private lateinit var binding: FragmentCardEditBinding

    private val socialsDefault = setOf("Twitter", "GitHub", "LinkedIn", "Reddit", "CV")
    private var socialsSelected = mutableSetOf<String>()

    private val contactsDefault = setOf("Address", "Phone", "Email")
    private var contactsSelected = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).title = "Edit your card"

        binding = FragmentCardEditBinding.inflate(inflater, container, false)
        val view = binding.root

        // Add first item to container and set up add button listener
        addItem(binding.contactsContainer)
        onAddContact()
        addItem(binding.socialsContainer)
        onAddSocial()

        // TODO Fix multiple on change listener calls
        // TODO Save data on orientation change

        onChangeItem(binding.contactsContainer)
        onChangeItem(binding.socialsContainer)

        // Load default avatar
        loadAvatarDefault()
        // Set up onclick avatar change listener
        onAvatarClick()

        // Inflate the layout for this fragment
        return view
    }

    private fun onAddContact() {
        binding.buttonAddContact.setOnClickListener {
            addItem(binding.contactsContainer)
        }
    }

    private fun onAddSocial() {
        binding.buttonAddSocial.setOnClickListener {
            addItem(binding.socialsContainer)
        }
    }

    private fun addItem(container: LinearLayout) {
        // Get sets and button depending on a container
        val setDefault: Set<String>
        val setSelected: MutableSet<String>
        val button: Button
        when(container.id) {
            R.id.socialsContainer -> {
                button = binding.buttonAddSocial
                setDefault = socialsDefault
                setSelected = socialsSelected
            }
            R.id.contactsContainer -> {
                button = binding.buttonAddContact
                setDefault = contactsDefault
                setSelected = contactsSelected
            }
            else -> {
                // TODO Throw error
                Log.i(CONTENT_TAG, "addItem: Wrong container!")
                return
            }
        }

        val entries = (setDefault - setSelected).toList()

        val view = layoutInflater.inflate(R.layout.card_item_edit, null, false)
        val rowBinding = CardItemEditBinding.bind(view)

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, entries)
        rowBinding.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set up remove item listener on new item
        rowBinding.buttonDelete.setOnClickListener {
            removeItem(view, container, button, setSelected)
        }

        container.addView(view)

        // TODO Focus on new item
//        binding.buttonAddSocial.requestFocus(View.FOCUS_DOWN)
//        binding.scrollCard.scrollTo(0, binding.scrollCard.bottom)

        if (container.size == setDefault.size) {
            button.visibility = View.GONE
        }

        when(container.id) {
            R.id.socialsContainer -> socialsSelected = getEntries(container)
            R.id.contactsContainer -> contactsSelected = getEntries(container)
        }

        updateItems(container)
    }

    /**
     * For each row in contacts/socials container update spinner adapter and listeners
     */
    private fun updateItems(container: LinearLayout) {
        val set = when (container.id) {
            R.id.socialsContainer -> {
                socialsDefault - socialsSelected
            }
            R.id.contactsContainer -> {
                contactsDefault - contactsSelected
            }
            else -> {
                Log.i(CONTENT_TAG, "updateItems: wrong container is passed")
                return
            }
        }

        container.forEach {
            // Get views
            val layout = it as ConstraintLayout
            val spinner = (layout[0] as FrameLayout)[0] as Spinner

            // Update listener
            onChangeItem(container)

            // Update spinner adapter
            val entries = set.toMutableList()
            entries.add(0, spinner.selectedItem.toString())
            val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, entries)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = arrayAdapter
        }
    }

    /**
     * Remove item from contact/social container.
     * view - item to remove
     * container - container where item is located (binding.contactContainer or binding.socialContainer)
     * buttonAdd - button to add new item
     * entriesSelected - set of selected entries in container
     */
    private fun removeItem(view: View, container: LinearLayout, buttonAdd: Button, entriesSelected: MutableSet<String>) {
        // Remove selected item from entriesSelected
        val layout = view as ConstraintLayout
        val spinner = (layout[0] as FrameLayout)[0] as Spinner

        entriesSelected.remove(spinner.selectedItem.toString())
        updateItems(container)

        // Remove view
        container.removeView(view)
        buttonAdd.visibility = View.VISIBLE
    }

    /**
     * Update spinners when any of container's spinner is selected
     */
    private fun onChangeItem(container: LinearLayout) {
        container.forEach {
            val layout = it as ConstraintLayout
            val spinner = (layout[0] as FrameLayout)[0] as Spinner

            spinner.onItemSelectedListener = object: OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.i(CONTENT_TAG, "onItemSelected: ")

                    when(container.id) {
                        R.id.socialsContainer -> socialsSelected = getEntries(container)
                        R.id.contactsContainer -> contactsSelected = getEntries(container)
                    }
                    updateItems(container)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    /**
     * Get all content from contact/social container.
     * Key is a selected item in Spinner
     * Value is text in EditText
     */
    private fun getContent(container: LinearLayout): HashMap<String, String> {
        val hashMap = hashMapOf<String, String>()

        container.forEach {
            val layout = it as ConstraintLayout
            val spinner = (layout[0] as FrameLayout)[0] as Spinner
            val editText = layout[1] as EditText

            hashMap[spinner.selectedItem.toString()] = editText.text.toString()
        }

        Log.i(CONTENT_TAG, "getContent: $hashMap")

        return hashMap
    }

    /**
     * Get all entries (selected items in spinners) in container.
     */
    private fun getEntries(container: LinearLayout): MutableSet<String> {
        val setEntries = mutableSetOf<String>()

        container.forEach {
            val layout = it as ConstraintLayout
            val spinner = (layout[0] as FrameLayout)[0] as Spinner

            setEntries.add(spinner.selectedItem.toString())
        }

        Log.i(CONTENT_TAG, "getEntries: $setEntries")

        return setEntries
    }

    /**
     * Load default avatar and cicle crop it using Glide
     */
    private fun loadAvatarDefault() {
        Glide.with(requireActivity())
            .load(R.drawable.avatar_default)
            .circleCrop()
            .into(binding.cardAvatar)
    }

    /**
     * When avatar is clicked run image picker
     */
    private fun onAvatarClick() {
        binding.cardAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }
    }

    /**
     * If image pick intent is successful circle crop image and load into avatar
     */
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
                val imageUri = data.data as Uri
                Log.i(EDIT_AVATAR, "$imageUri")
                Glide.with(requireActivity())
                    .load(imageUri)
                    .circleCrop()
                    .placeholder(R.drawable.avatar_default)
                    .into(binding.cardAvatar);
            }
        }
    }


}