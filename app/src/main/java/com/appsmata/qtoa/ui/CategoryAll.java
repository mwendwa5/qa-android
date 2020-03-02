package com.appsmata.qtoa.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.*;
import com.appsmata.qtoa.fragments.*;
import com.appsmata.qtoa.models.BackgroundDrawer;
import com.appsmata.qtoa.models.Callback.*;
import com.squareup.picasso.*;

import de.hdodenhof.circleimageview.CircleImageView;
import com.appsmata.qtoa.data.DatabaseHelpers;
import com.appsmata.qtoa.retrofitconfig.*;
import com.appsmata.qtoa.shared.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.appsmata.qtoa.R;

public class CategoryAll extends AppCompatActivity{

    private InterstitialAd interstitialAd;
    private AdRequest adRequest;
    NavigationView navigationView;
    DatabaseHelpers db;
    NestedScrollView nestedScrollView;
    CardView cardView;
    ImageButton bt_search;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedStore sharedStore;
    Intent intent;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    int currentItem = 0;
    PrefManager session;
    CircleImageView user_image;
    LinearLayout user_background;
    private retrofit2.Call<CallbackBackgroundDrawer> bgcall;
    private BackgroundDrawer backgroundDrawer;

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_all);

        prefget = PreferenceManager.getDefaultSharedPreferences(this);
        prefedit = prefget.edit();

        initInterface();
        initBindData();
        customFragment();
        session = new PrefManager(getApplicationContext());
        displayProfile();
        if (prefget.getInt("Login_user_id", 0) != 0){
            getImageDrawer();
        }
        /*if (sharedStore.whenLaunch()){
            startActivity(new Intent(CategoryAll.this, GuidePosts.class));
            sharedStore.setFirstLaunch(false);
        }*/
    }

    boolean isCard = false;

    public void cardView(boolean card_bool){
        if (isCard && card_bool || !isCard && !card_bool) return;
        isCard = card_bool;
        int moveY = card_bool ? - (2 * cardView.getHeight()) : 0;
        cardView.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void initInterface(){
        initToolbar();
        initDrawerLayout();
        initNavigation();
        navigationView = findViewById(R.id.nav_view);
        cardView = findViewById(R.id.search_bar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //swipeRefreshLayout.setEnabled(false);
        nestedScrollView = findViewById(R.id.nested_content);
        bt_search = findViewById(R.id.bt_search);
        sharedStore = new SharedStore(this);
        db = new DatabaseHelpers(this);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) cardView(false);
                if (scrollY > oldScrollY) cardView(true);
            }
        });

        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                waitLoadingPosts();
            }
        });*/

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryAll.this, SearchView.class));
            }
        });
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null && toolbar != null){
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };

            drawerLayout.addDrawerListener(toggle);
            drawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    toggle.syncState();
                }
            });
        }
    }

    private void initNavigation(){
        navigationView = findViewById(R.id.nav_view);
        if (navigationView != null){
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    navigationMenu(item);
                    return true;
                }

                private void navigationMenu(MenuItem item){
                    openFragmentMenu(item.getItemId());
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                }
            });
        }
    }

    private void waitLoadingPosts(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customFragment();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void customFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //PostsSliderFragment itemsSliderFragment = new PostsSliderFragment();
        //ft.replace(R.id.content_items_slider, itemsSliderFragment);

        CategoryPostsFragment cnf = new CategoryPostsFragment();
        ft.replace(R.id.content_category_posts, cnf);
        ft.commit();
    }

    private void initBindData(){
        initBindMenu();
    }

    public void initBindMenu(){
        if (prefget.getInt("Login_user_id", 0) != 0){
            navigationView.getMenu().setGroupVisible(R.id.group_after_Login, true);
            navigationView.getMenu().setGroupVisible(R.id.group_before_Login, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.group_before_Login, true);
            navigationView.getMenu().setGroupVisible(R.id.group_after_Login, false);
        }
    }

    private void displayProfile() {
        View userControl = navigationView.getHeaderView(0);
        user_background = userControl.findViewById(R.id.user_background);
        user_image = userControl.findViewById(R.id.user_image);
        TextView user_name = userControl.findViewById(R.id.user_name);
        TextView user_email = userControl.findViewById(R.id.user_email);
        if (prefget.getInt("Login_user_id", 0) != 0){
            String user_name_display = prefget.getString("Login_name", "");
            String user_email_display = prefget.getString("Login_email", "");
            user_email.setVisibility(View.VISIBLE);
            user_name.setText(getString(R.string.hello) + user_name_display);
            user_email.setText(user_email_display);
            user_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CategoryAll.this, UserProfile.class));
                }
            });

            String getUrl = prefget.getString("Login_profile_image", "");
            //Picasso.with(getApplicationContext()).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+BaseUrlConfig.BaseUrl_IMAGE+getUrl).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(user_image);
            getImageDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.exit))
                    .setIcon(R.drawable.appicon)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            loadAdInterstial();
            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showInterstial();
                }
            });
        }
        return true;
    }

    private void loadAdInterstial(){
        adRequest = new AdRequest.Builder().build();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ads));
        interstitialAd.loadAd(adRequest);
    }

    private void showInterstial(){
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    private void updateFragment(Intent activity){
        startActivity(activity);
    }

    private void whenUserLogout(){
        prefget.edit().remove("Login_user_id").apply();
        prefget.edit().remove("Login_unique_id").apply();
        prefget.edit().remove("Login_name").apply();
        prefget.edit().remove("Login_email").apply();
        prefget.edit().remove("Login_created_at").apply();
        prefget.edit().remove("Login_profile_image").apply();
        prefget.edit().remove("Login_name_display").apply();
        prefget.edit().remove("Login_user_id_img").apply();
        initBindData();
        openFragmentMenu(R.id.home);
    }

    public void openFragmentMenu(int menuItem){
        switch (menuItem){
            case R.id.home:
            case R.id.home_Login:
                intent = new Intent(CategoryAll.this, CategoryAll.class);
                break;
            case R.id.category:
            case R.id.category_Login:
                intent = new Intent(CategoryAll.this, HomeView.class);
                finish();
                break;
            case R.id.offline:
            case R.id.offline_Login:
                intent = new Intent(CategoryAll.this, OfflinePosts.class);
                finish();
                break;
            case R.id.feedback:
            case R.id.feedback_Login:
                intent = new Intent(CategoryAll.this, Feedback.class);
                finish();
                break;
            case R.id.nav_profile:
            case R.id.nav_profile_Login:
                if (prefget.getInt("Login_user_id", 0) != 0){
                    intent = new Intent(CategoryAll.this, UserProfile.class);
                    finish();
                }else{
                    intent = new Intent(CategoryAll.this, UserLogin.class);
                    finish();
                }
                break;
            case R.id.signout:
                session.setLogin(false);
                whenUserLogout();
                break;
            default:
                break;
        }

        if (currentItem != menuItem && menuItem != R.id.signout){
            currentItem = menuItem;
            updateFragment(intent);
            try{
                navigationView.getMenu().findItem(menuItem).setChecked(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getImageDrawer(){
        API api = CallJson.callJson();
        bgcall = api.getImageDrawer();
        bgcall.enqueue(new Callback<CallbackBackgroundDrawer>() {
            @Override
            public void onResponse(Call<CallbackBackgroundDrawer> call, Response<CallbackBackgroundDrawer> response) {
                CallbackBackgroundDrawer cbd = response.body();
                if (cbd != null){
                    backgroundDrawer = cbd.drawer;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*Picasso.with(getApplicationContext()).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+backgroundDrawer.background_image).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    user_background.setBackground(new BitmapDrawable(getResources(), bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    user_background.setBackgroundResource(R.drawable.whenloading);
                                }
                            });*/
                        }
                    }, 10);
                }
            }

            @Override
            public void onFailure(Call<CallbackBackgroundDrawer> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCount(navigationView);
        if (prefget.getInt("Login_user_id", 0) != 0){
            String getUrl = prefget.getString("Login_profile_image", "");
            //Picasso.with(getApplicationContext()).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+BaseUrlConfig.BaseUrl_IMAGE+getUrl).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(user_image);
            getImageDrawer();
        }
    }

    public void updateCount(NavigationView view){
        Menu menu = view.getMenu();
        long wish_list = db.getUpdateCountWish();
        ((TextView) menu.findItem(R.id.offline).getActionView().findViewById(R.id.counter_wish)).setText(String.valueOf(wish_list));
    }
}
