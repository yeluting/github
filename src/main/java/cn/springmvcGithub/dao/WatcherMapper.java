package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.Watcher;
import cn.springmvcGithub.model.WatcherKey;

public interface WatcherMapper {
    int deleteByPrimaryKey(WatcherKey key);

    int insert(Watcher record);

    int insertSelective(Watcher record);

    Watcher selectByPrimaryKey(WatcherKey key);

    int updateByPrimaryKeySelective(Watcher record);

    int updateByPrimaryKey(Watcher record);
}