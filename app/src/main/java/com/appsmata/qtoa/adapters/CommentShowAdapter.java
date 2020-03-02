package com.appsmata.qtoa.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.appsmata.qtoa.models.*;
import com.appsmata.qtoa.retrofitconfig.*;
import com.appsmata.qtoa.R;

public class CommentShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comment> comments;
    private int ItemView = 1;
    private int ItemLoading = 0;
    private boolean loading = false;
    private SharedPreferences prefget;
    private SharedPreferences.Editor prefedit;

    public CommentShowAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;

        prefget = PreferenceManager.getDefaultSharedPreferences(context);
        prefedit = prefget.edit();

    }

    public class ViewHolderComment extends RecyclerView.ViewHolder{

        public CircleImageView user_image;
        public TextView user_name, user_comment, user_date;

        public ViewHolderComment(View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_comment = itemView.findViewById(R.id.user_comment);
            user_date = itemView.findViewById(R.id.user_date);
        }
    }

    public static class ProgressBarCustom extends RecyclerView.ViewHolder{

        public ProgressBar progressBar;

        public ProgressBarCustom(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_loading);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == ItemView){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_show_comment, parent, false);
            vh = new ViewHolderComment(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false);
            vh = new ProgressBarCustom(view);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderComment){
            Comment comment = comments.get(position);
            ViewHolderComment vhc = (ViewHolderComment) holder;

            //Picasso.with(context).load(prefget.getString("app_base_url", BaseUrlConfig.BaseUrl)+BaseUrlConfig.BaseUrl_IMAGE+comment.profile_image).into(vhc.user_image);
            vhc.user_name.setText(comment.name);
            vhc.user_comment.setText(comment.comment);
            vhc.user_date.setText(comment.comment_date);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.comments.get(position) != null ? ItemView : ItemLoading;
    }

    public void insertComment(List<Comment> comments){
        setLoaded();
        int start = getItemCount();
        int finish = comments.size();
        this.comments.addAll(comments);
        notifyItemRangeChanged(start, finish);

    }

    public void setLoaded() {
        loading = false;
        for (int k = 0; k < getItemCount(); k++){
            if (comments.get(k) == null){
                comments.remove(k);
                notifyItemRemoved(k);
            }
        }
    }
}
