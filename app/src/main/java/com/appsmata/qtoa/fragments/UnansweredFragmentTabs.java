package com.appsmata.qtoa.fragments;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.adapters.ListsQuestionsAdapter;
import com.appsmata.qtoa.models.Callback.CallbackPostsLists;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.retrofitconfig.API;
import com.appsmata.qtoa.retrofitconfig.CallJson;
import com.appsmata.qtoa.ui.QtoAndy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnansweredFragmentTabs extends Fragment {
    private String request;
    private View view;
    private ListsQuestionsAdapter recentAdapter;
    private Call<CallbackPostsLists> callbackPostsCall;
    private RecyclerView itemsRecyclerView;

    public UnansweredFragmentTabs() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        request = getArguments().getString("api_request");
        view = inflater.inflate(R.layout.posts_fragment_tabs, container, false);

        if ("selected".equals(request)) {
            loadItemsData("selected");
        } else if ("upvotes".equals(request)) {
            loadItemsData("upvotes");
        } else {
            loadItemsData("recent");
        }

        return view;
    }

    private void loadItemsData(final String sort) {
        itemsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsRecyclerView.setLayoutManager(layoutManager);

        recentAdapter = new ListsQuestionsAdapter(new ArrayList<PostModel>(), getContext());

        recentAdapter.setOnItemClickListener(new ListsQuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PostModel postModel) {
                QtoAndy.passingIntent(getActivity(), postModel.postid, "PostView");
            }
        });

        itemsRecyclerView.setAdapter(recentAdapter);
        //if (CheckNetwork.isConnectCheck(getContext())) requestAction(0, sort);
        requestAction(0, sort);
    }

    private void displayApiResult(final List<PostModel> posts) {
        recentAdapter.insertData(posts);
        if (posts.size() == 0);
    }

    private void requestAction(final int start, final String sort){
        recentAdapter.setLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData(start, sort);
            }
        }, 1000);
    }

    private void requestData(final int start, final String sort) {
        API api = CallJson.callJson();
        //callbackPostsCall = api.PostListing(BaseUrlConfig.RequestLoadMore, start, sort);
        callbackPostsCall = api.PostsLists(0, start, sort);
        callbackPostsCall.enqueue(new Callback<CallbackPostsLists>() {
            @Override
            public void onResponse(Call<CallbackPostsLists> call, Response<CallbackPostsLists> response) {
                CallbackPostsLists callbackPostsLists = response.body();
                if (callbackPostsLists != null){
                    displayApiResult(callbackPostsLists.data);
                }
            }

            @Override
            public void onFailure(Call<CallbackPostsLists> call, Throwable t) {
                if (!call.isCanceled());
            }
        });
    }

}
