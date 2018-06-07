package cn.springmvc.model;

import java.io.Serializable;
import java.util.Date;

//extends ProjectMemberKey implements Serializable
public class ProjectMember {
    private Integer repoId;

    private Integer userId;

    //private static final long serialVersionUID = 1L;

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
    private Date createdAt;

    private String extRefId;

    //private static final long serialVersionUID = 1L;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getExtRefId() {
        return extRefId;
    }

    public void setExtRefId(String extRefId) {
        this.extRefId = extRefId == null ? null : extRefId.trim();
    }
}