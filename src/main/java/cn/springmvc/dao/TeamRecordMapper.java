package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TeamRecordMapper {

    ArrayList<Integer> getProjectId(@Param("table") String table);

    ArrayList<Integer> getCommitter(@Param("project_id") int pid);

    void insertTeams(List<Map<String, Object>> teamrecords);

    void insertMembers(List<Map<String, Object>> memberteams);

}
