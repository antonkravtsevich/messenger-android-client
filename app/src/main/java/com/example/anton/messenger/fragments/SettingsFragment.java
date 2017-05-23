package com.example.anton.messenger.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anton.messenger.InternetState;
import com.example.anton.messenger.R;
import com.example.anton.messenger.UserData;
import com.example.anton.messenger.response.chats.ChatsResponse;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anton on 15.5.17.
 */

public class SettingsFragment extends Fragment {

    Realm realm;
    UserData userData;
    Button panicButton;
    String jwt;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        userData = realm.where(UserData.class).findFirst();
        jwt = userData.getJwt();

        panicButton = (Button) getView().findViewById(R.id.btnPanic);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetState.isOnline(getActivity())) {
                    PanicCycle();
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Sorry, no internet connection", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void PanicCycle() {


        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "http://188.166.93.46:3001/users";

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getActivity().finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Server error", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (NullPointerException e) {
                    Log.d("LOG", "SETTINGS: PANIC!!!! " + e.toString() + "/n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("jwt", jwt);
                return params;
            }
        };

        realm.beginTransaction();
        RealmResults<UserData> rows = realm.where(UserData.class).findAll();
        rows.deleteAllFromRealm();
        realm.commitTransaction();

        queue.add(stringRequest);
    }
}
