package cn.springmvc.model;

import java.io.Serializable;

public class ProjectMemberKey implements Serializable {
    private Integer repoId;

    private Integer userId;

    private static final long serialVersionUID = 1L;

    public Integer getRepoId() {
        return repoId;
    }

    public void setRepoId(Integer repoId) {
        this.repoId = repoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}