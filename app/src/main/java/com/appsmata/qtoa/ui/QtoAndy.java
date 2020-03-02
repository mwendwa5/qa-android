package com.appsmata.qtoa.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

import com.appsmata.qtoa.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//This a class for doing some user interface stuff required in abstract classes like adapters
public class QtoAndy extends AppCompatActivity {

    private static final String EXT_OBJ_ID = "key.EXTRA_OBJ_ID";
    private static final String EXT_NOTIFICATION_ID = "key.NOTIFICATION.ID";

    public static void passingIntent(Activity activity, Integer postid, String request){
        if ("PostView".equals(request)) {
            Intent intent = new Intent(activity, Question.class);
            intent.putExtra(EXT_OBJ_ID, postid);
            intent.putExtra(EXT_NOTIFICATION_ID, postid);
            activity.startActivity(intent);
            //activity.overridePendingTransition(R.anim.animation_left_right, R.anim.animation_blank);
        }
    }

    public static void ResponseInterceptor()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);

                    // todo deal with the issues the way you need to
                    if (response.code() == 500) {
                        //startActivity(new Intent( ErrorHandlingActivity.this, ServerIsBrokenActivity.class));
                        return response;
                    }
                    return response;
                }
            }).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .client(okHttpClient).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
    }

}
