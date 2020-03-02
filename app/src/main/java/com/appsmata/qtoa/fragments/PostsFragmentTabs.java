package com.appsmata.qtoa.fragments;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.adapters.*;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.models.Callback.*;
import com.appsmata.qtoa.retrofitconfig.*;
import com.appsmata.qtoa.ui.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragmentTabs extends Fragment {
    private String request;
    private View view;
    private Call<CallbackPostsLists> callbackPostsCall;
    private ListsQuestionsAdapter recentAdapter;
    private RecyclerView itemsRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int pagesa = 1;
    private boolean isLoading;
    private int pastVisibleItem, visibleIemCount, totalItemCount, previous_total = 0;
    private int view_thresould = 7;

    private ImageView StatusImg;
    private TextView StatusTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        request = getArguments().getString("api_request");
        view = inflater.inflate(R.layout.posts_fragment_tabs, container, false);

        StatusImg = view.findViewById(R.id.status_img);
        StatusTxt = view.findViewById(R.id.status_text);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        itemsRecyclerView = view.findViewById(R.id.posts_recycler_view);

        if ("hot".equals(request)) {
            loadItemsData("hot");
        } else if ("votes".equals(request)) {
            loadItemsData("votes");
        } else if ("answers".equals(request)) {
            loadItemsData("answers");
        } else if ("views".equals(request)) {
            loadItemsData("views");
        } else {
            loadItemsData("created");
        }

        return view;
    }

    private void requestAction(final int page, final int start, final String sort){
        if (page == 1){
            swipeProgress(true);
        }else{
            recentAdapter.setLoading();
        }new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData(page, start, sort);
            }
        }, 1000);
    }

    private void requestData(final int page, final int start, final String sort) {
        API api = CallJson.callJson();
        callbackPostsCall = api.PostsLists(page, start, sort);
        callbackPostsCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                CallbackPostsLists callbackPostsLists = response.body();
                if (callbackPostsLists != null){
                    displayApiResult(callbackPostsLists.data);
                    swipeProgress(false);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                if (!call.isCanceled());
            }
        });
        itemsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        pagination(pagesa, start, sort);
                        isLoading = true;
                    }
                    swipeProgress(false);
                }

            }
        });
    }

    private void pagination(final int page, final int start, final String sort){
        swipeProgress(true);
        API api = CallJson.callJson();
        callbackPostsCall = api.PostsLists(page, start, sort);
        callbackPostsCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                swipeProgress(false);
                CallbackPostsLists callbackPostsLists = response.body();
                if (callbackPostsLists != null) displayApiResult(callbackPostsLists.data);
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                swipeProgress(false);
                if (!call.isCanceled());
            }
        });
    }

    private void loadItemsData(final String sort) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        itemsRecyclerView = view.findViewById(R.id.posts_recycler);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        itemsRecyclerView.setLayoutManager(gridLayoutManager);
        recentAdapter = new ListsQuestionsAdapter(new ArrayList<PostModel>(), getContext());

        itemsRecyclerView.setAdapter(recentAdapter);

        recentAdapter.setOnItemClickListener(new ListsQuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                QtoAndy.passingIntent(getActivity(), postModel.postid, "PostView");
            }
        });

        itemsRecyclerView.setAdapter(recentAdapter);
        //if (CheckNetwork.isConnectCheck(getContext())) requestAction(0, sort);
        requestAction(1, 0, sort);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recentAdapter.resetData();
                requestAction(1, 0, sort);
            }
        });

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

    private void displayApiResult(final List<PostModel> posts) {
        recentAdapter.insertData(posts);
        if (posts.size() == 0){
            StatusTxt.setText("No questions found.\nHelp get things started by asking a question.");
            StatusImg.setVisibility(View.VISIBLE);
            StatusTxt.setVisibility(View.VISIBLE);
            itemsRecyclerView.setVisibility(View.GONE);
        }
    }

}
