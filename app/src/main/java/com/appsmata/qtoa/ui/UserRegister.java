package com.appsmata.qtoa.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;
import com.appsmata.qtoa.shared.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegister extends AppCompatActivity {

    private CircleImageView user_image;
    private EditText txtEmail, txtHandle, txtPassword;
    private ProgressDialog progressDialog;
    private Button btnRegister, choose_photo, go_profile;
    private PrefManager session;
    private CoordinatorLayout coordinator_layout;
    private int id_userse;
    private String id_users;
    private Bitmap bitmap;
    private TextView login;
    private AlertDialog.Builder builder;
    private AlertDialog dialog, alertDialog1, alertDialog2;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        login = findViewById(R.id.login);
        txtEmail = findViewById(R.id.input_email);
        txtHandle = findViewById(R.id.input_handle);
        txtPassword = findViewById(R.id.input_password);
        coordinator_layout = findViewById(R.id.coordinator_layout);
        btnRegister = findViewById(R.id.button_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String handle = txtHandle.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (handle.isEmpty())
                Snackbar.make(view, "Please enter your handle!", Snackbar.LENGTH_SHORT).show();
            else if (email.isEmpty())
                Snackbar.make(view, "Please enter your email address!", Snackbar.LENGTH_SHORT).show();
            else if (password.isEmpty())
                Snackbar.make(view, "Please enter your password!", Snackbar.LENGTH_SHORT).show();
            else registerUser(handle, email, password);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegister.this, UserLogin.class));
            }
        });
    }

    private void showFeedback(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Failed");
        if (message.isEmpty())
            builder.setMessage("You can try to hit the back button and try to sign in instead to see if your sign up went through or not.");
        else builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(), clickedItem + " deleted completely!", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri file = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                user_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            id_userse = prefget.getInt("Login_user_id_img", 0);
            //uploadPhoto(String.valueOf(id_userse), getBitmap(bitmap));
            Log.d("CheckUpload", String.valueOf(id_userse));
        }
    }

    private void registerUser(final String handle, final String email, final String password) {
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                prefget.getString("app_base_url", BaseUrlConfig.BaseUrl) + BaseUrlConfig.UserRegister, new Response.Listener<String>() {
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

                        startActivity(new Intent(UserRegister.this, HomeView.class));
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
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("handle", handle);
                param.put("email", email);
                param.put("password", password);

                return param;
            }
        };
        stringRequest.setShouldCache(false);
        //MySingleton.getmInstance(this).addToRequest(stringRequest);
    }

    public void doLogin(View view){
        startActivity(new Intent(UserRegister.this, UserLogin.class));
        finish();
    }

    private void showDialog(){
        if (!progressDialog.isShowing()){
            progressDialog.setTitle("Registering you");
            progressDialog.setMessage("Some patience please . . .");
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
        overridePendingTransition(R.anim.animation_right_left, R.anim.animation_blank);
    }
}
