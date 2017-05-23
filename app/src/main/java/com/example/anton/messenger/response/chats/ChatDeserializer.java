package com.example.anton.messenger.response.chats;

import android.util.Log;

import com.example.anton.messenger.response.user.PersonalData;
import com.example.anton.messenger.response.user.User;
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

public class ChatDeserializer implements JsonDeserializer<Chat> {
    @Override
    public Chat deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Chat c = new Chat();
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray messages = jsonObject.get("messages").getAsJsonArray();
        Log.d("LOG", "CHATDESERIALIZER: deserialize | messages: "+messages.toString());
        ArrayList<Message> messages_list = new ArrayList<>();
        for(JsonElement jmessage: messages){
            messages_list.add((Message)context.deserialize(jmessage, Message.class));
        }
        c.setMessages(messages_list);
        c.set_id(jsonObject.get("_id").getAsString());
        ArrayList<User> user_list = new ArrayList<>();
        JsonArray users = jsonObject.get("users").getAsJsonArray();
        for(JsonElement juser: users){
            Log.d("LOG", "CHATDESERIALIZER: deserialize | juser: "+juser.toString());
            String username = ((JsonObject)juser).get("username").getAsString();
            String _id = ((JsonObject)juser).get("_id").getAsString();
            PersonalData pd = ((PersonalData)context.deserialize(((JsonObject)juser).get("personal_data").getAsJsonObject(), PersonalData.class));
            User u = new User(username, _id, pd);
            user_list.add(u);
        }
        c.setUser(user_list.get(0));
        return c;
    }
}