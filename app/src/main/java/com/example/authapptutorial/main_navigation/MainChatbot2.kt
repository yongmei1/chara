package com.example.authapptutorial.main_navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.authapptutorial.R

class MainChatbot2 : AppCompatActivity() {
    private val button: Button? = null
    lateinit var cancelBtn: ImageView

    @SuppressLint("NonConstantResourceId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainchatbot2)


        cancelBtn = findViewById(R.id.cancelBtn)

        cancelBtn.setOnClickListener { v: View ->
            val i = Intent(v.context, MainChatbot::class.java)
            startActivity(i)
        }

    }
}