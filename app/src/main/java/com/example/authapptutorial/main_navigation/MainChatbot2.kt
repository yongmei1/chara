package com.example.authapptutorial.main_navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.authapptutorial.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainChatbot2 : AppCompatActivity() {
    private val adapterChatBot = AdapterChatBot()
    lateinit var sendbutton: ImageView
    lateinit var etChat: EditText
    lateinit var cancelBtn: ImageView
    lateinit var rvChatList: RecyclerView

    @SuppressLint("NonConstantResourceId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbotexample)

        //initialise and assign variable
        val bottomNav = findViewById<BottomNavigationView>(R.id.menu_navigation)
        //set chatbot main selected
        //set chatbot main selected
        bottomNav.selectedItemId = R.id.chatbot

        //perform itemselectedlistner

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.account -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            Account::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            List::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chatbot -> return@setOnNavigationItemSelectedListener true
                R.id.library -> {
                    startActivity(Intent(applicationContext, Library::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.21:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        rvChatList = findViewById(R.id.rvChatList)
        sendbutton = findViewById(R.id.btnSend)
        rvChatList.layoutManager = LinearLayoutManager(this)
        rvChatList.adapter =  adapterChatBot
        etChat=findViewById(R.id.etChat)

        sendbutton.setOnClickListener {
                if(etChat.text.isNullOrEmpty()){
                    Toast.makeText(this@MainChatbot2, "Please enter some text", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                adapterChatBot.addChatToList(ChatModel(etChat.text.toString()))
                apiService.chatWithTheBit(etChat.text.toString()).enqueue(callBack)
                etChat.text.clear()
            }

    }

    private val callBack = object  : Callback<ChatResponse>{
        override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
            if(response.isSuccessful &&  response.body()!= null){
                adapterChatBot.addChatToList(ChatModel(response.body()!!.chatBotReply))
            }else{
                Toast.makeText(this@MainChatbot2, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
            Toast.makeText(this@MainChatbot2, "Something went wrong", Toast.LENGTH_LONG).show()
        }

    }
}