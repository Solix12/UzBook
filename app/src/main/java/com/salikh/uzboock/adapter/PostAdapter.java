package com.salikh.uzboock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salikh.uzboock.R;
import com.salikh.uzboock.activity.CommentActivity;
import com.salikh.uzboock.databinding.DashboardRvSimpleBinding;
import com.salikh.uzboock.model.Notification;
import com.salikh.uzboock.model.Post;
import com.salikh.uzboock.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
        Collections.shuffle(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post model = list.get(position);
        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.plase_holder)
                .into(holder.binding.postImg);
        holder.binding.likePost.setText(String.valueOf(model.getPostLike()));
        holder.binding.commentPost.setText(String.valueOf(model.getCommentCount()));
        String description = model.getPostDescription();
        if (description.equals("")) {
            holder.binding.textView5.setVisibility(View.GONE);
        } else {
            holder.binding.textView5.setText(model.getPostDescription());
            holder.binding.textView5.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.defaunt_icon)
                        .into(holder.binding.imageProfileNotification);

                holder.binding.nameDashboard.setText(user.getName());
                holder.binding.tagDashboard.setText(user.getProfession());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.binding.likePost.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);
                } else {
                    holder.binding.likePost.setOnClickListener(view -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("posts")
                                .child(model.getPostId())
                                .child("likes")
                                .child(FirebaseAuth.getInstance().getUid())
                                .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("posts")
                                        .child(model.getPostId())
                                        .child("postLike")
                                        .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostID(model.getPostId());
                                        notification.setPostedBy(model.getPostedBy());
                                        notification.setType("like");


                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(model.getPostedBy())
                                                .push()
                                                .setValue(notification);
                                    }
                                });
                            }
                        });
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.binding.commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", model.getPostId());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final DashboardRvSimpleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvSimpleBinding.bind(itemView);

        }
    }
}
