package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

public interface LangFilterMapper {

    ArrayList<Map<String, Object>> loadLangParser();

    ArrayList<Integer> getProjectId();

    ArrayList<String> getLangs(@Param("pId") int pid);
}
