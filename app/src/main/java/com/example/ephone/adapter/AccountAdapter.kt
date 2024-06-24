package com.example.ephone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ephone.R
import com.example.ephone.model.Message
import com.github.islamkhsh.CardSliderAdapter

class AccountAdapter (private var accounts : List<Message>) :
    CardSliderAdapter<AccountAdapter.AccountViewHolder>() {

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val accountAmount: TextView
        val bankName: TextView
        val lastChecked: TextView

        init {
            accountAmount = view.findViewById(R.id.balance)
            bankName = view.findViewById(R.id.bankName)
            lastChecked = view.findViewById(R.id.lastCheck)
        }
    }
    override fun getItemCount() = accounts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun bindVH(holder: AccountViewHolder, position: Int) {
        //TODO bind item object with item layout view
        val account = accounts[position]
        holder.accountAmount.text= " ETB " +account.remaining_balance
        holder.bankName.text= account.bank_name
        holder.lastChecked.text= " "+account.time+ " "+ account.date

    }

    fun updateAccounts(newAccounts: List<Message>) {
        accounts = newAccounts;
        notifyDataSetChanged();
    }


}