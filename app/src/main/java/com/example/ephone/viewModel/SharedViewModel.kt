package com.example.ephone.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ephone.model.Message

class SharedViewModel: ViewModel() {
    val messages: MutableLiveData<List<Message>> = MutableLiveData(emptyList())
    val accounts: MutableLiveData<List<Message>> = MutableLiveData(emptyList())

    fun addMessage(message: Message) {
        val currentList = messages.value ?: emptyList()
        messages.value = currentList + message
    }

    fun updateMessages(newMessages:List<Message>){
        messages.value = newMessages;
    }

    fun addAccounts(newAccounts: List<Message>){
        Log.d("Added Accounts",newAccounts.size.toString())
        accounts.value = newAccounts
    }

    fun updateAccount(bankName:String,account:Message){
        var isFound = false
        Log.d("Updating Account",bankName)
        accounts.value= accounts.value?.map {
            if(it.bank_name== bankName){
                isFound=true
                account
            }
            it
        }
        if(!isFound){
            val currentList = accounts.value ?: emptyList()
            accounts.value = currentList + account
        }
    }
}