package com.appsmata.qtoa.retrofitconfig;

import java.io.IOException;

public class BaseUrlConfig {
    public static final String BaseUrl = "https://qtoa.appsmata.com/"; // change this url with your base url

    public static final String AppToken = "qa-api/insert_token_from_app.php"; // change this url with your base url http://192.168.0.101/com.appsmata.qtoa-posts
    public static final int RequestLoadMore = 10;
    public static final String SqlDbPath = "data/data/com.appsmata.qtoa.android/posts/databases"; //don't change this
    public static final String GooglePlayStoreUrl = "https://play.google.com/store/apps/details?id=";   //don't change this

    //register,Login and comment user
    public static final String BaseUrl_IMAGE = "qa-api/images_users/";  //don't change this
    public static final String UserRegister = "qa-api/user-register.php";   //don't change this
    public static final String UserLogin = "qa-api/user-login.php";   //don't change this
    public static final String UPDATE = "qa-api/users/update.php"; //don't change this
    public static final String UPDATE_IMAGE_USERS = "qa-api/users/images_users/images.php";    //don't change this
    public static final String USER_COMMENT = "qa-api/users/comment.php"; //don't change this

    public BaseUrlConfig() throws IOException {
    }
}
