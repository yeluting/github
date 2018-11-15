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

    List<Map<String, Object>> getTeamRecord();

    List<String> getProjectLang(@Param("project_id") int project_id);

    List<Map<String, Object>> getLangAbility(@Param("author_id") int author_id, @Param("langs") List<String> langs);

    void updateGrowDiff(@Param("member_id") int member_id, @Param("project_id") int project_id, @Param("grow") double grow, @Param("diff") double diff);

    void updateCost(@Param("project_id") int project_id, @Param("member_id") int member_id, @Param("cost") double cost);

    List<Integer> getTeamRecordAnalysisProject();
}
