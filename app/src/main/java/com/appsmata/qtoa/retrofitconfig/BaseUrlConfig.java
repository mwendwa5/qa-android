package com.appsmata.qtoa.retrofitconfig;

import java.io.IOException;

public class BaseUrlConfig {
    //public static final String BaseUrl = "https://qtoa.appsmata.com/"; // change this url with your base url
    public static final String BaseUrl = "http://192.168.43.16/qtoa/"; // change this url with your base url

    public static final int RequestLoadMore = 10;
    public static final String SqlDbPath = "data/data/com.appsmata.qtoa.android/posts/databases"; //don't change this
    public static final String GooglePlayStoreUrl = "https://play.google.com/store/apps/details?id=";   //don't change this

    public static final String UserRegister = "qa-api/user-register.php";   //don't change this
    public static final String UserLogin = "qa-api/user-login.php";

    public BaseUrlConfig() throws IOException {
    }
}
