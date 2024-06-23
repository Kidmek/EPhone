package com.example.ephone.model

import android.graphics.Color
import java.time.LocalDateTime

data class Message(
    val date: String?,
    var bank_name: String,
    val time: String?,
    val debit_or_credit: Type,
    val amount: String,
    val remaining_balance: String,
    val date_time: LocalDateTime,
)

enum class Type {
    Debited,
    Credited
}

enum class Bank(val fullName: String) {
    CBE("Commercial Bank Of Ethiopia"),
    BOA("Bank Of Abyssinia");
}

enum class BankColors(val color: Int) {
    CBE(Color.parseColor("#800080")),
    BOA(Color.YELLOW);
}