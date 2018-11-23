package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.ProjectCommit;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectCommitMapper {
    int insert(ProjectCommit record);

    int insertSelective(ProjectCommit record);

    List<Map<String, Integer>> getProjectCommitsByUser(@Param("user_id") Integer userId);

}