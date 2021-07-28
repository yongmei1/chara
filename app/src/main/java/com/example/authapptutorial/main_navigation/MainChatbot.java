package com.example.authapptutorial.main_navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.authapptutorial.R;

import java.util.ArrayList;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainChatbot extends AppCompatActivity {

    //creating variables for our widgets in xml file.
    private RecyclerView chatsRV;
    private Button button;
    private EditText usermessagebox;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    //creating a variable for array list and adapter class.
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;


    @SuppressLint({"NonConstantResourceId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_test);
/*
        //initialise and assign variable
        BottomNavigationView bottomNav = findViewById(R.id.menu_navigation);
        //set chatbot main selected
        bottomNav.setSelectedItemId(R.id.chatbot);

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), Account.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.list:
                    startActivity(new Intent(getApplicationContext(), List.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.chatbot:
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), Library.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });


    chatsRV = findViewById(R.id.listview);
    sendMsgIB = findViewById(R.id.button);
    userMsgEdt = findViewById(R.id.userMessage);
    //below line is to initialize our request queue.
    //creating a new array list
    messageModalArrayList = new ArrayList<>();
    //adding on click listener for send message button.
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //checking if the message entered by user is empty or not.
            if (userMsgEdt.getText().toString().isEmpty()) {
                //if the edit text is empty display a toast message.
                Toast.makeText(MainChatbot.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                return;
            }
            //calling a method to send message to our bot to get response.
            sendMessage(userMsgEdt.getText().toString());
            //below line we are setting text in our edit text as empty
            userMsgEdt.setText("");

        }
    });

    //on below line we are initialiing our adapter class and passing our array lit to it.
    messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);
    //below line we are creating a variable for our linear layout manager.
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainChatbot.this, RecyclerView.VERTICAL, false);
    //below line is to set layout manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);
    //below line we are setting adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);
}

    private void sendMessage(String userMsg) {
        //below line is to pass message to our array list which is entered by the user.
        messageModalArrayList.add(new MessageModal(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();


        //in on response method we are extracting data from json response and adding this response to our array list.


        String botResponse = response.getString("cnt");
        messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
        //notifying our adapter as data changed.
        messageRVAdapter.notifyDataSetChanged();

        //handling error response from bot.
        messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
        messageRVAdapter.notifyDataSetChanged();

    }*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.8.100:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiSerevice myApi = retrofit.create(ApiSerevice.class);
    ListView superListView;
    findViewById(R.id.superListView);

    button.setOnClickListener(v ->{
        if(usermessagebox.getText()!=null){
            Toast.makeText(this,"Please enter a response",Toast.LENGTH_LONG).show();
            messageRVAdapter.addChatToList(new MessageModal(usermessagebox.getText().toString()));
            myApi.chatWithTheBit(usermessagebox.getText().toString());
            usermessagebox.getText().clear();

        }
        else{
            Toast.makeText(this,"thanks!",Toast.LENGTH_LONG).show();
        }
        });

}



}

