package com.salikh.uzboock.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salikh.uzboock.R;
import com.salikh.uzboock.adapter.CommentAdapter;
import com.salikh.uzboock.databinding.ActivityCommentBinding;
import com.salikh.uzboock.model.Comment;
import com.salikh.uzboock.model.Notification;
import com.salikh.uzboock.model.Post;
import com.salikh.uzboock.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    private final ArrayList<Comment> list = new ArrayList<>();
    private ActivityCommentBinding binding;
    private Intent intent;
    private String postId, postedBy;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        setBars();


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference()
                .child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get()
                        .load(post.getPostImage())
                        .placeholder(R.drawable.plase_holder)
                        .into(binding.imageView4);

                binding.textView1.setText(post.getPostDescription());
                binding.likePost.setText(String.valueOf(post.getPostLike()));
                binding.commentPost.setText(String.valueOf(post.getCommentCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.defaunt_icon)
                        .into(binding.imageProfileNotification);

                binding.nameProfile.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendBtn.setOnClickListener(view -> {

            Comment comment = new Comment();
            comment.setCommentBody(binding.commentText.getText().toString());
            comment.setCommentedAt(new Date().getTime());
            comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

            database.getReference()
                    .child("posts")
                    .child(postId)
                    .child("comments")
                    .push()
                    .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference()
                            .child("posts")
                            .child(postId)
                            .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int commentCount = 0;
                            if (snapshot.exists()) {
                                commentCount = snapshot.getValue(Integer.class);
                            }
                            database.getReference()
                                    .child("posts")
                                    .child(postId)
                                    .child("commentCount")
                                    .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.commentText.setText("");
                                    Notification notification = new Notification();
                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                    notification.setNotificationAt(new Date().getTime());
                                    notification.setPostID(postId);
                                    notification.setPostedBy(postedBy);
                                    notification.setType("comment");

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("notification")
                                            .child(postedBy)
                                            .push()
                                            .setValue(notification);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        });


        CommentAdapter adapter = new CommentAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.comentRv.setLayoutManager(layoutManager);
        binding.comentRv.setAdapter(adapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    list.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setBars() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }
}