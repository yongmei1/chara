package com.example.authapptutorial.main_navigation


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.authapptutorial.R


class AdapterChatBot  : RecyclerView.Adapter<AdapterChatBot.MyViewHolder>(){
    lateinit var txtChat: TextView
    private val list = ArrayList<ChatModel>()


    inner class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(

        LayoutInflater.from(parent.context).inflate(R.layout.listitem_chat, parent, false)
    ) {

//to access txtChat component directly lets add a kotlin plugin, now we can call components directly
        fun bind(chat: ChatModel) = with(itemView) {
            if(!chat.isBot) {
                    txtChat = findViewById(R.id.txtChat)
                    txtChat.setBackgroundColor(Color.WHITE)
                    txtChat.setTextColor(Color.BLACK)
                    txtChat.text = chat.chat
            }else{
                txtChat.setBackgroundColor(Color.CYAN)
                txtChat.setTextColor(Color.BLACK)
                txtChat.text = chat.chat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addChatToList(chat: ChatModel) {
        list.add(chat)
        notifyDataSetChanged()
    }
}

