package com.salikh.uzboock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.StoryRvDesignBinding;
import com.salikh.uzboock.model.Story;
import com.salikh.uzboock.model.User;
import com.salikh.uzboock.model.UserStories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {


    private final ArrayList<Story> list;
    private final Context context;

    public StoryAdapter(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_rv_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Story story = list.get(position);

        if (story.getStories().size() > 0) {
            UserStories lastStory = story.getStories().get(story.getStories().size() - 1);
            Picasso.get()
                    .load(lastStory.getImage())
                    .into(holder.binding.postImg);
            holder.binding.view.setPortionsCount(story.getStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfile())
                            .placeholder(R.drawable.defaunt_icon)
                            .into(holder.binding.imageProfileNotification);
                    holder.binding.storyName.setText(user.getName());

                    holder.binding.postImg.setOnClickListener(view -> {

                        ArrayList<MyStory> myStories = new ArrayList<>();

                        for (UserStories stories : story.getStories()) {
                            myStories.add(new MyStory(
                                    stories.getImage()
                            ));
                        }

                        String photo = user.getProfile();

                        if (photo == null) {
                            photo = String.valueOf(R.drawable.defaunt_icon);
                        } else {
                            photo = user.getProfile();
                        }

                        new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                .setStoriesList(myStories) // Required
                                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                .setTitleText(user.getName()) // Default is Hidden
                                .setSubtitleText("") // Default is Hidden
                                .setTitleLogoUrl(photo)// Default is Hidden
                                .setStoryClickListeners(new StoryClickListeners() {
                                    @Override
                                    public void onDescriptionClickListener(int position) {
                                        //your action
                                    }

                                    @Override
                                    public void onTitleIconClickListener(int position) {
                                        //your action
                                    }
                                }) // Optional Listeners
                                .build() // Must be called before calling show method
                                .show();

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final StoryRvDesignBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = StoryRvDesignBinding.bind(itemView);

        }
    }

}

