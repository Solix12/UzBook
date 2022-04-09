package com.salikh.uzboock.model;

import com.salikh.uzboock.adapter.helper.HomeData;

public class Post implements HomeData {

    private String postId;
    private String postImage;
    private String postedBy;
    private String postDescription;
    private long postAt;
    private int postLike;
    private int commentCount;

    public Post(String postId, String postImage, String postedBy, String postDescription, long postAt) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postAt = postAt;
    }

    public Post() {
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostAt() {
        return postAt;
    }

    public void setPostAt(long postAt) {
        this.postAt = postAt;
    }

    @Override
    public int getType() {
        return HomeData.TYPE_POST;
    }
}
