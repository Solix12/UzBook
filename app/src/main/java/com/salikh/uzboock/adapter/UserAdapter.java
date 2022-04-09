package com.salikh.uzboock.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.UserSeampleBinding;
import com.salikh.uzboock.model.Follow;
import com.salikh.uzboock.model.Notification;
import com.salikh.uzboock.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<User> list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_seample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = list.get(position);
        Picasso.get()
                .load(user.getProfile())
                .placeholder(R.drawable.defaunt_icon)
                .into(holder.binding.imageProfileNotification);

        holder.binding.name.setText(user.getName());
        holder.binding.profession.setText(user.getProfession());

        holder.binding.addBtn.setOnClickListener(view -> {
            Follow follow = new Follow();
            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
            Log.d("taggggg", FirebaseAuth.getInstance().getUid());
            follow.setFollowedAt(new Date().getTime());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(user.getUserID())
                    .child("followers")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(user.getUserID())
                            .child("followerCount")
                            .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            holder.binding.addBtn.setBackgroundResource(R.drawable.follow_active_btn);
                            holder.binding.addBtn.setText("Following");
                            holder.binding.addBtn.setTextColor(context.getResources().getColor(R.color.black));
                            holder.binding.addBtn.setEnabled(false);
                            Toast.makeText(context, "You following " + user.getName(), Toast.LENGTH_SHORT).show();
                            Notification notification = new Notification();
                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notification.setNotificationAt(new Date().getTime());
                            notification.setType("follow");


                            FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                    .child(user.getUserID())
                                    .push()
                                    .setValue(notification);
                        }
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final UserSeampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSeampleBinding.bind(itemView);
        }
    }
}
