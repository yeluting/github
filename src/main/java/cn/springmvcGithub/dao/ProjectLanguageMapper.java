package cn.springmvcGithub.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.springmvcGithub.model.ProjectLanguage;
import org.apache.ibatis.annotations.Param;

public interface ProjectLanguageMapper {
    int insert(ProjectLanguage record);

    int insertSelective(ProjectLanguage record);

    List<Map<String,Object>> getLangsByProject(@Param("project_id") Integer projectId);

    ArrayList<Integer> getAllProjectIds();

    ArrayList<Integer> getProjectIdsFilter1();

    ArrayList<String> getProjectLanBatch(ArrayList<Integer> projects);

    void updateLanguagePercentBatch(ArrayList<ProjectLanguage> toUpdate);

    void updateLanguagePercent(@Param("item") ProjectLanguage item);

    void insertProjectLanguageFilter1(ArrayList<ProjectLanguage> projectLanguages);

    ArrayList<HashMap<Object,Object>> getLanguagePercentTotal();

    void deleteProjectWOLan(@Param("projectId") int projectId);
}