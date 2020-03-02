package com.appsmata.qtoa.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.adapters.*;
import com.appsmata.qtoa.data.DatabaseHelpers;
import com.appsmata.qtoa.models.Callback.*;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.retrofitconfig.*;
import com.appsmata.qtoa.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Question extends AppCompatActivity {

    private static final String EXT_OBJ_ID = "key.EXTRA_OBJ_ID";
    private static final String EXT_NOTIFICATION_ID = "key.NOTIFICATION.ID";

    private boolean showeditor = false, isanswered = false;
    private int questionid;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private Call<CallbackPostSingle> postsCall;
    private PostSingle Question;
    private Call<CallbackPostsLists> callbackQuestionCall;
    private RecyclerView recyclerView;
    private ListsAnswersAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pagesa = 1;
    private boolean isLoading;
    private int pastVisibleItem, visibleIemCount, totalItemCount, previous_total = 0;
    private int view_thresould = 7;

    private MenuItem wishlist, favourites;
    private DatabaseHelpers databaseHelpers;

    private AppCompatButton btn_answer_now;
    private Button btn_answer_add, btn_cancel;
    private LinearLayout post_details, post_answers;
    private TextView post_title, post_content, post_acount;
    private GridLayout post_meta, post_tags;
    private EditText post_answer;
    private CardView answer_now;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionid = getIntent().getIntExtra(EXT_OBJ_ID, 0);

        post_title = findViewById(R.id.post_title);
        post_meta = findViewById(R.id.post_meta);
        post_content = findViewById(R.id.post_content);
        post_tags = findViewById(R.id.post_tags);
        post_acount = findViewById(R.id.post_acount);

        btn_answer_now = findViewById(R.id.btn_answer_now);
        answer_now = findViewById(R.id.answer_now);
        post_answers = findViewById(R.id.post_answers);
        btn_answer_add = findViewById(R.id.btn_answer_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        post_details = findViewById(R.id.post_details);

        btn_answer_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (showeditor) {
                showeditor = false;
                answer_now.setVisibility(View.GONE);
            }
            else {
                showeditor = true;
                answer_now.setVisibility(View.VISIBLE);
                answer_now.requestFocus();
            }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer_now.setVisibility(View.GONE);
            }
        });

        /*FloatingActionButton fab = findViewById(R.id.fabwrite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        databaseHelpers = new DatabaseHelpers(this);

        toolbarSet();
        adapterSet();
        requestAction(1);
    }

    private void requestAction(final int page){
        if (page == 1){
            swipeProgress(true);
        }else{
            adapter.setLoading();
        }new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestQuestionDetail();
            }
        }, 1000);
    }

    private void adapterSet() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.posts_recycler_view);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ListsAnswersAdapter(this, new ArrayList<PostModel>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListsAnswersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel news) {
                //DetailNews.navigateParent(AllNews.this, news.Menu_ID, false);
            }
        });
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Question View");
    }

    private void displayAnswers(final List<PostModel> answers) {
        adapter.insertData(answers);
        swipeProgress(false);
        if (answers.size() == 0);
    }

    private void requestQuestionDetail() {
        API api = CallJson.callJson();
        postsCall = api.PostsSingle(questionid);
        postsCall.enqueue(new Callback<CallbackPostSingle>() {
            @Override
            public void onResponse(Call<CallbackPostSingle> call, Response<CallbackPostSingle> response) {
                CallbackPostSingle cl = response.body();
                if (cl != null){
                    Question = cl.data;
                    displayData();
                    //swipeRefreshProgress(false);
                    swipeProgress(false);
                }else{
                    //onFailedRequest();
                    if (!call.isCanceled());
                }
            }

            @Override
            public void onFailure(Call<CallbackPostSingle> call, Throwable t) {
                //if (!call.isCanceled()) onFailedRequest();
                if (!call.isCanceled());
            }
        });
    }

    private void requestAnswersData() {
        API api = CallJson.callJson();
        callbackQuestionCall = api.PostsAnswers(questionid, 0, 0);
        callbackQuestionCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                CallbackPostsLists callbackAnswers = response.body();
                if (callbackAnswers != null){
                    displayAnswers(callbackAnswers.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                if (!call.isCanceled());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleIemCount = gridLayoutManager.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                pastVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (dy>0){
                    if (isLoading){
                        if (totalItemCount>previous_total){
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount-visibleIemCount)<=pastVisibleItem+view_thresould){

                        ++pagesa;
                        //pagination(pagesa);
                        //Toast.makeText(getApplicationContext(), getString(R.string.load_more_news), Toast.LENGTH_LONG).show();
                        isLoading = true;
                    }
                    swipeProgress(false);
                }

            }
        });
    }

    @SuppressLint("NewApi")
    private void displayData() {
        actionBar.setTitle(Question.title );
        post_title.setText(Question.title);

        TextView post_what = new TextView(this);
        post_what.setText(Html.fromHtml(Question.what, Html.FROM_HTML_MODE_COMPACT));
        post_what.setTextSize(15);
        post_what.setPadding(20, 5, 20, 5);
        post_meta.addView(post_what);

        TextView post_when = new TextView(this);
        post_when.setText(Html.fromHtml(Question.when, Html.FROM_HTML_MODE_COMPACT));
        post_when.setTextSize(15);
        post_when.setPadding(20, 5, 20, 5);
        post_meta.addView(post_when);

        if (!Question.where.isEmpty()) {
            TextView post_where = new TextView(this);
            post_where.setText(Html.fromHtml(Question.where, Html.FROM_HTML_MODE_COMPACT));
            post_where.setTextSize(15);
            post_where.setPadding(20, 5, 20, 5);
            post_meta.addView(post_where);
        }

        TextView post_who = new TextView(this);
        post_who.setText(Html.fromHtml(Question.who, Html.FROM_HTML_MODE_COMPACT));
        post_who.setTextSize(15);
        post_meta.addView(post_who);

        post_content.setText(Html.fromHtml(Question.content, Html.FROM_HTML_MODE_COMPACT));
        String[] MyTags = TextUtils.split(Question.tags, ",");

        for (int i = 0; i < MyTags.length; i++){
            TextView tagView = new TextView(this);
            tagView.setText(MyTags[i]);
            tagView.setTextColor(getResources().getColor(R.color.white_color));
            tagView.setBackground(getResources().getDrawable(R.drawable.custom_tag_view));
            tagView.setPadding(20, 5, 20, 5);
            tagView.setTextSize(15);
            post_tags.addView(tagView);
        }

        int acount = Integer.parseInt(Question.acount);

        if (acount > 0 )
        {
            post_answers.setVisibility(View.VISIBLE);
            post_acount.setText(acount + " Answer" + (acount == 1 ? "" : "s"));
            requestAnswersData();
        }
        post_details.setVisibility(View.VISIBLE);
    }
    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callbackQuestionCall != null && callbackQuestionCall.isExecuted()){
            callbackQuestionCall.cancel();
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipeRefreshLayout.setRefreshing(show);
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        favourites = menu.findItem(R.id.action_wish);
        wishlist = menu.findItem(R.id.user_comment);


        View view = MenuItemCompat.getActionView(wishlist);

        if (databaseHelpers.isDataExist(questionid)){
            favourites.setIcon(R.drawable.ic_favorite_black_48dp);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(wishlist);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){
            onBackPressed();
        }else if (item_variable == R.id.action_wish){
            try{
                if (databaseHelpers.isDataExist(Question.postid)){
                    databaseHelpers.deleteData(Question.postid);
                    favourites.setIcon(R.drawable.ic_favorite_border_black_24dp);
                }else{
                    //databaseHelpers.addData(postSingle.postid, postSingle.title, postSingle.code, postSingle.category, postSingle.image, postSingle.content, postSingle.created);
                    favourites.setIcon(R.drawable.ic_favorite_black_48dp);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), getString(R.string.please_wait_loading_data), Toast.LENGTH_LONG).show();
            }
        }else if (item_variable == R.id.share){

        }else if (item_variable == R.id.user_comment){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
