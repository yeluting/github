package cn.springmvc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LangAbilityAnaMapper {

    ArrayList<String> selectLang();

    ArrayList<Map<String, Object>> selectLangAbility();

    void saveLangAnalysis(List<Map<String, Object>> output);
}
