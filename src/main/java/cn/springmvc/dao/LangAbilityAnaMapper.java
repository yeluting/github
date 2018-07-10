package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LangAbilityAnaMapper {

    ArrayList<String> selectLang();

    ArrayList<Map<String, Object>> selectLangAbility(@Param("off") int offset, @Param("lim") int limit);

    void saveLangAnalysis(List<Map<String, Object>> output);

    int getCount(@Param("lang") String lang, @Param("val") double value);

    void updateCount(@Param("lang") String lang, @Param("val") double value);
}
