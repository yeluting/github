package cn.springmvc.dao;

import cn.springmvc.model.CommitParents;

public interface CommitParentsMapper {
    int insert(CommitParents record);

    int insertSelective(CommitParents record);
}