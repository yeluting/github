package cn.springmvcGithub.model;

import java.io.Serializable;

public class CommitParents implements Serializable {
    private Integer commitId;

    private Integer parentId;

    private static final long serialVersionUID = 1L;

    public Integer getCommitId() {
        return commitId;
    }

    public void setCommitId(Integer commitId) {
        this.commitId = commitId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}