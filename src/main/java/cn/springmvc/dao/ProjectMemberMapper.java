package cn.springmvc.dao;

import cn.springmvc.model.ProjectMember;
import cn.springmvc.model.ProjectMemberKey;

public interface ProjectMemberMapper {
    int deleteByPrimaryKey(ProjectMemberKey key);

    int insert(ProjectMember record);

    int insertSelective(ProjectMember record);

    ProjectMember selectByPrimaryKey(ProjectMemberKey key);

    int updateByPrimaryKeySelective(ProjectMember record);

    int updateByPrimaryKey(ProjectMember record);
}