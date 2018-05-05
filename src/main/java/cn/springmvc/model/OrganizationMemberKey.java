package cn.springmvc.model;

import java.io.Serializable;

public class OrganizationMemberKey implements Serializable {
    private Integer orgId;

    private Integer userId;

    private static final long serialVersionUID = 1L;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}