package com.salikh.uzboock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.Notification2SeampleBinding;
import com.salikh.uzboock.model.Notification;
import com.salikh.uzboock.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private final Context context;
    private final ArrayList<Notification> list;

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
        notifyItemRangeInserted(this.list.size() - list.size(), list.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_2_seample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification notification = list.get(position);
        String type = notification.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.defaunt_icon)
                                .into(holder.binding.imageProfileNotification);

                        holder.binding.nameNotification.setText(user.getName());

                        if (type.equals("like")) {
                            holder.binding.textOr.setText("liked your post");
                        } else if (type.equals("comment")) {
                            holder.binding.textOr.setText("commented your post");
                        } else {
                            holder.binding.textOr.setText("start following you.");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*FirebaseDatabase.getInstance().getReference()
                        .child("notification")
                        .child(notification.getPostedBy())
                        .child(notification.getNotificationID())
                        .child("checkOpen")
                        .setValue(true);*/


                Intent intent = new Intent(context, CommentAdapter.class);
                intent.putExtra("postId", notification.getPostID());
                intent.putExtra("postedBy", notification.getPostedBy());
                context.startActivity(intent);

            }
        });
        /*Boolean checkOpen = notification.isCheckOpen();
        if (checkOpen == true) {
            // nimadurlar
        }else {
            // nimadur
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final Notification2SeampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = Notification2SeampleBinding.bind(itemView);

        }
    }
}
