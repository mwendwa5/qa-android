package com.appsmata.qtoa.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.appsmata.qtoa.adapters.PostsFavoriteAdapter;
import com.appsmata.qtoa.data.DatabaseHelpers;
import com.appsmata.qtoa.R;

public class OfflinePosts extends AppCompatActivity{

    TextView not_yet;
    ListView listviewFavorites;
    ProgressBar progressFavorites;
    DatabaseHelpers databaseHelpers;
    PostsFavoriteAdapter postsFavoriteAdapter;
    AlertDialog.Builder builder;
    ArrayList<ArrayList<Object>> getDatas = new ArrayList<ArrayList<Object>>();
    public static final ArrayList<Integer> postid = new ArrayList<Integer>();
    public static final ArrayList<Object> title = new ArrayList<Object>();
    public static final ArrayList<Object> code = new ArrayList<Object>();
    public static final ArrayList<Object> image = new ArrayList<Object>();
    public static final ArrayList<Object> content = new ArrayList<Object>();
    public static final ArrayList<Object> created = new ArrayList<Object>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_favorite);
        not_yet = findViewById(R.id.not_yet);
        listviewFavorites = findViewById(R.id.listviewFavorites);
        progressFavorites = findViewById(R.id.progress_loading);
        databaseHelpers = new DatabaseHelpers(this);
        postsFavoriteAdapter = new PostsFavoriteAdapter(this);

        new getDataFromServer().execute();

        postsFavoriteAdapter.setOnItemClickListener(new PostsFavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getApplicationContext(), Favorited.class);
                intent.putExtra("postid", postid.get(i));
                intent.putExtra("menu_title", (String) title.get(i));
                intent.putExtra("short_title", (String) code.get(i));
                intent.putExtra("menu_image", (String) image.get(i));
                intent.putExtra("description", (String) content.get(i));
                startActivity(intent);
                overridePendingTransition(R.anim.animation_right_left, R.anim.animation_blank);
            }
        });

        toolbarSet();
    }

    private void toolbarSet() {
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("");
    }

//    private void showPopUpDelete(final int id) {
//
//        builder = new AlertDialog.Builder(this);
//        builder.setTitle("Hapus atau Lihat");
//        builder.setMessage("Klik hapus jika ingin menghapus, klik lihat jika ingin melihat");
//        builder.setPositiveButton(R.string.deleted, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                databaseHelpers.deleteData(id);
//                listviewFavorites.invalidateViews();
//                new getDataFromServer().execute();
//            }
//        });
//        builder.setNegativeButton(R.string.view, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.create();
//        builder.show();
//    }

    public void clearData(){
        postid.clear();
        title.clear();
        code.clear();
        image.clear();
        content.clear();
        created.clear();
    }

    private class getDataFromServer extends AsyncTask<Void, Void, Void> {

        getDataFromServer(){
            if (!progressFavorites.isShown()){
                progressFavorites.setVisibility(View.GONE);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getDataFromSql();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (postid.size() > 0){
                listviewFavorites.setAdapter(postsFavoriteAdapter);
            }else {
                not_yet.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getDataFromSql() {

        clearData();
        getDatas = databaseHelpers.getPostsAll();
        for (int o = 0; o<getDatas.size(); o++){
            ArrayList<Object> rows = getDatas.get(o);
            postid.add(Integer.parseInt(rows.get(0).toString()));
            title.add(rows.get(1).toString());
            code.add(rows.get(2).toString());
            image.add(rows.get(3).toString());
            content.add(rows.get(4).toString());
            created.add(rows.get(5).toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_variable = item.getItemId();
        if (item_variable == android.R.id.home){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        new getDataFromServer().execute();
        postsFavoriteAdapter.notifyDataSetChanged();
    }

    private void showPopUp() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_all));
        builder.setMessage(getString(R.string.message_delete_all));
        builder.setPositiveButton(R.string.deleted, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelpers.deleteAllData();
                listviewFavorites.invalidateViews();
                new getDataFromServer().execute();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OfflinePosts.this, CategoryAll.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_left_right, R.anim.animation_blank);
    }
}
