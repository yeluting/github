package cn.springmvcGithub.model;

import java.io.Serializable;

public class ProjectCommit implements Serializable {
    private Integer projectId;

    private Integer commitId;

    private static final long serialVersionUID = 1L;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCommitId() {
        return commitId;
    }

    public void setCommitId(Integer commitId) {
        this.commitId = commitId;
    }
}