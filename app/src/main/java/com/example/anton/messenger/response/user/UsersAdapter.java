package com.example.anton.messenger.response.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.anton.messenger.R;

import java.util.ArrayList;

/**
 * Created by anton on 22.5.17.
 */

public class UsersAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> UsersList;

    public UsersAdapter(Context context, ArrayList<User> usersList) {

        super(context, R.layout.message, usersList);

        this.context = context;
        this.UsersList = usersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Get rowView from inflater
        View rowView = inflater.inflate(R.layout.user, parent, false);

        //Get the two text view from the rowView
        TextView usernameText = (TextView) rowView.findViewById(R.id.username);

        //Set the text for textView
        PersonalData pd = UsersList.get(position).getPd();
        usernameText.setText(pd.getFirstName()+" "+pd.getLastName());

        //retrn rowView
        return rowView;
    }
}