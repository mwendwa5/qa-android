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
import android.widget.GridLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.appsmata.qtoa.models.*;

import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;
import com.appsmata.qtoa.R;

public class ListsQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PostModel> thisPost;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;
    private final int tagcolor = 0;

    private final Context context;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel thisPost);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ListsQuestionsAdapter(List<PostModel> thisPost, Context context){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_posts, parent, false);
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
        final PostModel Question = thisPost.get(position);
        MyViewHolder view = (MyViewHolder) holder;
        String details = Question.what  + " " + Question.when + " " + Question.where + " " + Question.who;

        view.post_title.setText(Question.title);
        view.post_netvotes.setText(Question.netvotes);
        view.post_acount.setText(Question.acount);
        view.post_details.setText(Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT));

        if (!Question.tags.isEmpty()) {
            String[] MyTags = TextUtils.split(Question.tags, ",");
            for (int i = 0; i < MyTags.length; i++) {
                TextView tagView = new TextView(context);
                tagView.setText(MyTags[i]);
                tagView.setTextColor(context.getResources().getColor(R.color.white_color));
                tagView.setBackground(context.getResources().getDrawable(R.drawable.custom_tag_view));
                tagView.setPadding(20, 5, 20, 5);
                tagView.setTextSize(15);
                view.post_tags.addView(tagView);
            }
        }

        view.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(view, Question);
            }
            }
        });
        Log.d("MyAdapter", "position: " + position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MaterialRippleLayout layout_parent;
        public TextView post_title, post_netvotes, post_acount, post_details;
        public GridLayout post_tags;

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
