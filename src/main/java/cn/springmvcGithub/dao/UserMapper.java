package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<Integer> getUserIdOffLim(@Param("offset") Integer offset, @Param("limit") Integer limit);
}