package com.salikh.uzboock.model;

import com.salikh.uzboock.adapter.helper.HomeData;

import java.util.ArrayList;

public class Story implements HomeData {

    ArrayList<UserStories> stories;
    private String storyBy;
    private long storyAt;


    public Story() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UserStories> stories) {
        this.stories = stories;
    }

    @Override
    public int getType() {
        return HomeData.TYPE_STORY;
    }
}
