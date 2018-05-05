package cn.springmvc.dao;

import cn.springmvc.model.CommitComment;

public interface CommitCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommitComment record);

    int insertSelective(CommitComment record);

    CommitComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommitComment record);

    int updateByPrimaryKey(CommitComment record);
}