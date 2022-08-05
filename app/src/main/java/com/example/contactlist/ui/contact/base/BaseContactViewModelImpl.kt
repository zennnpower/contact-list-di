package com.example.contactlist.ui.contact.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseContactViewModelImpl: ViewModel(), BaseContactViewModel {
    override val name: MutableLiveData<String> = MutableLiveData()
    override val phone: MutableLiveData<String> = MutableLiveData()

    protected val _finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    override val finish: SharedFlow<Unit> = _finish

    protected val _error: MutableSharedFlow<String> = MutableSharedFlow()
    override val error: SharedFlow<String> = _error
}