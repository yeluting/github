package cn.springmvcGithub.dao;


import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface SocIntimacyMapper {

    LinkedList<Integer> getUserId();

    List<Map<String, Object>> getOrgs(@Param("uid") int user);

    void updateOrg(@Param("val") Integer value, @Param("uid") int user);

    void updateOrg0(@Param("uid") int user);

}
