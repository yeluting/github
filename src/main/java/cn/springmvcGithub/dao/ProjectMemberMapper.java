package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.ProjectMember;
import cn.springmvcGithub.model.ProjectMemberKey;

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