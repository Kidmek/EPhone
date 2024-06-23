package com.example.ephone

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ephone.model.Bank
import com.example.ephone.model.Message
import com.example.ephone.ui.fragment.ChartFragment
import com.example.ephone.ui.fragment.HomeFragment
import com.example.ephone.util.Helper
import com.example.ephone.viewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SharedViewModel

    lateinit var bottomNav : BottomNavigationView
    private val PERMISSION_REQUEST_CODE = 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.chart -> {
                    loadFragment(ChartFragment())
                    true
                }else -> false
            }
        }
        checkAndRequestPermissions()

        val smsReceiver = SmsReceiver { message ->
            viewModel.addMessage(message)
        }

        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION).apply {
            priority = 1000
        }
        registerReceiver(smsReceiver, filter)
    }


    private fun checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
                , PERMISSION_REQUEST_CODE)
        }else{
            readSmsMessages()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readSmsMessages();
        } else {
            Toast.makeText(this,
                "SMS permissions are required for this app to function.",
                Toast.LENGTH_LONG).show()
//          TODO: close on permission deny
//            finish()
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private fun readSmsMessages() {
        Log.i("READING", "Started");
        val smsUri = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(smsUri,
            null, null,
            null, null)
        var index = 0;

        cursor?.use {
            val indexBody = cursor.getColumnIndex("body")
            val indexAddress = cursor.getColumnIndex("address")
            val indexDate = cursor.getColumnIndex("date")
            while (cursor.moveToNext() ) {
                val smsBody = cursor.getString(indexBody)
                val address = cursor.getString(indexAddress)
                val date=cursor.getLong(indexDate);
                val dt =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(date),TimeZone.getDefault().toZoneId());
                if(Bank.values().any { b->b.name==address.trim() }){
                    val message =Helper.parseMessage(smsBody,dt)
                    if(message != null){
                        message.bank_name = Bank.valueOf(address.trim()).fullName;
                        viewModel.addMessage(message)
                    }
                }

                index++;
            }
        }
        Log.i("READING", "Finished");
    }

//    onMessageReceived callback as a way around passing
//    the view model
    class SmsReceiver(private val onMessageReceived: (Message) -> Unit)  : BroadcastReceiver() {
//        Update the view model to add new message
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (smsMessage in smsMessages) {
                    Log.d("SMS Received", smsMessage.messageBody)
                    if(Bank.values().any { b->b.name==smsMessage.originatingAddress?.trim()} ){
                        val message = Helper.parseMessage(
                            smsMessage.messageBody, LocalDateTime.now())
                        if(message != null){
                            message.bank_name = Bank.valueOf(smsMessage.originatingAddress?.trim()!!).fullName;
                            onMessageReceived(message)
                        }
                    }
                }
            }
        }
    }

}