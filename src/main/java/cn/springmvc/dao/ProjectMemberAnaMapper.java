package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.*;

public interface ProjectMemberAnaMapper {

    ArrayList<Integer> getMemOfPrj();

    void saveMemPercent(List<Map<String, Object>> percents);

    ArrayList<Integer> getProjects();

    HashSet<Integer> getMembers(@Param("table") String table, @Param("field") String filed, @Param("project_id") int project_id);

    void updateResult(@Param("project_id") int project_id, @Param("lost") int lost);
}
