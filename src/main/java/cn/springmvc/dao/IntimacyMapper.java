package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface IntimacyMapper {

    Object getSize();

    void updateIntimacy(@Param("limit") int limit, @Param("offset") int offset);

    LinkedList<Integer> getProjectId();

    List<Integer> getMembersByPid(@Param("project_id") int project_id);

    Double getPairIntimacy(@Param("userA") int userA, @Param("userB") int userB);

    void insertTeamIntimacy(List<Map<String, Object>> intimacy);

}
