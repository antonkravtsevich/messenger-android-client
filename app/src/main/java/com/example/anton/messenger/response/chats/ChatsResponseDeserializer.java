package com.example.anton.messenger.response.chats;

/**
 * Created by anton on 22.5.17.
 */

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

public class ChatsResponseDeserializer implements JsonDeserializer<ChatsResponse> {
    @Override
    public ChatsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        ChatsResponse cr = new ChatsResponse();
        JsonObject jsonObject = json.getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        Log.d("LOG", "Status: "+status);
        cr.setStatus(status);

        if(status.compareTo("ok")==0){
            JsonArray chats = jsonObject.get("message").getAsJsonArray();
            Log.d("LOG", "CHATRESPONSEDESEREALIZER: deserialize | chats: "+chats.toString());
            ArrayList<Chat> chat_list = new ArrayList<>();
            if (chats.size()!=0){
                for(JsonElement jchat: chats){
                    chat_list.add((Chat)context.deserialize(jchat, Chat.class));
                }
            }
            cr.setChats(chat_list);
        } else {
            cr.setStatus(jsonObject.get("message").getAsString());
        }
        return cr;
    }
}