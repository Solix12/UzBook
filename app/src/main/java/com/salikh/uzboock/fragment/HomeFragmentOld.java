package com.salikh.uzboock.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.salikh.uzboock.R;
import com.salikh.uzboock.adapter.PostAdapter;
import com.salikh.uzboock.adapter.StoryAdapter;
import com.salikh.uzboock.model.Post;
import com.salikh.uzboock.model.Story;
import com.salikh.uzboock.model.UserStories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class HomeFragmentOld extends Fragment {

    private ShimmerRecyclerView storyRv;
    private ShimmerRecyclerView dashBoardRv;
    private ArrayList<Story> storyList;
    private ArrayList<Post> postList;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private RoundedImageView addStoryImage;
    private ActivityResultLauncher<String> galleryLauncher;
    private ProgressDialog dialog;

    public HomeFragmentOld() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dashBoardRv = view.findViewById(R.id.dashboard_list);
        dashBoardRv.showShimmerAdapter();
        storyRv = view.findViewById(R.id.storyRv);
        storyRv.showShimmerAdapter();


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);


        storyRv = view.findViewById(R.id.storyRv);
        storyList = new ArrayList<>();

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        // storyRv.setNestedScrollingEnabled(false);


        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        Story story = storySnapshot.getValue(Story.class);
                        story.setStoryBy(storySnapshot.getKey());
                        story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));
                        ArrayList<UserStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()) {
                            UserStories userStories = snapshot1.getValue(UserStories.class);
                            stories.add(userStories);
                        }
                        story.setStories(stories);
                        storyList.add(story);
                    }
                    Collections.shuffle(storyList);
                    storyRv.setAdapter(adapter);
                    storyRv.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        postList = new ArrayList<>();

        PostAdapter postAdapter = new PostAdapter(getContext(), postList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashBoardRv.setLayoutManager(layoutManager);
        //dashBoardRv.setNestedScrollingEnabled(false);


        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());
                    postList.add(post);
                }
                Collections.shuffle(postList);
                dashBoardRv.setAdapter(postAdapter);
                dashBoardRv.hideShimmerAdapter();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addStoryImage = view.findViewById(R.id.postImg);
        addStoryImage.setOnClickListener(view1 -> {
            galleryLauncher.launch("image/*");
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                addStoryImage.setImageURI(result);

                dialog.show();
                final StorageReference reference = storage.getReference()
                        .child("stories")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(String.valueOf(new Date().getTime()));

                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Story story = new Story();
                                story.setStoryAt(new Date().getTime());

                                database.getReference()
                                        .child("stories")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("postedBy")
                                        .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        UserStories stories = new UserStories(uri.toString(), story.getStoryAt());

                                        database.getReference()
                                                .child("stories")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("userStories")
                                                .push()
                                                .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}