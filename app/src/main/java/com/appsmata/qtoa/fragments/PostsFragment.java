package com.appsmata.qtoa.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.adapters.ViewPagerAdapter;
import com.appsmata.qtoa.components.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class PostsFragment extends Fragment {

    private View view;
    private TabLayout subTabLayout;

    public PostsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.posts_fragment, container, false);

        CustomViewPager viewPager = view.findViewById(R.id.homeViewPager);
        setupViewPager(viewPager);

        subTabLayout = view.findViewById(R.id.sub_tabs);
        subTabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    @SuppressLint("NewApi")
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());

        Bundle bundle1 = new Bundle();
        bundle1.putString("api_request", "recent");
        PostsFragmentTabs tab1 = new PostsFragmentTabs();
        tab1.setArguments(bundle1);
        adapter.addFragment(tab1, "recent");

        Bundle bundle2 = new Bundle();
        bundle2.putString("api_request", "hot");
        PostsFragmentTabs tab2 = new PostsFragmentTabs();
        tab2.setArguments(bundle2);
        adapter.addFragment(tab2, "hot");

        Bundle bundle3 = new Bundle();
        bundle3.putString("api_request", "votes");
        PostsFragmentTabs tab3 = new PostsFragmentTabs();
        tab3.setArguments(bundle3);
        adapter.addFragment(tab3, "votes");

        Bundle bundle4 = new Bundle();
        bundle4.putString("api_request", "answers");
        PostsFragmentTabs tab4 = new PostsFragmentTabs();
        tab4.setArguments(bundle4);
        adapter.addFragment(tab4, "answers");

        Bundle bundle5 = new Bundle();
        bundle5.putString("api_request", "views");
        PostsFragmentTabs tab5 = new PostsFragmentTabs();
        tab5.setArguments(bundle5);
        adapter.addFragment(tab5, "views");

        viewPager.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabOne.setText(R.string.quiz_tab_one);
        Objects.requireNonNull(subTabLayout.getTabAt(0)).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabTwo.setText(R.string.quiz_tab_two);
        Objects.requireNonNull(subTabLayout.getTabAt(1)).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabThree.setText(R.string.quiz_tab_three);
        Objects.requireNonNull(subTabLayout.getTabAt(2)).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabFour.setText(R.string.quiz_tab_four);
        Objects.requireNonNull(subTabLayout.getTabAt(3)).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabFive.setText(R.string.quiz_tab_five);
        Objects.requireNonNull(subTabLayout.getTabAt(4)).setCustomView(tabFive);
    }
}
