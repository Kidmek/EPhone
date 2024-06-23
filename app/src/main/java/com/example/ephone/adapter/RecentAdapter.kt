package com.example.ephone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ephone.R
import com.example.ephone.model.Message
import com.example.ephone.model.Type

class RecentAdapter(private var messages: List<Message>):
        RecyclerView.Adapter<RecentAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView
        val remaining : TextView
        val type: TextView
        val date: TextView
        val image: ImageView


        init {
            amount = view.findViewById(R.id.txtAmount)
            remaining = view.findViewById(R.id.txtRemaining)
            date = view.findViewById(R.id.txtDate)
            type = view.findViewById(R.id.txtType)
            image = view.findViewById(R.id.imageType)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_recent, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val message = messages[position]
        viewHolder.date.text = message.time+ " "+ message.date;
        viewHolder.amount.text = (if(message.debit_or_credit === Type.Credited) "+" else "-")+message.amount;
        viewHolder.remaining.text = message.remaining_balance;
        viewHolder.type.text = message.bank_name;
        viewHolder.image.setImageResource(
            if(message.debit_or_credit === Type.Credited)
                R.drawable.img_arrow_down_green
                else R.drawable.img_arrow_up
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages;
        notifyDataSetChanged();
    }

    fun changeType (type: Type?){
        messages = messages.filter {
            it.debit_or_credit ==type
        }
        notifyDataSetChanged()
    }

    fun addMessage(newMessage: Message){
        messages +newMessage;
        notifyItemInserted(messages.size - 1)
    }
}