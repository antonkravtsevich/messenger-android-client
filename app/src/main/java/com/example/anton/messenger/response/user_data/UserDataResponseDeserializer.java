package com.example.anton.messenger.response.user_data;

import com.example.anton.messenger.response.user.PersonalData;
import com.example.anton.messenger.response.user.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by anton on 22.5.17.
 */

public class UserDataResponseDeserializer implements JsonDeserializer<UserDataResponse> {
    @Override
    public UserDataResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        UserDataResponse udr = new UserDataResponse();
        JsonObject jsonObject = json.getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        udr.setStatus(status);

        if(status.compareTo("ok")==0){
            JsonObject juser = jsonObject.get("message").getAsJsonObject();
            String username = juser.get("username").getAsString();
            String _id = juser.get("_id").getAsString();
            PersonalData pd = ((PersonalData)context.deserialize(juser.get("personal_data").getAsJsonObject(), PersonalData.class));
            User u = new User(username, _id, pd);
            udr.setUser(u);
        } else {
            udr.setStatus(jsonObject.get("message").getAsString());
        }
        return udr;
    }
}