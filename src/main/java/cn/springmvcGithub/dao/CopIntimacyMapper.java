package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface CopIntimacyMapper {

    LinkedList<Integer> getUserId();

    List<Map<String, Object>> getTeamProejct(List<Integer> users);

    void updateCop(@Param("val") Integer value, @Param("projects") List<Integer> pids);
}
