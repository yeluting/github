package cn.springmvcGithub.dao;

import java.util.List;
import cn.springmvcGithub.model.Language;

public interface LanguageMapper {
    int deleteByPrimaryKey(String language);

    int insert(Language record);

    int insertSelective(Language record);

    List<String> getLangs();
}