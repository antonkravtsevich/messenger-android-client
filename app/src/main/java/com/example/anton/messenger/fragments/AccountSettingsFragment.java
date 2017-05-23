package com.example.anton.messenger.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.anton.messenger.InternetState;
import com.example.anton.messenger.R;
import com.example.anton.messenger.UserData;
import com.example.anton.messenger.response.user.UsersResponse;
import com.example.anton.messenger.response.user.UsersResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by anton on 15.5.17.
 */

public class AccountSettingsFragment extends Fragment {
    EditText nameEditText;
    EditText lastnameEditText;
    EditText emailEditText;
    Button saveAccountChangesBtn;
    Context context;
    Realm realm;
    UserData userData;
    String jwt;
    Gson gson;
    final String NAMESPACE = "http://188.166.93.46:3001";
    final String URL = "http://188.166.93.46:3001/wsdl";
    final String METHOD_NAME = "getData";
    final String SOAP_ACTION = URL;

    public AccountSettingsFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        realm = Realm.getDefaultInstance();
        userData = realm.where(UserData.class).findFirst();
        jwt = userData.getJwt();

        nameEditText = (EditText) this.getView().findViewById(R.id.nameEditText);
        lastnameEditText = (EditText) this.getView().findViewById(R.id.lastnameEditText);
        emailEditText = (EditText) this.getView().findViewById(R.id.emailEditText);
        saveAccountChangesBtn = (Button) this.getView().findViewById(R.id.btnSaveAccountChanges);

        context = this.getActivity().getApplicationContext();

        saveAccountChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        realm = realm.getDefaultInstance();

        //loadDataFromDatabase();
        startCycle();
    }

    private void startCycle(){
        if(InternetState.isOnline(getActivity())){
            loadDataFromDatabase();
            saveChanges();
        } else {
            loadData();
        }
    }

    private void loadDataFromDatabase(){
        userData = realm.where(UserData.class).findFirst();
        nameEditText.setText(userData.getFirst_name());
        lastnameEditText.setText(userData.getLast_name());
        emailEditText.setText(userData.getEmail());
    }

    //загрузка информации о пользователе с сервера в textview
    public void loadData(){
        if (InternetState.isOnline(getActivity())) {
            new callWebService().execute(userData.get_id());
        } else {
            Toast toast = Toast.makeText(context,
                    "Offline mode", Toast.LENGTH_SHORT);
            toast.show();
            loadDataFromDatabase();
        }
    }

    class callWebService extends AsyncTask<String, Void, String[]>{
        @Override
        protected void onPostExecute(String[] res){
            nameEditText.setText(res[0]);
            lastnameEditText.setText(res[1]);
            emailEditText.setText(res[2]);
            saveChangesInDatabase();
        }

        @Override
        protected String[] doInBackground(String... params){
            String res[] = new String[3];
            SoapObject soapObject = new SoapObject(NAMESPACE, "Request");

            PropertyInfo pa = new PropertyInfo();
            pa.setName("_id");
            pa.setValue(params[0]);
            pa.setType(String.class);
            soapObject.addProperty(pa);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

            try{
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.d("LOG", "first_name: " + resultRequestSOAP.getProperty("first_name").toString());
                res[0] = resultRequestSOAP.getProperty("first_name").toString();
                res[1] = resultRequestSOAP.getProperty("last_name").toString();
                res[2] = resultRequestSOAP.getProperty("email").toString();
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
            }

            return res;
        }
    }

    //сохранение данных из textview на сервере
    public void saveChanges(){
        Log.d("LOG", "ACCOUNTSETTINGSFRAGMENT: saveChangesOnServer | ");
        RequestQueue queue = Volley.newRequestQueue(context);

        String url ="http://188.166.93.46:3001/users";

        if(InternetState.isOnline(getActivity())) {
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast toast = Toast.makeText(context,
                                    "Изменения сохранены", Toast.LENGTH_SHORT);
                            toast.show();
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

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("first_name", nameEditText.getText().toString());
                    params.put("last_name", lastnameEditText.getText().toString());
                    params.put("email", emailEditText.getText().toString());
                    return params;
                }
            };
            saveChangesInDatabase();
            queue.add(stringRequest);
        } else {
            saveChangesInDatabase();
        }
    }

    public void saveChangesInDatabase(){
        realm.beginTransaction();
        userData.setFirst_name(nameEditText.getText().toString());
        userData.setLast_name(lastnameEditText.getText().toString());
        userData.setEmail(emailEditText.getText().toString());
        realm.commitTransaction();
    }
}
