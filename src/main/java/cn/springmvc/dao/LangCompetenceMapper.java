package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

public interface LangCompetenceMapper {

    ArrayList<Map<String, Object>> loadSuffixMap();

    ArrayList<Integer> getProjectID(@Param("limit") int limit);

    ArrayList<Integer> getProjectID();

    ArrayList<Integer> getCommits(@Param("pid") int project_id, @Param("tno") int table_no);

    String[] getLang(@Param("pid") int project_id);

    ArrayList<Map<String, Object>> getCommitDetail(@Param("commits") ArrayList<Integer> commit_ids);

    void saveMissed(@Param("pid") int project_id, @Param("suffix") String suffix);

    void updateCompetenceByUser(@Param("uid") int author_id, @Param("langComp") Map<String, Integer> langComp);

}
