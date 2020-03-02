package com.appsmata.qtoa.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.adapters.SubCategoryAdapter;
import com.appsmata.qtoa.models.Callback.CallbackPostsByCategory;
import com.appsmata.qtoa.models.Category;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.retrofitconfig.API;
import com.appsmata.qtoa.retrofitconfig.CallJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.appsmata.qtoa.R;

public class SubCategory extends AppCompatActivity {

    private static final String EXTRA_GET_OBJ = "key.EXTRA_GET_OBJ";

    public static void passingIntent(Activity activity, Category category){
        Intent intent = new Intent(activity, SubCategory.class);
        intent.putExtra(EXTRA_GET_OBJ, category);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.animation_right_left, R.anim.animation_blank);
    }

    private Toolbar toolbar;
    private ActionBar actionBar;
    private Category category;
    private AppBarLayout appBarLayout;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<CallbackPostsByCategory> callbackPostsByCategoryCall;
    private RecyclerView recyclerView;
    private SubCategoryAdapter adapter;
    private int post_total = 0;
    private int failed_page = 0;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        view = findViewById(android.R.id.content);
        category = (Category) getIntent().getSerializableExtra(EXTRA_GET_OBJ);
        component();
        toolbarSet();
        requestData();
    }

    private void toolbarSet() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }

    private void component() {
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new SubCategoryAdapter(this, recyclerView, new ArrayList<PostModel>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel, int position) {
                //PostSinglex.navigateParent(SubCategory.this, postSingle.postid, false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (callbackPostsByCategoryCall != null && callbackPostsByCategoryCall.isExecuted())
                    callbackPostsByCategoryCall.cancel();
                adapter.resetListData();
                requestData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posts_search_no_action, menu);
        menu.findItem(R.id.go_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){
            onBackPressed();
        }else if (item_variable == R.id.go_search){
            startActivity(new Intent(SubCategory.this, SearchView.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_blank, R.anim.animation_left_right);
    }

    private void displaydata(List<PostModel> categories) {
        adapter.insertData(categories);
        if (categories.size() == 0);
    }

    private void requestData() {
        API api = CallJson.callJson();
        callbackPostsByCategoryCall = api.getPostsCategory(category.categoryid);
        callbackPostsByCategoryCall.enqueue(new Callback<CallbackPostsByCategory>() {
            @Override
            public void onResponse(Call<CallbackPostsByCategory> call, Response<CallbackPostsByCategory> response) {
                CallbackPostsByCategory cnbc = response.body();
                if (cnbc != null){
                    displaydata(cnbc.data);
                }else{
                    //null showing
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsByCategory> call, Throwable t) {
                if (!call.isCanceled());
                //null showing
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callbackPostsByCategoryCall != null && callbackPostsByCategoryCall.isExecuted()) {
            callbackPostsByCategoryCall.cancel();
        }
    }

}
