package cn.springmvc.dao;

import cn.springmvc.model.Commit;

public interface CommitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commit record);

    int insertSelective(Commit record);

    Commit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commit record);

    int updateByPrimaryKey(Commit record);
}