package cn.springmvcGithub.model;

import java.io.Serializable;
import java.util.Date;

public class OrganizationMember extends OrganizationMemberKey implements Serializable {
    private Date createdAt;

    private static final long serialVersionUID = 1L;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}