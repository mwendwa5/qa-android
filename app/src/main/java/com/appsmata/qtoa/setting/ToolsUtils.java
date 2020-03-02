package com.appsmata.qtoa.setting;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

import com.appsmata.qtoa.ui.CategoryAll;

public class ToolsUtils {

    public static CategoryAll categoryAll;

    public ToolsUtils(Context context){
        this.categoryAll = (CategoryAll) context;
    }

    public static int getFeaturedPostsImageHeight(Activity activity){
        float width_ratio = 2, height_ratio = 1;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels - 10;
        float resHeight = (screenWidth * height_ratio) / width_ratio;
        return Math.round(resHeight);
    }
}
