package com.example.ephone.util

import android.util.Log
import com.example.ephone.model.Message
import com.example.ephone.model.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Helper {
    companion object{
        fun parseMessage(body:String,dateTime: LocalDateTime): Message? {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val date = dateTime.format(dateFormatter)
            val time = dateTime.format(timeFormatter)
            val amounts = body.split(" ");
            val amountIndex = amounts.indexOf("ETB");
            val remainingIndex = amounts.lastIndexOf("ETB");
            return if(amountIndex > 0  && remainingIndex > 0){
                val amount = amounts[amountIndex+1];
                val remaining = amounts[remainingIndex+1];
                Message(
                    date,
                    "Commercial Bank Of Ethiopia",//default
                    time,
                    if(body.lowercase().contains("debited")) Type.Debited else Type.Credited,
                    if(amount.last()== '.') amount.dropLast(1) else amount ,
                    if(remaining.last()== '.') remaining.dropLast(1) else remaining ,
                    dateTime
                );
            }else{
                //Promotional Message
                null;
            }
        }
    }

}