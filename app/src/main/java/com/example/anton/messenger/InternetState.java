package com.example.anton.messenger;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by anton on 22.5.17.
 */

public class InternetState {
    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
