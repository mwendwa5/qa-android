package com.appsmata.qtoa.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;

public class DatabaseHelpers extends SQLiteOpenHelper {

    public static SQLiteDatabase db;
    private Context context;
    String DB_PATH;

    public DatabaseHelpers(Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
        DB_PATH = BaseUrlConfig.SqlDbPath;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utils.CREATE_CATEGORIES_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_POSTS_TABLE_SQL);
        sqLiteDatabase.execSQL(Utils.CREATE_USERS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_POSTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utils.TBL_USERS);
        onCreate(sqLiteDatabase);
    }

    public void createDatabase(SQLiteDatabase sqLiteDatabase){
        boolean dbExist = checkDatabase();
        db = null;

        if (dbExist){

        }else {
            db = this.getReadableDatabase();
            db.close();
            try{
                copyDatabase();
            }catch (IOException ex){
                throw new Error("Error Copy DB");
            }
        }
    }

    private boolean checkDatabase() {
        File file = new File(DB_PATH + Utils.DATABASE_NAME);
        return file.exists();
    }

    private void copyDatabase() throws IOException{
        InputStream inputStream = context.getAssets().open(Utils.DATABASE_NAME);
        String outFileName = DB_PATH + Utils.DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = inputStream.read(bytes)) > 0){
            outputStream.write(bytes, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void openDatabase(){
        db = getWritableDatabase();
        String path = DB_PATH + Utils.DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public ArrayList<ArrayList<Object>> getPostsAll(){
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(
                    Utils.TBL_POSTS, new String[]{Utils.POSTID, Utils.CATEGORYID, Utils.TYPE, Utils.BASETYPE, Utils.HIDDEN, Utils.CONTENT, Utils.QUEUED, Utils.ACOUNT, Utils.SELCHILDID, Utils.CLOSEDBYID, Utils.UPVOTES, Utils.DOWNVOTES, Utils.NETVOTES, Utils.VIEWS, Utils.HOTNESS, Utils.FLAGCOUNT, Utils.TITLE, Utils.TAGS, Utils.CREATED, Utils.NAME, Utils.CATEGORYNAME, Utils.CATEGORYBACKPATH, Utils.CATEGORYIDS, Utils.USERVOTE, Utils.USERFLAG, Utils.USERFAVORITEQ, Utils.USERID, Utils.COOKIEID, Utils.CREATEIP, Utils.FLAGS, Utils.LEVEL, Utils.EMAIL, Utils.HANDLE, Utils.AVATARBLOBID, Utils.AVATARWIDTH, Utils.AVATARHEIGHT, Utils.ITEMORDER }
                    , null, null, null, null, Utils.POSTID + " ASC"
            );
           
            if (cursor.moveToFirst()){
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));
					dataList.add(cursor.getString(12));
					dataList.add(cursor.getString(13));
					dataList.add(cursor.getString(14));
					dataList.add(cursor.getString(15));
					dataList.add(cursor.getString(16));
					dataList.add(cursor.getString(17));
					dataList.add(cursor.getString(18));
					dataList.add(cursor.getString(19));
					dataList.add(cursor.getString(20));
					dataList.add(cursor.getString(21));
					dataList.add(cursor.getString(22));
					dataList.add(cursor.getString(23));
					dataList.add(cursor.getString(24));
					dataList.add(cursor.getString(25));
					dataList.add(cursor.getString(26));
					dataList.add(cursor.getString(27));
					dataList.add(cursor.getString(28));
					dataList.add(cursor.getString(29));
					dataList.add(cursor.getString(30));
					dataList.add(cursor.getString(31));
					dataList.add(cursor.getString(32));
					dataList.add(cursor.getString(33));
					dataList.add(cursor.getString(34));
					dataList.add(cursor.getString(35));
					dataList.add(cursor.getString(36));

                    dataArray.add(dataList);
                }while (cursor.moveToNext());
            }

            cursor.close();
        }catch (SQLiteException ex){
            ex.printStackTrace();
        }

        return dataArray;
    }

    public ArrayList<ArrayList<Object>> getAllDataOne(int id){
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(
                    Utils.TBL_POSTS, new String[]{Utils.POSTID, Utils.TITLE, Utils.CATEGORYID}
                    , Utils.POSTID+"=?"+id, null, null, null, Utils.POSTID + " ASC"
            );

            if (cursor.moveToFirst()){
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));

                    dataArray.add(dataList);
                }while (cursor.moveToNext());
            }

            cursor.close();
        }catch (SQLiteException ex){
            ex.printStackTrace();
        }

        return dataArray;
    }

    public boolean isDataExist(long id){
        db = this.getReadableDatabase();
        boolean existDatabase = false;

        try{
            Cursor cursor = db.query(Utils.TBL_POSTS, new String[]
                    {Utils.POSTID}, Utils.POSTID +"="+id
            ,null, null, null, null, null);
            if (cursor.getCount() > 0){
                existDatabase = true;
            }

            cursor.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return existDatabase;
    }

    public boolean isPreviousDataExist(){
        boolean exist = false;
        try{
            Cursor cursor = db.query(Utils.TBL_POSTS,
                    new String[]{Utils.POSTID}, null, null, null, null, null);

            if (cursor.getCount() > 0){
                exist = true;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return exist;
    }

    public void addData(long postid, String categoryid, String type, String basetype, String hidden, String content, String queued, String acount, String selchildid, String closedbyid, String upvotes, String downvotes, String netvotes, String views, String hotness, String flagcount, String title, String tags, String created, String name, String categoryname, String categorybackpath, String categoryids, String uservote, String userflag, String userfavoriteq, String userid, String cookieid, String createip, String points, String flags, String level, String email, String handle, String avatarblobid, String avatarwidth, String avatarheight, String itemorder){
        ContentValues cv = new ContentValues();

			cv.put(Utils.POSTID, postid);
			cv.put(Utils.CATEGORYID, categoryid);
			cv.put(Utils.TYPE, type);
			cv.put(Utils.BASETYPE, basetype);
			cv.put(Utils.HIDDEN, hidden);
			cv.put(Utils.QUEUED, queued);
			cv.put(Utils.ACOUNT, acount);
			cv.put(Utils.SELCHILDID, selchildid);
			cv.put(Utils.CLOSEDBYID, closedbyid);
			cv.put(Utils.UPVOTES, upvotes);
			cv.put(Utils.DOWNVOTES, downvotes);
			cv.put(Utils.NETVOTES, netvotes);
			cv.put(Utils.VIEWS, views);
			cv.put(Utils.HOTNESS, hotness);
			cv.put(Utils.FLAGCOUNT, flagcount);
			cv.put(Utils.TITLE, title);
			cv.put(Utils.TAGS, tags);
			cv.put(Utils.CREATED, created);
			cv.put(Utils.NAME, name);
			cv.put(Utils.CATEGORYNAME, categoryname);
			cv.put(Utils.CATEGORYBACKPATH, categorybackpath);
			cv.put(Utils.CATEGORYIDS, categoryids);
			cv.put(Utils.USERVOTE, uservote);
			cv.put(Utils.USERFLAG, userflag);
			cv.put(Utils.USERFAVORITEQ, userfavoriteq);
			cv.put(Utils.USERID, userid);
			cv.put(Utils.COOKIEID, cookieid);
			cv.put(Utils.CREATEIP, createip);
			cv.put(Utils.POINTS, points);
			cv.put(Utils.FLAGS, flags);
			cv.put(Utils.LEVEL, level);
			cv.put(Utils.EMAIL, email);
			cv.put(Utils.HANDLE, handle);
			cv.put(Utils.AVATARBLOBID, avatarblobid);
			cv.put(Utils.AVATARWIDTH, avatarwidth);
			cv.put(Utils.AVATARHEIGHT, avatarheight);
			cv.put(Utils.ITEMORDER, itemorder);

        try{
            db.insert(Utils.TBL_POSTS, null, cv);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void deleteData(long id){
        try {
            db.delete(Utils.TBL_POSTS, Utils.POSTID+"="+id, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void deleteAllData(){
        try {
            db.delete(Utils.TBL_POSTS, null, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void close(){
        db.close();
    }

    public long getUpdateCountWish(){
        db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, Utils.TBL_POSTS);
        db.close();
        return count;
    }
}
