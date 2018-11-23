package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.CommitParents;

public interface CommitParentsMapper {
    int insert(CommitParents record);

    int insertSelective(CommitParents record);
}