package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.Follower;
import cn.springmvcGithub.model.FollowerKey;

public interface FollowerMapper {
    int deleteByPrimaryKey(FollowerKey key);

    int insert(Follower record);

    int insertSelective(Follower record);

    Follower selectByPrimaryKey(FollowerKey key);

    int updateByPrimaryKeySelective(Follower record);

    int updateByPrimaryKey(Follower record);
}