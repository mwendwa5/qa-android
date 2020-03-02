package com.appsmata.qtoa.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.adapters.CategoryPostsFragmentAdapter;
import com.appsmata.qtoa.models.Callback.CallbackCategory;
import com.appsmata.qtoa.models.Category;
import com.appsmata.qtoa.retrofitconfig.API;
import com.appsmata.qtoa.retrofitconfig.CallJson;
import com.appsmata.qtoa.ui.SubCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPostsFragment extends Fragment {
    private View root_view;
    private RecyclerView recyclerView;
    private CategoryPostsFragmentAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_category, null);
        component();
        requestData();
        return root_view;
    }

    private void component() {
        recyclerView = root_view.findViewById(R.id.recyclerViewFragment);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CategoryPostsFragmentAdapter(getActivity(), new ArrayList<Category>());
        adapter.setOnItemClickListener(new CategoryPostsFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Category category) {
                SubCategory.passingIntent(getActivity(), category);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void requestData() {
        final API api = CallJson.callJson();
        api.getCategory().enqueue(new Callback<CallbackCategory>() {
            @Override
            public void onResponse(Call<CallbackCategory> call, Response<CallbackCategory> response) {
                CallbackCategory cl = response.body();
                if (cl != null)
                    recyclerView.setVisibility(View.VISIBLE);
                //adapter.setPosts(cl.data);
            }

            @Override
            public void onFailure(Call<CallbackCategory> call, Throwable t) {

            }
        });
    }
}
