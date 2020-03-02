package com.appsmata.qtoa.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appsmata.qtoa.R;
import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity{

    private View view;
    private EditText inputPasskey;
    private EditText inputPassword;
    private Button buttonLogin;
    private TextView register;
    private Intent intent;
    private ProgressDialog progressDialog;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        inputPasskey = findViewById(R.id.input_passkey);
        inputPassword = findViewById(R.id.input_password);
        buttonLogin = findViewById(R.id.button_Login);
        register = findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String passkey = inputPasskey.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (passkey.isEmpty())
                Snackbar.make(view, "Please input your username or email", Snackbar.LENGTH_SHORT).show();
            else if (password.isEmpty())
                Snackbar.make(view, "Please input your password", Snackbar.LENGTH_SHORT).show();
            else loginUser(passkey, password);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLogin.this, UserRegister.class));
            }
        });
    }

    private void inputChecker(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid User Details");
        builder.setMessage(message);
        builder.setNegativeButton("Okay Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.show();
    }

    private void showFeedback(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logging you Failed");
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String passkey = inputPasskey.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                loginUser(passkey, password);
            }
        });
        builder.show();
    }

    public void loginUser(final String passkey, final String password){
        showDialog();

        StringRequest request = new StringRequest(Request.Method.POST,
                prefget.getString("app_base_url", BaseUrlConfig.BaseUrl) + BaseUrlConfig.UserLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {
                        JSONObject user = jsonObject.getJSONObject("data");

                        prefedit.putBoolean("app_user_loggedin", true);
                        prefedit.putInt("user_userid", Integer.parseInt(user.getString("userid")));
                        //prefedit.putString("user_uniqueid", user.getString("uid"));
                        prefedit.putString("user_email", user.getString("email"));
                        prefedit.putString("user_level", user.getString("level"));
                        prefedit.putString("user_handle", user.getString("handle"));
                        prefedit.putString("user_created", user.getString("created"));
                        prefedit.putString("user_loggedin", user.getString("loggedin"));
                        prefedit.putString("user_avatarblobid", user.getString("avatarblobid"));
                        prefedit.putInt("user_points", Integer.parseInt(user.getString("points")));
                        prefedit.putInt("user_wallposts", Integer.parseInt(user.getString("wallposts")));
                        prefedit.apply();

                        startActivity(new Intent(UserLogin.this, HomeView.class));
                        finish();
                    } else{
                        prefedit.putBoolean("app_user_loggedin", false);
                        prefedit.apply();
                        showFeedback(message);
                        Log.d("test", response);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("emailhandle", passkey);
                param.put("password", password);

                return param;
            }

        };

        //MySingleton.getmInstance(this).addToRequest(request);
    }

    private void showDialog(){
        if (!progressDialog.isShowing()){
            progressDialog.setTitle("Loging you in");
            progressDialog.setMessage("Some patience please. . .");
            progressDialog.show();
        }
    }

    private void hideDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
