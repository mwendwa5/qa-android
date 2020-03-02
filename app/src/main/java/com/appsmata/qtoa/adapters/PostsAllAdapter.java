package com.appsmata.qtoa.adapters;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.appsmata.qtoa.models.*;

import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.retrofitconfig.BaseUrlConfig;
import com.appsmata.qtoa.R;

public class PostsAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<PostModel> postModelList;
    public OnItemClickListener onItemClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public boolean loading;
    private int VIEW_PROGRES = 0;
    private int VIEW_ITEM = 1;

    public interface OnItemClickListener{
        void onItemClick(View view, PostModel postModel);
    }

    public PostsAllAdapter(Activity activity, List<PostModel> postModelList){
        this.activity = activity;
        this.postModelList = postModelList;
    }

    public class SourceView extends RecyclerView.ViewHolder {

        public MaterialRippleLayout lyt_parent;
        public ImageView imgsubCategory;
        public TextView title;
        public TextView shortTitle;
        public TextView date_posts;

        public SourceView(View itemView) {
            super(itemView);
            lyt_parent = itemView.findViewById(R.id.material_ripple);
            imgsubCategory = itemView.findViewById(R.id.imgBig);
            title = itemView.findViewById(R.id.title);
            shortTitle = itemView.findViewById(R.id.shortTitle);
            date_posts = itemView.findViewById(R.id.date_posts);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_loading);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_posts, parent, false);
            viewHolder = new SourceView(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false);
            viewHolder = new SourceView(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SourceView){
            final PostModel postModel = postModelList.get(position);
            SourceView sourceView = (SourceView) holder;
            //Picasso.with(activity).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+ postSingle.image).into(sourceView.imgsubCategory);
            sourceView.title.setText(postModel.title);
            //sourceView.shortTitle.setText(postModel.handle);
            //sourceView.date_posts.setText(postModel.created);
            sourceView.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view, postModel);
                    }
                }
            });
        }else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.postModelList.get(position) != null ? VIEW_ITEM : VIEW_PROGRES;
    }

    public void resetData(){
        this.postModelList = new ArrayList<PostModel>();
        notifyDataSetChanged();
    }

    public void insertData(List<PostModel> postModelList){
//        setLoaded();
        int itemStart = getItemCount();
        int itemCount = postModelList.size();
        this.postModelList.addAll(postModelList);
        notifyItemRangeInserted(itemStart, itemCount);
    }

    public void setLoaded(){
        loading = false;
        for (int k = 0; k<getItemCount(); k++){
            if (postModelList.get(k) == null){
                postModelList.remove(k);
                notifyItemRemoved(k);
            }
        }
    }

    public void setLoading(){
        if (getItemCount() != 0){
            this.postModelList.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
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
