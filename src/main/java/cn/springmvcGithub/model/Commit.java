package cn.springmvcGithub.model;

import java.io.Serializable;
import java.util.Date;

public class Commit implements Serializable {
    private Integer id;

    private String sha;

    private Integer authorId;

    private Integer committerId;

    private Integer projectId;

    private Date createdAt;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha == null ? null : sha.trim();
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getCommitterId() {
        return committerId;
    }

    public void setCommitterId(Integer committerId) {
        this.committerId = committerId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}