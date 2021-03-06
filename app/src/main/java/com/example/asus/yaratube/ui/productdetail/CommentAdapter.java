package com.example.asus.yaratube.ui.productdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.yaratube.R;
import com.example.asus.yaratube.data.model.Comment;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public void setComments(List<Comment> comments) {

        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View result = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(result);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.onBind(comments.get(position));
    }

    @Override
    public int getItemCount() {

        return comments == null ? 0 : comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentUser;
        TextView commentText;

        CommentViewHolder(View itemView) {
            super(itemView);

            commentUser = itemView.findViewById(R.id.comment_user);
            commentText = itemView.findViewById(R.id.comment_text);
        }

        void onBind(Comment comment) {

            commentUser.setText(comment.getUser());
            commentText.setText(comment.getCommentText());
        }
    }
}
