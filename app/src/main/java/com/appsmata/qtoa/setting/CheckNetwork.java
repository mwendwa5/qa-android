package com.appsmata.qtoa.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class CheckNetwork {
    public static boolean isConnectCheck(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null){
                if (networkInfo.isConnected() || networkInfo.isConnectedOrConnecting()){
                    return true;
                }else {
                    return false;
                }

            }else {
                return false;
            }

        }catch (Exception e){
            return false;
        }
    }
}
