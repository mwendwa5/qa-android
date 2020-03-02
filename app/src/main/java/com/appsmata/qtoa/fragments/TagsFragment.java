package com.appsmata.qtoa.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.models.ListingModel;

import java.util.ArrayList;

public class TagsFragment extends Fragment {
    private String request;
    private View view;

    public TagsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.posts_fragment_tabs, container, false);

        RecyclerView itemsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        itemsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsRecyclerView.setLayoutManager(layoutManager);

        ArrayList<ListingModel> mArrayList = new ArrayList<ListingModel>();
        if (getActivity() != null) {
            //mArrayList.add(new ListingModel("", "", "special"));tabOne.setText(R.string.tab_one)
            mArrayList.add(new ListingModel("Most popular tags", "", "normal"));
        }

        //ListingAdapter itemsListingAdapter = new ListingAdapter(mArrayList, getContext());
        //itemsRecyclerView.setAdapter(itemsListingAdapter);

        return view;
    }

    private void RecentItemsRecyclerView() {

    }

}
