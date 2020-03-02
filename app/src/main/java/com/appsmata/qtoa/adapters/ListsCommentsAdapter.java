package com.appsmata.qtoa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsmata.qtoa.models.*;
import com.balysv.materialripple.MaterialRippleLayout;
import com.appsmata.qtoa.R;
import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;

import java.util.ArrayList;
import java.util.List;

public class ListsCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PostModel> thisPost;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    private final Context context;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel thisPost);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsCommentsAdapter(List<PostModel> thisPost, Context context){
        this.thisPost = thisPost;
        this.context = context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_answers, parent, false);
            viewHolder = new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false);
            viewHolder = new MyViewHolder(view);
        }
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PostModel listItem = thisPost.get(position);
        MyViewHolder view = (MyViewHolder) holder;
        String[] MyTags = TextUtils.split(listItem.tags, ",");
        String ItemTags = "";
        String details = listItem.what  + " " + listItem.when + " " + listItem.where + " " + listItem.who;

        for (int i = 0; i<MyTags.length; i++){
            //ItemTags = ItemTags + "<span style='background: " + getResources().getColor(R.color.brown) + "'>" + MyTags[i] + "</span> ";
            ItemTags = ItemTags + "<span style='background: #e67e22;padding: 2px 8px;'><b>" + MyTags[i] + "</b></span>, ";
        }

        view.post_title.setText(listItem.title);
        view.post_netvotes.setText(listItem.netvotes);
        view.post_acount.setText(listItem.acount);
        view.post_details.setText(Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT));
        view.post_tags.setText(Html.fromHtml(ItemTags, Html.FROM_HTML_MODE_COMPACT));

        view.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, listItem);
                }
            }
        });
        Log.d("MyAdapter", "position: " + position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MaterialRippleLayout layout_parent;
        public TextView post_title, post_netvotes, post_acount, post_details, post_tags;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_parent = itemView.findViewById(R.id.material_ripple);
            post_title = itemView.findViewById(R.id.post_title);
            post_netvotes = itemView.findViewById(R.id.post_netvotes);
            post_acount = itemView.findViewById(R.id.post_acount);
            post_details = itemView.findViewById(R.id.post_details);
            post_tags = itemView.findViewById(R.id.post_tags);
        }
    }

    @Override
    public int getItemCount() {
        return thisPost.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.thisPost.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void resetData(){
        this.thisPost = new ArrayList<PostModel>();
        notifyDataSetChanged();
    }

    public void insertData(List<PostModel> thisPost){
        setLoaded();
        int itemStart = getItemCount();
        int itemCount = thisPost.size();
        this.thisPost.addAll(thisPost);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int pl = 0; pl<getItemCount(); pl++){
            if (thisPost.get(pl) == null){
                thisPost.remove(pl);
                notifyItemRemoved(pl);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.thisPost.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    private void lastPostsView(RecyclerView recyclerView){
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPosts = layoutManager.findLastVisibleItemPosition();
                    if (!loading && lastPosts == getItemCount() - 1 && onLoadMoreListener != null){
                        if (onLoadMoreListener != null){
                            int get = getItemCount() / BaseUrlConfig.RequestLoadMore;
                            onLoadMoreListener.onLoadMore(get);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }


}
