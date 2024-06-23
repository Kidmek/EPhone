package com.example.ephone.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ephone.model.Message

class SharedViewModel: ViewModel() {
    val messages: MutableLiveData<List<Message>> = MutableLiveData(emptyList())

    fun addMessage(message: Message) {
        val currentList = messages.value ?: emptyList()
        messages.value = currentList + message
    }
}