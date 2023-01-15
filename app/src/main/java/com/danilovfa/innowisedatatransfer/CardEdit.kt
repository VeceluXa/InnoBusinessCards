package com.danilovfa.innowisedatatransfer

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.util.Log
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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.danilovfa.innowisedatatransfer.databinding.CardItemEditBinding
import com.danilovfa.innowisedatatransfer.databinding.FragmentCardEditBinding
import java.io.Serializable

const val CONTENT_TAG = "edit-content"
const val EDIT_AVATAR = "edit-avatar"

class CardEdit : Fragment() {
    private lateinit var binding: FragmentCardEditBinding

    private var avatarUri = Uri.parse("android.resource://com.danilovfa.innowisedatatransfer/drawable/avatar_default.jpg")

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

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            avatarUri = savedInstanceState.parcelable("avatar")
            val socials = savedInstanceState.serializable<HashMap<String, String>>("socials")
            val contacts = savedInstanceState.serializable<HashMap<String, String>>("contacts")

            addItems(socials!!, binding.socialsContainer)
            addItems(contacts!!, binding.contactsContainer)

        } else {
            addItem(binding.contactsContainer)
            addItem(binding.socialsContainer)
        }

        // Setup add item listeners
        onAddContact()
        onAddSocial()

        // TODO Fix multiple on change listener calls

        onChangeItem(binding.contactsContainer)
        onChangeItem(binding.socialsContainer)

        // Load avatar
        loadAvatar()
        // Set up onclick avatar change listener
        onAvatarClick()
    }

    private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
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
        val setDefault = getDefault(container)
        val setSelected = getSelected(container)
        val button = getButton(container)

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

        changeSelected(getEntries(container), container)
        updateItems(container)
    }

    private fun addItems(items: HashMap<String, String>, container: LinearLayout) {
        val setDefault = getDefault(container)
        val setSelected = getSelected(container)
        var i = 0
        items.forEach {
            addItem(container)

            val layout = container[i] as ConstraintLayout
            val spinner = (layout[0] as FrameLayout)[0] as Spinner
            val editText = layout[1] as EditText

            val key = it.key
            val value = it.value

            // Set up adapter
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, setDefault.toList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Choose right item
            val position = adapter.getPosition(key)
            spinner.setSelection(position)

            editText.text = SpannableStringBuilder(value)

            setSelected.add(key)
            changeSelected(setSelected, container)

            i++
        }

        // Get sets and button depending on a container
//        updateItems(container)
    }

    /**
     * For each row in contacts/socials container update spinner adapter and listeners
     */
    private fun updateItems(container: LinearLayout) {
        val set = getDefault(container) - getSelected(container)

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

                    changeSelected(getEntries(container), container)
                    updateItems(container)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
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
                avatarUri = imageUri
                Log.i(EDIT_AVATAR, "$imageUri")
                loadAvatar()
            }
        }
    }

    private fun loadAvatar() {
        Glide.with(requireActivity())
            .load(avatarUri)
            .circleCrop()
            .placeholder(R.drawable.avatar_default)
            .into(binding.cardAvatar);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("socials", getContent(binding.socialsContainer))
        outState.putSerializable("contacts", getContent(binding.contactsContainer))
        outState.putParcelable("avatar", avatarUri)
    }

    private fun getDefault(container: LinearLayout): Set<String> {
        return when(container.id) {
            R.id.socialsContainer -> socialsDefault
            R.id.contactsContainer -> contactsDefault
            else -> {
                throw Exception("getDefault: wrong container passed!")
            }
        }
    }

    private fun getSelected(container: LinearLayout): MutableSet<String> {
        return when(container.id) {
            R.id.socialsContainer -> socialsSelected
            R.id.contactsContainer -> contactsSelected
            else -> {
                throw Exception("getSelected: wrong container passed!")
            }
        }
    }

    private fun getButton(container: LinearLayout): Button {
        return when(container.id) {
            R.id.socialsContainer -> binding.buttonAddSocial
            R.id.contactsContainer -> binding.buttonAddContact
            else -> {
                throw Exception("getButton: wrong container passed!")
            }
        }
    }

    private fun changeSelected(set: MutableSet<String>, container: LinearLayout) {
        when(container.id) {
            R.id.socialsContainer -> socialsSelected = set
            R.id.contactsContainer -> contactsSelected = set
            else -> {
                throw Exception("changeSelected: wrong container passed!")
            }
        }
    }
}