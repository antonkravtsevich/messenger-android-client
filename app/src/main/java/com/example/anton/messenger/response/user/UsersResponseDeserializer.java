package com.example.anton.messenger.response.user;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by anton on 22.5.17.
 */

public class UsersResponseDeserializer implements JsonDeserializer<UsersResponse> {
    @Override
    public UsersResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        UsersResponse ur = new UsersResponse();
        JsonObject jsonObject = json.getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        Log.d("LOG", "Status: "+status);
        ur.setStatus(status);

        if(status.compareTo("ok")==0){
            JsonArray users = jsonObject.get("message").getAsJsonArray();
            Log.d("LOG", "USERDESERIALIZER: deserialize | userses: "+users.toString());
            ArrayList<User> user_list = new ArrayList<>();
            for(JsonElement juser: users){
                Log.d("LOG", "USERDESERIALIZER: deserialize | juser: "+juser.toString());
                String username = ((JsonObject)juser).get("username").getAsString();
                String _id = ((JsonObject)juser).get("_id").getAsString();
                PersonalData pd = ((PersonalData)context.deserialize(((JsonObject)juser).get("personal_data").getAsJsonObject(), PersonalData.class));
                User u = new User(username, _id, pd);
                user_list.add(u);
            }
            ur.setUsers(user_list);
        } else {
            ur.setStatus(jsonObject.get("message").getAsString());
        }
        return ur;
    }
}