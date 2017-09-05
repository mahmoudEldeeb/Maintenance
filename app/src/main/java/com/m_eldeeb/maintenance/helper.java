package com.m_eldeeb.maintenance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by melde on 8/2/2017.
 */

public class helper {
    Context context;
public helper(Context c){
    this.context=c;
}
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
