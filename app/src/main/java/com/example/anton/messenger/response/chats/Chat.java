package com.example.anton.messenger.response.chats;

import com.example.anton.messenger.response.user.User;

import java.util.ArrayList;

/**
 * Created by anton on 22.5.17.
 */

public class Chat {
    private String _id;
    private String last_date;
    private String __v;
    private ArrayList<Message> messages;
    private User user;

    public Chat(String _id, String last_date, String __v, ArrayList<Message> messages, User user) {
        this._id = _id;
        this.last_date = last_date;
        this.__v = __v;
        this.messages = messages;
        this.user = user;
    }

    public Chat() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {

        String serializedMessages="\n";
        for(Message message: messages){
            serializedMessages+= message.toString()+'\n';
        }

        return "Chat "+_id+" {" +
                "last_date='" + last_date + '\'' +
                ", __v='" + __v + '\'' +
                ", messages=" + serializedMessages +
                ", user=" + user +
                '}';
    }
}
