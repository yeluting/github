package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LangAbilityAnaMapper {

    ArrayList<String> selectLang();

    ArrayList<Map<String, Object>> selectLangAbility(@Param("off") int offset, @Param("lim") int limit);

    void saveLangAnalysis(List<Map<String, Object>> output);

    void updateCount(@Param("lang") String lang, @Param("val") int value);

    ArrayList<Double> selectNormedLA(@Param("language") String language);

    void insertNormedDis(@Param("dis") Map<String, int[]> dis);
}
