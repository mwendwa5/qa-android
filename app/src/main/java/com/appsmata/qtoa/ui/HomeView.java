package com.appsmata.qtoa.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.appsmata.qtoa.shared.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.appsmata.qtoa.R;
import com.appsmata.qtoa.adapters.*;
import com.appsmata.qtoa.fragments.*;
import com.appsmata.qtoa.components.*;
import com.google.android.material.tabs.TabLayout;

public class HomeView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PrefManager session;
    Intent intent;

    NavigationView navigationView;
    DrawerLayout drawer;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        displayProfile();

        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingSearchView mSearchView = findViewById(R.id.search_view);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fabwrite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeView.this, Ask.class));
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        de.hdodenhof.circleimageview.CircleImageView profileImageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        Glide.with(this).load(R.drawable.usericon).into(profileImageView);

        mSearchView.attachNavigationDrawerToMenuButton(drawer);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void displayProfile() {
        View userControl = navigationView.getHeaderView(0);
        TextView profile_text1 = userControl.findViewById(R.id.profile_text1);
        TextView profile_text2 = userControl.findViewById(R.id.profile_text2);
        LinearLayout user_view = userControl.findViewById(R.id.user_view);

        if (prefget.getInt("user_userid", 0) != 0){
            profile_text1.setText("Hello " + prefget.getString("user_handle", ""));
            profile_text2.setText(prefget.getInt("user_points", 0) + " points");

            user_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeView.this, UserProfile.class));
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem){
            case R.id.nav_updates:

                break;

            case R.id.nav_logout:
                logoutUser();
                break;

            case R.id.nav_settings:

                break;

            case R.id.nav_feedback:

                break;

            case R.id.nav_about:

                break;

            default:

                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser(){
        prefedit.putBoolean("app_user_loggedin", false);
        prefedit.apply();

        prefget.edit().remove("user_userid").apply();
        prefget.edit().remove("user_handle").apply();
        prefget.edit().remove("user_uniqueid").apply();
        prefget.edit().remove("user_email").apply();
        prefget.edit().remove("use_level").apply();
        prefget.edit().remove("user_created").apply();
        prefget.edit().remove("user_loggedin").apply();
        prefget.edit().remove("user_avatarblobid").apply();
        prefget.edit().remove("user_points").apply();
        prefget.edit().remove("user_wallposts").apply();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        startActivity(new Intent(HomeView.this, AppStart.class));
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostsFragment(), "Questions");
        adapter.addFragment(new UnansweredFragment(), "Unanswered");
        adapter.addFragment(new TagsFragment(), "Tags");
        adapter.addFragment(new CategoryFragment(), "Categories");
        adapter.addFragment(new UsersFragment(), "Users");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
