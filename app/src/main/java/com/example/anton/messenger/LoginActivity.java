package com.example.anton.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anton.messenger.request.Login;
import com.example.anton.messenger.response.LoginResponse;
import com.example.anton.messenger.response.user.User;
import com.example.anton.messenger.response.user_data.UserDataResponse;
import com.example.anton.messenger.response.user_data.UserDataResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by anton on 15.5.17.
 */

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText loginText;
    EditText passwordText;
    Gson gson;
    Context self;
    Activity currentActivity;
    Realm realm;
    String jwt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginText = (EditText)findViewById(R.id.loginText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetState.isOnline(getApplicationContext())) {
                    Login();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Offline mode", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        gson = new GsonBuilder()
                .registerTypeAdapter(UserDataResponse.class, new UserDataResponseDeserializer())
                .create();

        self = this.getApplicationContext();
        currentActivity = this;

        Realm.init(this);

        realm = Realm.getDefaultInstance();
        RealmResults<UserData> result = realm.where(UserData.class).findAll();
        if(result.size()==1){
            Intent intent = new Intent(self, MainActivity.class);
            startActivity(intent);
            currentActivity.finish();
        }
    }

    public void Login(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://188.166.93.46:3001/ask_token";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        LoginResponse lr = gson.fromJson(response, LoginResponse.class);
                        if(lr.getStatus().compareTo("ok")==0){
                            Log.d("LOG", "LOGINACTIVITY: LoginResponse | jwt: "+lr.getJwt());
                            jwt = lr.getJwt();
                            //editor.putString("jwt", lr.getJwt());
                            //editor.commit();
                            loadUserData();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Server error", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", loginText.getText().toString());
                params.put("password", passwordText.getText().toString());
                return params;
            }
        };

        queue.add(putRequest);
    }

    public void loadUserData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://188.166.93.46:3001/users/data";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOG", "LOGINACTIVITY: usersLoad | " + response);
                        UserDataResponse udr = gson.fromJson(response, UserDataResponse.class);
                        setUserData(udr.getUser());
                        toMainActivity();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast toast = Toast.makeText(getApplicationContext(),
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
            public Map<String, String>getHeaders() throws AuthFailureError{
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jwt", jwt);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setUserData(User user){

        realm.beginTransaction();
        UserData userData = realm.createObject(UserData.class);
        userData.setJwt(jwt);
        userData.set_id(user.get_id());
        userData.setUsername(user.getUsername());
        userData.setFirst_name(user.getPd().getFirstName());
        userData.setLast_name(user.getPd().getLastName());
        userData.setEmail(user.getPd().getEmail());
        realm.commitTransaction();

        Log.d("LOG", "LOGINACTIVITY: setuserData | username: "+user.getUsername());
        Log.d("LOG", "LOGINACTIVITY: setuserData | _id: "+user.get_id());
        Log.d("LOG", "LOGINACTIVITY: setuserData | first_name: "+user.getPd().getFirstName());
        Log.d("LOG", "LOGINACTIVITY: setuserData | last_name: "+user.getPd().getLastName());
        Log.d("LOG", "LOGINACTIVITY: setuserData | email: "+user.getPd().getEmail());
        //editor.commit();
    }

    public void toMainActivity(){
        Intent intent = new Intent(self, MainActivity.class);
        startActivity(intent);
        currentActivity.finish();
    }
}
