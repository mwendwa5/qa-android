package com.appsmata.qtoa.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.components.SearchDialogCompat;
import com.appsmata.qtoa.models.Callback.*;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.core.*;
import com.appsmata.qtoa.retrofitconfig.API;
import com.appsmata.qtoa.retrofitconfig.CallJson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ask extends AppCompatActivity {

    private static final String TAG = "";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText inputTitle, inputCat, inputContent, inputTags;
    private TextView previewTitle, previewCat, previewContent, previewTags;

    private LinearLayout BasicInputs, PreviewInputs;

    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinator_layout;
    private AlertDialog alertDialog1;

    private Bitmap bitmap;
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    private CategoryModel Category;
    private Call<CallbackCategory> catsCall;

    private PostSingle Posting;
    private Call<CallbackPostSingle> postsCall;

    private String q_categoryid = "", uploadedFile = "";
    private int stage = 1;
    private Uri uri;
    private ArrayList<CategorySearch> categories = new ArrayList<CategorySearch>();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Toolbar toolbar = findViewById(R.id.toolbar);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        BasicInputs = findViewById(R.id.basic_inputs);
        PreviewInputs = findViewById(R.id.basic_previews);

        inputTitle = findViewById(R.id.input_title);
        inputCat = findViewById(R.id.input_category);
        inputContent = findViewById(R.id.input_content);
        inputTags = findViewById(R.id.input_tags);

        previewTitle = findViewById(R.id.preview_title);
        previewCat = findViewById(R.id.preview_category);
        previewContent = findViewById(R.id.preview_content);
        previewTags = findViewById(R.id.preview_tags);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setSupportActionBar(toolbar);
        toolbarSet();
        getDrafts();
        fetchCategories();

        BasicInputs.setVisibility(View.VISIBLE);
        PreviewInputs.setVisibility(View.GONE);

        inputCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categories.size() != 0) categoryDialog();
                else Toast.makeText(Ask.this, "No categories available at the moment", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabdone);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitInput(view);
            }
        });
    }

    private void getDrafts(){
        if (!prefget.getString("q_title", "").isEmpty()) inputTitle.setText(prefget.getString("q_title", ""));
        if (!prefget.getString("q_category", "").isEmpty()) inputCat.setText(prefget.getString("q_category", ""));
        if (!prefget.getString("q_categoryid", "").isEmpty()) q_categoryid = prefget.getString("q_categoryid", "");
        if (!prefget.getString("q_content", "").isEmpty()) inputContent.setText(prefget.getString("q_content", ""));
        if (!prefget.getString("q_tags", "").isEmpty()) inputTags.setText(prefget.getString("q_tags", ""));
    }

    private void SubmitInput(View view){
        String q_title = inputTitle.getText().toString().trim();
        String q_category = inputCat.getText().toString().trim();
        String q_content = inputContent.getText().toString().trim();
        String q_tags = inputTags.getText().toString().trim();

        switch (stage) {
            case 1:
                if (q_title.isEmpty())
                Snackbar.make(view, "Please enter the title of your question!", Snackbar.LENGTH_SHORT).show();
                else if (q_category.isEmpty())
                    Snackbar.make(view, "Please select a category!", Snackbar.LENGTH_SHORT).show();
                else if (q_content.isEmpty())
                    Snackbar.make(view, "Please enter more infromation about your question!", Snackbar.LENGTH_SHORT).show();
                else if (q_tags.isEmpty())
                    Snackbar.make(view, "Please enter at least a tag for your question!", Snackbar.LENGTH_SHORT).show();
                else {
                    previewTitle.setText("Title: " + q_title);
                    previewCat.setText("Category: " + q_category);
                    previewContent.setText("Content: " + q_content);
                    previewTags.setText("Tags: " + q_tags);

                    prefedit.putString("q_title", q_title).apply();
                    prefedit.putString("q_categoryid", q_categoryid).apply();
                    prefedit.putString("q_category", q_category).apply();
                    prefedit.putString("q_content", q_content).apply();
                    prefedit.putString("q_tags", q_tags).apply();

                    BasicInputs.setVisibility(View.GONE);
                    PreviewInputs.setVisibility(View.VISIBLE);

                    actionBar.setSubtitle("Question Details > Preview >");
                    stage = 2;
                }
                Toast.makeText(this, "Tap back to edit anything", Toast.LENGTH_LONG).show();
                break;

            case 2:
                postItemData();
                Toast.makeText(this, "Posting your Question", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void showDialog(final boolean show){
        if (show){
            progressDialog.setTitle("Posting your Question");
            progressDialog.setMessage("Some patience please . . .");
            progressDialog.show();
        }
        else progressDialog.dismiss();
    }

    private void displayApiResult(final List<CategoryModel> cat_results) {
        for (int i = 0; i < cat_results.size(); i++){
            categories.add(new CategorySearch( cat_results.get(i).title, cat_results.get(i).categoryid + ""));
            //Log.d(TAG, cat_results.get(i).title + " added");
        }
    }

    private void categoryDialog(){
        SearchDialogCompat<CategorySearch> dialog = new SearchDialogCompat<CategorySearch>(Ask.this,
                "Select Question Category", "Search for a Category", null, categories, new SearchResultListener<CategorySearch>() {
            @Override
            public void onSelected( BaseSearchDialogCompat dialog, CategorySearch category, int position )
            {
                inputCat.setText(category.getTitle());
                q_categoryid = category.getCatID();
                dialog.dismiss();
            }
        }
        );
        dialog.show();
        dialog.getSearchBox().setTypeface(Typeface.SERIF);
    }

    private void fetchCategories() {
        API api = CallJson.callJson();
        catsCall = api.getCategories();
        catsCall.enqueue(new Callback<CallbackCategory>() {
            @Override
            public void onResponse(Call<CallbackCategory> call, Response<CallbackCategory> response) {
                CallbackCategory callbackCatListing = response.body();
                displayApiResult(callbackCatListing.data);
            }

            @Override
            public void onFailure(Call<CallbackCategory> call, Throwable t) {
                if (!call.isCanceled());
            }
        });
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Ask a Question");
        actionBar.setSubtitle("Question Details >");
    }

    private void postItemData() {
        showDialog(true);
        String title = inputTitle.getText().toString().trim();
        String content = inputContent.getText().toString().trim();
        String tags = inputTags.getText().toString().trim();

        API api = CallJson.callJson();
        postsCall = api.AskNow(prefget.getInt("user_userid", 0), prefget.getString("user_handle", ""), prefget.getString("user_email", ""),
                q_categoryid, title, content, tags);

        postsCall.enqueue(new Callback<CallbackPostSingle>() {
            @Override
            public void onResponse(Call<CallbackPostSingle> call, Response<CallbackPostSingle> response) {
                showDialog(false);
                CallbackPostSingle cl = response.body();
                if (cl != null){
                    Posting = cl.data;
                    if (cl.data.success == 1) finishPosting();
                    else showFeedback("Oops! Posting your question Failed", "Unable to be post at the moment", 0);
                } else showFeedback("Oops! Posting your question Failed", "Unable to be post at the moment", 1);
            }

            @Override
            public void onFailure(Call<CallbackPostSingle> call, Throwable t) {
                showDialog(false);
                showFeedback("Oops! Posting your question Failed", "Your question could not be posted at the moment", 1);
            }

        });
    }

    private void finishPosting(){
        prefedit.putString("q_title", "").apply();
        prefedit.putString("q_category", "").apply();
        prefedit.putString("q_categoryid", "").apply();
        prefedit.putString("q_content", "").apply();
        prefedit.putString("q_tags", "").apply();

        startActivity(new Intent(Ask.this, HomeView.class));
        finish();
    }

    private void showFeedback(String title, String message, int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Okay Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        switch (type) {
            case 1:
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        postItemData();
                        Toast.makeText(Ask.this, "Posting your item", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

        builder.show();
    }

    @Override
    public void onBackPressed() {
        switch (stage) {
            case 2:
                BasicInputs.setVisibility(View.VISIBLE);
                PreviewInputs.setVisibility(View.GONE);
                stage = 1;
                break;

            default:
                super.onBackPressed();
                finish();
                break;
        }
    }
}
