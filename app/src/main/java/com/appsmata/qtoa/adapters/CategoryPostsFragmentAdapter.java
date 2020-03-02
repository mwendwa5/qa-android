package com.appsmata.qtoa.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.R;
import com.appsmata.qtoa.models.*;

public class CategoryPostsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Category> categories;
    private ArrayList<String> imgPath;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, Category category);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryPostsFragmentAdapter(Context context, List<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final Category category = categories.get(position);
            //Glide.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+category.image).into(viewHolder.imgBig);
            viewHolder.textCategory.setText(category.title);
            viewHolder.material_ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view, category);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPosts(List<Category> posts){
        this.categories = posts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgBig;
        public TextView textCategory;
        public MaterialRippleLayout material_ripple;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBig = itemView.findViewById(R.id.imgBig);
            textCategory = itemView.findViewById(R.id.textCategory);
            material_ripple = itemView.findViewById(R.id.material_ripple);
        }
    }
}
