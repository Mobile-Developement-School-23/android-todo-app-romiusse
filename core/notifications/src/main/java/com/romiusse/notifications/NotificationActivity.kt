package com.romiusse.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


       // val manager: FragmentManager = supportFragmentManager
       // val transaction: FragmentTransaction = manager.beginTransaction()
       // transaction.add(R.id.container, AddScreenFragment(), "")
       // transaction.addToBackStack(null)
        //transaction.commit()

    }
}