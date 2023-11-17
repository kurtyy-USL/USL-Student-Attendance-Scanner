package com.example.uslstudentattendancescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanButton.setOnClickListener{
            Toast.makeText(applicationContext, "Scanner Tapped", Toast.LENGTH_LONG)
                .show()

            val intent = Intent(this, Scanner::class.java)
            startActivity(intent)

        }



    }




}