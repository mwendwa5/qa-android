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

public class UsersFragment extends Fragment {

    private View view;
    private TabLayout subTabLayout;

    public UsersFragment() { }

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
        bundle1.putString("api_request", "topusers");
        UsersFragmentTabs tab1 = new UsersFragmentTabs();
        tab1.setArguments(bundle1);
        adapter.addFragment(tab1, "topusers");

        /*Bundle bundle2 = new Bundle();
        bundle2.putString("api_request", "newest");
        UsersFragmentTabs tab2 = new UsersFragmentTabs();
        tab2.setArguments(bundle2);
        adapter.addFragment(tab2, "newest");

        Bundle bundle3 = new Bundle();
        bundle3.putString("api_request", "special");
        UsersFragmentTabs tab3 = new UsersFragmentTabs();
        tab3.setArguments(bundle3);
        adapter.addFragment(tab3, "special");*/

        viewPager.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabOne.setText(R.string.users_tab_one);
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_explorer, 0, 0);
        Objects.requireNonNull(subTabLayout.getTabAt(0)).setCustomView(tabOne);

        /*TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabTwo.setText(R.string.users_tab_two);
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star, 0, 0);
        Objects.requireNonNull(subTabLayout.getTabAt(1)).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabThree.setText(R.string.users_tab_three);
        //tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_category, 0, 0);
        Objects.requireNonNull(subTabLayout.getTabAt(2)).setCustomView(tabThree);*/

    }
}
