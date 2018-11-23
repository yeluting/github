package cn.springmvcGithub.dao;

import java.util.List;
import java.util.Map;

public interface DataPreLoadMapper {

    List<Map<String, Object>> loadLanguage();

    List<Map<String, Object>> loadCompetence();

    List<Map<String, Object>> loadCoefficient();

    List<Map<String, Object>> loadIntimacy();

}
