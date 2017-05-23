package com.example.anton.messenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.anton.messenger.InternetState;
import com.example.anton.messenger.MessagesActivity;
import com.example.anton.messenger.R;
import com.example.anton.messenger.UserData;
import com.example.anton.messenger.response.chat.ChatResponse;
import com.example.anton.messenger.response.chat.ChatResponseDeserializer;
import com.example.anton.messenger.response.user.User;
import com.example.anton.messenger.response.user.UsersAdapter;
import com.example.anton.messenger.response.user.UsersResponse;
import com.example.anton.messenger.response.user.UsersResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by anton on 15.5.17.
 */

public class UsersFragment extends Fragment {

    ListView usersList;
    EditText usersSearch;
    UsersResponse ur;
    Gson gson;
    Context context;
    Realm realm;
    UserData userData;
    String jwt;

    public UsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        gson = new GsonBuilder()
                .registerTypeAdapter(UsersResponse.class, new UsersResponseDeserializer())
                .registerTypeAdapter(ChatResponse.class, new ChatResponseDeserializer())
                .create();

        usersList = (ListView) getView().findViewById(R.id.usersList);
        usersSearch = (EditText) getView().findViewById(R.id.usersSearch);

        realm = Realm.getDefaultInstance();
        userData = realm.where(UserData.class).findFirst();
        jwt = userData.getJwt();

        context = this.getActivity().getApplicationContext();

        usersSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usersLoad();
            }

            @Override
            public void afterTextChanged(Editable s) {
                usersLoad();
            }
        });

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startChatWithUserByIndex(position);
            }
        });

        usersLoad();
    }

    public void usersLoad(){
        if(InternetState.isOnline(getActivity())) {
            RequestQueue queue = Volley.newRequestQueue(this.getContext());
            String url = "http://188.166.93.46:3001/users";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("LOG", "USERFRAGMENT: usersLoad | " + response);
                            ur = gson.fromJson(response, UsersResponse.class);
                            updateList();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Server error", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (NullPointerException e) {
                        Log.d("LOG", "USERFRAGMENT: usersLoad | onErrorResponse: " + e.toString() + "/n" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            queue.add(stringRequest);
        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    "Offline mode", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void updateList(){
        ArrayList<User> users = ur.getUsers();
        Log.d("LOG", "USERSFRAGMENT: updateList | ur.usernames : "+users);
        if(usersSearch.getText().toString().length()!=0) {
            users = User.FilterUsers(users, usersSearch.getText().toString());
        }
        UsersAdapter usersAdapter = new UsersAdapter(context, users);
        usersAdapter.notifyDataSetChanged();
        usersList.setAdapter(usersAdapter);
    }

    public void startChatWithUserByIndex(Integer userIndex){
        User user = ur.getUsers().get(userIndex);
        if(user.getUsername().compareTo(userData.getUsername())==0){
            Toast toast = Toast.makeText(context,
                    "Невозможно начать чат с самим собой", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            String user_id = user.get_id();
            final String username = user.getUsername();
            final String reciever = user.get_id();

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://188.166.93.46:3001/chats/by_users/" + user_id;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("LOG", "USERFRAGMENT: Login | raw response: "+response.toString());
                            ChatResponse cr = gson.fromJson(response, ChatResponse.class);
                            Log.d("LOG", "USERSFRAGMENT: Login | chatResponse: "+cr.toString());
                            String chat_id = cr.getChat().get_id();
                            Log.d("LOG", "USERSFRAGMENT: startActivityWithUser | chat_id: "+chat_id);
                            startChat(chat_id, username, reciever);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Toast toast = Toast.makeText(context,
                                        "Server error", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (NullPointerException e) {
                                Log.d("LOG", "USERFRAGMENT: usersLoad | onErrorResponse: " + e.toString() + "/n" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("jwt", jwt);

                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }

    public void startChat(String chat_id, String username, String reciever){
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("chat_id", chat_id);
        intent.putExtra("companion_name", username);
        intent.putExtra("reciever", reciever);
        startActivity(intent);
    }
}
