package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface IntimacyMapper {

    ArrayList<Integer> getSize();

    void updateIntimacy(@Param("value1") int value1, @Param("value2") int value2);

    LinkedList<Integer> getProjectId();

    List<Integer> getMembersByPid(@Param("project_id") int project_id);

    Double getPairIntimacy(@Param("userA") int userA, @Param("userB") int userB);

    void insertTeamIntimacy(List<Map<String, Object>> intimacy);

}
