package cn.springmvc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import cn.springmvc.model.ProjectLanguage;

public interface ProjectLanguageMapper {
    int insert(ProjectLanguage record);

    int insertSelective(ProjectLanguage record);

    List<Map<String,Object>> getLangsByProject(@Param("project_id") Integer projectId);
}