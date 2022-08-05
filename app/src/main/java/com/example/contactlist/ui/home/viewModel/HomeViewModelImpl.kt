package com.example.contactlist.ui.home.viewModel

import androidx.lifecycle.*
import com.example.contactlist.data.model.BaseItem
import com.example.contactlist.data.model.Title
import com.example.contactlist.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(val contactRepository: ContactRepository): HomeViewModel, ViewModel() {
    private val _contacts: MutableLiveData<List<BaseItem>> = MutableLiveData()
    override val contacts: LiveData<List<BaseItem>> = _contacts

    override val emptyScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _refreshFinished: MutableSharedFlow<Unit> = MutableSharedFlow()
    override val refreshFinished: SharedFlow<Unit> = _refreshFinished

    init {
        getContacts()
    }

    private fun getContacts() {
        viewModelScope.launch {
            val response = contactRepository.getContacts()
            _refreshFinished.emit(Unit)
            val tempList: MutableList<BaseItem> = mutableListOf()

            if (response.isNotEmpty()) {
                val len = response.size - 1

                tempList.add(Title(response[0].name[0].toString()))
                tempList.add(response[0])

                for(i in 1..len) {
                    // Adds title
                    if(response[i - 1].name[0] != response[i].name[0]) {
                        val title = Title(response[i].name[0].toString())
                        tempList.add(title)
                    }

                    tempList.add(response[i])
                }

                _contacts.value = tempList
            }

            emptyScreen.value = _contacts.value.isNullOrEmpty()
        }
    }

    override fun onDeleteClicked(id: Int) {
        viewModelScope.launch {
            contactRepository.deleteContact(id)
            refresh()
        }
    }

    override fun refresh() {
        getContacts()
    }

}