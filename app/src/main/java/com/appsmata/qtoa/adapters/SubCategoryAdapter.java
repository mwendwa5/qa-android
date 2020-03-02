package com.appsmata.qtoa.adapters;

import android.content.Context;
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
import com.appsmata.qtoa.shared.SharedStore;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<PostModel> postModelList = new ArrayList<PostModel>();
    private OnItemClickListener onItemClickListener;
    private OnLoadMoreListener onLoadMoreListener;
    private SharedStore sharedStore;
    public int ITEM = 1;
    public int PROGRES = 0;
    private boolean loading;
    public interface OnItemClickListener{
        void onItemClick(View view, PostModel postModel, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public SubCategoryAdapter(Context context, RecyclerView view, List<PostModel> postModelList1) {
        this.context = context;
        this.postModelList = postModelList1;
        sharedStore = new SharedStore(context);
        viewDetector(view);
    }

    public class VHolder extends RecyclerView.ViewHolder{

        public ImageView imgsubCategory;
        public TextView textsubCategory;
        public TextView shortTitlesubCategory;
        public TextView datePosts;
        public MaterialRippleLayout lyt_parent;

        public VHolder(View itemView) {
            super(itemView);
            imgsubCategory = (ImageView) itemView.findViewById(R.id.imgBig);
            textsubCategory = (TextView) itemView.findViewById(R.id.title);
            datePosts = (TextView) itemView.findViewById(R.id.date_posts);
            shortTitlesubCategory = (TextView) itemView.findViewById(R.id.shortTitle);
            lyt_parent = (MaterialRippleLayout) itemView.findViewById(R.id.material_ripple);
        }
    }

    public static class ProgressLoadingData extends RecyclerView.ViewHolder{

        public ProgressBar progressBar;

        public ProgressLoadingData(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_loading);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_posts, parent, false);
            vh = new VHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false);
            vh = new VHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof  VHolder){
            final PostModel postModel = postModelList.get(position);
            VHolder vHolder = (VHolder) holder;
            //Picasso.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+ postSingle.image).into(vHolder.imgsubCategory);
            vHolder.textsubCategory.setText(postModel.title);
            //vHolder.shortTitlesubCategory.setText(postModel.categoryname);
            //vHolder.datePosts.setText(postModel.created);
            vHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view, postModel, position);
                    }
                }
            });
        }else{
            ((ProgressLoadingData) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.postModelList.get(position) != null ? ITEM : PROGRES;
    }

    public void insertData(List<PostModel> itemInsert){
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = itemInsert.size();
        this.postModelList.addAll(itemInsert);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int k = 0; k < getItemCount(); k++){
            if (postModelList.get(k) == null){
                postModelList.remove(k);
                notifyItemRemoved(k);
            }
        }
    }

    public void setLoadingPosts(){
        if (getItemCount() != 0){
            this.postModelList.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData(){
        this.postModelList = new ArrayList<PostModel>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void viewDetector(RecyclerView view){
        if (view.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
            view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int ltPost = layoutManager.findFirstVisibleItemPosition();
                    if (!loading && ltPost == getItemCount() - 1 && onLoadMoreListener != null){
                        if (onLoadMoreListener != null){
                            int cur_pg = getItemCount() / BaseUrlConfig.RequestLoadMore;
                            onLoadMoreListener.onLoadMore(cur_pg);
                        }
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore(int cur_page);
    }
}
