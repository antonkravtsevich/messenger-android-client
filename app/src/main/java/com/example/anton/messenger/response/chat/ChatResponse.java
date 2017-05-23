package com.example.anton.messenger.response.chat;

import com.example.anton.messenger.response.chats.Chat;

/**
 * Created by anton on 22.5.17.
 */

public class ChatResponse {
    private Chat chat;
    private String status;

    public ChatResponse(Chat chat, String status) {
        this.chat = chat;
        this.status = status;
    }

    public ChatResponse() {
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "chat=" + chat +
                ", status='" + status + '\'' +
                '}';
    }
}
