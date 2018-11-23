package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.OrganizationMember;
import cn.springmvcGithub.model.OrganizationMemberKey;

public interface OrganizationMemberMapper {
    int deleteByPrimaryKey(OrganizationMemberKey key);

    int insert(OrganizationMember record);

    int insertSelective(OrganizationMember record);

    OrganizationMember selectByPrimaryKey(OrganizationMemberKey key);

    int updateByPrimaryKeySelective(OrganizationMember record);

    int updateByPrimaryKey(OrganizationMember record);
}