package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

public interface LangCompetenceMapper {

    ArrayList<Map<String, Object>> loadSuffixMap();

    ArrayList<Integer> getProjectID(@Param("limit") int limit);

    ArrayList<Integer> getALLProjectID();

    ArrayList<Integer> getCommits(@Param("pid") int project_id, @Param("tno") int table_no);

    String[] getLang(@Param("pid") int project_id);

    ArrayList<Map<String, Object>> getCommitDetail(@Param("commits") ArrayList<Integer> commit_ids);

    void saveMissed(@Param("pid") int project_id, @Param("suffixes") ArrayList<String> suffixes);

    void updateCompetenceByUser(@Param("uid") int author_id, @Param("langComp") Map<String, Integer> langComp);

    ArrayList<Integer> getProjects(@Param("tn") String tn, @Param("cn") String cn);

    void insertContributionByUser(@Param("project_id") int pid, @Param("user_id") int uid,
                                  @Param("langMap") Map<String, Integer> langCon);

}
