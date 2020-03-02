package com.appsmata.qtoa.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;
import com.appsmata.qtoa.ui.OfflinePosts;
import com.appsmata.qtoa.R;

public class PostsFavoriteAdapter extends BaseAdapter {

    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;
    private Context context;
    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int i);
    }

    public PostsFavoriteAdapter(Context context){
        this.context = context;

        prefget = PreferenceManager.getDefaultSharedPreferences(context);
        prefedit = prefget.edit();

    }

    @Override
    public int getCount() {
        return OfflinePosts.postid.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null){

            view = LayoutInflater.from(context).inflate(R.layout.card_posts, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.imgHome = (ImageView) view.findViewById(R.id.imgBig);
        viewHolder.textHome = (TextView) view.findViewById(R.id.title);
        viewHolder.description = (TextView) view.findViewById(R.id.shortTitle);
        viewHolder.dateposts = (TextView) view.findViewById(R.id.date_posts);

        //Picasso.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+ OfflinePosts.image.get(i)).into(viewHolder.imgHome);
        viewHolder.textHome.setText("" + OfflinePosts.title.get(i));
        viewHolder.description.setText("" + OfflinePosts.code.get(i));
        viewHolder.dateposts.setText("" + OfflinePosts.created.get(i));
        viewHolder.lyt_parent = (MaterialRippleLayout) view.findViewById(R.id.material_ripple);
        viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, i);
                }
            }
        });

        return view;
    }

    static class ViewHolder{
        ImageView imgHome;
        TextView textHome;
        TextView description;
        TextView dateposts;
        MaterialRippleLayout lyt_parent;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
