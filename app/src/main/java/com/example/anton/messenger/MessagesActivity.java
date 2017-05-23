package com.example.anton.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anton.messenger.response.chat.ChatResponse;
import com.example.anton.messenger.response.chat.ChatResponseDeserializer;
import com.example.anton.messenger.response.chats.Chat;
import com.example.anton.messenger.response.chats.ChatDeserializer;
import com.example.anton.messenger.response.chats.ChatsResponse;
import com.example.anton.messenger.response.chats.Message;
import com.example.anton.messenger.response.chats.MessagesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by anton on 15.5.17.
 */

public class MessagesActivity extends Activity {

    Realm realm;
    UserData userData;
    String jwt;
    Gson gson;
    String chat_id;
    String companion_name;
    String reciever;

    ListView messagesList;
    EditText messageText;
    Button btnSendMessage;
    Handler myHandler;

    Context context;

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            reloadMessages();
            myHandler.postDelayed(runnableCode, 1000);
        }
    };

    //в onDestroy добавить удаление companion_name
    //а еще лучше переписать хранение этого говна в переменной и передачу в конструктор,
    //а то шо ты как дурной ваще
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Intent intent = this.getIntent();
        chat_id = intent.getStringExtra("chat_id");
        companion_name = intent.getStringExtra("companion_name");
        reciever = intent.getStringExtra("reciever");

        gson = new GsonBuilder()
                .registerTypeAdapter(ChatResponse.class, new ChatResponseDeserializer())
                .registerTypeAdapter(Chat.class, new ChatDeserializer())
                .create();

        realm = Realm.getDefaultInstance();
        userData = realm.where(UserData.class).findFirst();
        jwt = userData.getJwt();
        context = this;

        messagesList = (ListView) findViewById(R.id.messagesList);
        messageText = (EditText) findViewById(R.id.messageText);
        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Log.d("LOG", "chat "+chat_id+" with "+userData.getUsername());

        myHandler = new Handler();
        myHandler.post(runnableCode);
    }

    @Override
    protected void onDestroy(){
        myHandler.removeCallbacks(runnableCode);
        super.onDestroy();
    }

    private void reloadMessages(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://188.166.93.46:3001/chats/"+chat_id;

        Log.d("LOG", "MESSAGESACTIVITY: reloadMessages | start");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ChatResponse cr = gson.fromJson(response, ChatResponse.class);
                        ArrayList<Message> messages = cr.getChat().getMessages();
                        MessagesAdapter adapter = new MessagesAdapter(context, messages, companion_name);
                        messagesList.setAdapter(adapter);
                        messagesList.setSelection(adapter.getCount()-1);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast toast = Toast.makeText(context,
                                    "Server error", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        catch(NullPointerException e){
                            Log.d("LOG", "USERFRAGMENT: usersLoad | onErrorResponse: "+e.toString()+"/n"+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", jwt);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void sendMessage(){
        Log.d("LOG", "MESSAGESACTIVITY: sengMessage | ");
        RequestQueue queue = Volley.newRequestQueue(context);

        String url ="http://188.166.93.46:3001/messages";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        reloadMessages();
                        messageText.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast toast = Toast.makeText(context,
                            "Server error", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch(NullPointerException e){
                    Log.d("LOG", "MESSAGESACTIVITY: sendMessage | onErrorResponse: "+e.toString()+"/n"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", jwt);

                return params;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("reciever", reciever);
                params.put("text", messageText.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }
}