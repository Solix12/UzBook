package com.salikh.uzboock.adapter.holders;

import android.content.Intent;
import android.view.View;

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

import java.util.Date;

public class PostHolder extends RecyclerView.ViewHolder {

    private final DashboardRvSimpleBinding binding;

    public PostHolder(DashboardRvSimpleBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


    public void bindData(Post model) {

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.plase_holder)
                .into(binding.postImg);
        binding.likePost.setText(String.valueOf(model.getPostLike()));
        binding.commentPost.setText(String.valueOf(model.getCommentCount()));
        String description = model.getPostDescription();
        if (description.equals("")) {
            binding.textView5.setVisibility(View.GONE);
        } else {
            binding.textView5.setText(model.getPostDescription());
            binding.textView5.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.defaunt_icon)
                        .into(binding.imageProfileNotification);

                binding.nameDashboard.setText(user.getName());
                binding.tagDashboard.setText(user.getProfession());

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
                    binding.likePost.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_red, 0, 0, 0);
                } else {
                    binding.likePost.setOnClickListener(view -> {
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

        binding.commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), CommentActivity.class);
                intent.putExtra("postId", model.getPostId());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                binding.getRoot().getContext().startActivity(intent);
            }
        });
    }

}