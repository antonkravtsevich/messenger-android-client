package com.example.anton.messenger.response.chat;

import android.util.Log;

import com.example.anton.messenger.response.chats.Chat;
import com.example.anton.messenger.response.chats.ChatsResponse;
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

public class ChatResponseDeserializer implements JsonDeserializer<ChatResponse> {
    @Override
    public ChatResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        ChatResponse cr = new ChatResponse();
        JsonObject jsonObject = json.getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        Log.d("LOG", "Status: "+status);
        cr.setStatus(status);

        if(status.compareTo("ok")==0){
            JsonObject jchat = jsonObject.get("message").getAsJsonObject();
            Log.d("LOG", "CHATRESPONSEDESERIALIZER: deserialize | chats: "+jchat.toString());
            cr.setChat((Chat)context.deserialize(jchat, Chat.class));
        } else {
            cr.setStatus(jsonObject.get("message").getAsString());
        }
        return cr;
    }
}