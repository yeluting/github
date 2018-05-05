package cn.springmvc.dao;

import cn.springmvc.model.Follower;
import cn.springmvc.model.FollowerKey;

public interface FollowerMapper {
    int deleteByPrimaryKey(FollowerKey key);

    int insert(Follower record);

    int insertSelective(Follower record);

    Follower selectByPrimaryKey(FollowerKey key);

    int updateByPrimaryKeySelective(Follower record);

    int updateByPrimaryKey(Follower record);
}