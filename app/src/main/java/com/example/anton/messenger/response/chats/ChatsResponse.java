package com.example.anton.messenger.response.chats;

import com.example.anton.messenger.response.user.User;

import java.util.ArrayList;

/**
 * Created by anton on 22.5.17.
 */

public class ChatsResponse {
    private String status;
    private ArrayList<Chat> chats;

    public ChatsResponse(String status, ArrayList<Chat> chats) {
        this.status = status;
        this.chats = chats;
    }

    public ChatsResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> res = new ArrayList<>();
        for(Chat chat : chats){
            res.add(chat.getUser());
        }
        return res;
    }

    @Override
    public String toString() {
        String serializedChats = "\n";

        for(Chat chat : chats){
            serializedChats+=chat.toString()+'\n';
        }

        return "ChatsResponse{" +
                "chats=" + serializedChats +
                '}';
    }
}
