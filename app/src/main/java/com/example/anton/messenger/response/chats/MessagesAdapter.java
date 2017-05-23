package com.example.anton.messenger.response.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.anton.messenger.R;
import com.example.anton.messenger.UserData;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by anton on 16.5.17.
 */

public class MessagesAdapter extends ArrayAdapter<Message> {

    private Context context;
    private ArrayList<Message> MessagesList;
    Realm realm;
    UserData userData;
    private String current_name;
    private String companion_name;

    public MessagesAdapter(Context context, ArrayList<Message> messagesList, String _companion_name) {

        super(context, R.layout.message, messagesList);

        this.context = context;
        this.MessagesList = messagesList;
        companion_name = _companion_name;
        realm = Realm.getDefaultInstance();
        userData = realm.where(UserData.class).findFirst();

        current_name = userData.getUsername();
        companion_name = _companion_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Get rowView from inflater
        View rowView = inflater.inflate(R.layout.message, parent, false);

        //Get the two text view from the rowView
        TextView usernameText = (TextView) rowView.findViewById(R.id.username);
        TextView dateText = (TextView) rowView.findViewById(R.id.date);
        TextView textText = (TextView) rowView.findViewById(R.id.text);

        //Set the text for textView
        String username = "";
        if(userData.get_id().compareTo(MessagesList.get(position).getSender_id())==0){
            username = current_name;
        } else {
            username = companion_name;
        }
        usernameText.setText(username);
        String[] datesp = MessagesList.get(position).getDate().split("[\\p{P} \\t\\n\\r]");
        dateText.setText(datesp[3]+":"+datesp[4]+":"+datesp[5]);
        textText.setText(MessagesList.get(position).getText());
        return rowView;
    }
}
