package cn.springmvc.dao;


import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface SocIntimacyMapper {

    LinkedList<Integer> getUserId();

    List<Map<String, Object>> getOrgs(List<Integer> users);

    void updateOrg(@Param("val") Integer value, @Param("users") List<Integer> users);


}
