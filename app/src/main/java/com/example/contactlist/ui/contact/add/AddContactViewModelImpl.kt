package com.example.contactlist.ui.contact.add

import androidx.lifecycle.viewModelScope
import com.example.contactlist.data.model.Contact
import com.example.contactlist.data.repository.ContactRepository
import com.example.contactlist.ui.contact.base.BaseContactViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModelImpl @Inject constructor(val repository: ContactRepository): BaseContactViewModelImpl(), AddContactViewModel {

    override fun save() {
        viewModelScope.launch {
            if(name.value.isNullOrEmpty() || phone.value.isNullOrEmpty()) {
                _error.emit("Please enter both name and phone properly")
            } else {
                val contact = Contact(name = name.value!!, phone = phone.value!!)
                repository.addContact(contact)
                _finish.emit(Unit)
            }
        }
    }
}