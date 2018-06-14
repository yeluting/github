package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import javax.management.openmbean.ArrayType;
import java.util.ArrayList;
import java.util.Map;

public interface LangAbilityMapper {

    ArrayList<Integer> getWatchNum(@Param("repo_Id") int repo_id);

    ArrayList<Integer> selectProjectId_Filter1();

    ArrayList<Map<String, Object>> selectLangPercent(@Param("project_Id") int project_id);

    ArrayList<Map<String, Object>> selectAuthorTime(@Param("project_Id") int project_id);

}
