package me.prateeksaigal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by arvind on 03/11/17.
 */

public class NetworkUtility {
    /**
     * Checking Internet Connectivity both WIFI and Mobile
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager!=null?connectivityManager.getActiveNetworkInfo():null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
