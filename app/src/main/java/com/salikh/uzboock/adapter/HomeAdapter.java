package com.salikh.uzboock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.salikh.uzboock.adapter.helper.HomeData;
import com.salikh.uzboock.adapter.holders.PostHolder;
import com.salikh.uzboock.adapter.holders.StoryHolder;
import com.salikh.uzboock.databinding.DashboardRvSimpleBinding;
import com.salikh.uzboock.databinding.StoryRvDesignBinding;
import com.salikh.uzboock.model.Post;
import com.salikh.uzboock.model.Story;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<HomeData> homeData = new ArrayList<>();
    private final Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public void setHomeData(ArrayList<HomeData> homeData) {
        this.homeData.clear();
        this.homeData.addAll(homeData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HomeData.TYPE_POST) {
            return new PostHolder(DashboardRvSimpleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new StoryHolder(StoryRvDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), context);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == HomeData.TYPE_STORY) {
            StoryHolder storyHolder = (StoryHolder) holder;
            storyHolder.bindData((Story) homeData.get(position));
        } else {
            PostHolder postHolder = (PostHolder) holder;
            postHolder.bindData((Post) homeData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return homeData.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return homeData.size();
    }

}
