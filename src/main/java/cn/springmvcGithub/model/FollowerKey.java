package cn.springmvcGithub.model;

import java.io.Serializable;

public class FollowerKey implements Serializable {
    private Integer followerId;

    private Integer userId;

    private static final long serialVersionUID = 1L;

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}