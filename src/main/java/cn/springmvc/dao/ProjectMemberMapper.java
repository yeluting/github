package cn.springmvc.dao;

import cn.springmvc.model.ProjectMember;
import cn.springmvc.model.ProjectMemberKey;
import cn.springmvc.service.Ability;

import java.util.ArrayList;

public interface ProjectMemberMapper {
    int deleteByPrimaryKey(ProjectMemberKey key);

    int insert(ProjectMember record);

    int insertSelective(ProjectMember record);

    ProjectMember selectByPrimaryKey(ProjectMemberKey key);

    int updateByPrimaryKeySelective(ProjectMember record);

    int updateByPrimaryKey(ProjectMember record);

    ArrayList<Integer> getProjectSFilter1();

    ArrayList<ProjectMember> getProjectMemberBatch(ArrayList<Integer> projectIds);

    void insertProjectMemberFilter1(ArrayList<ProjectMember> projectMembers);
}