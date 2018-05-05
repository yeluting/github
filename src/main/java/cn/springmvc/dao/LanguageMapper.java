package cn.springmvc.dao;

import java.util.List;
import cn.springmvc.model.Language;

public interface LanguageMapper {
    int deleteByPrimaryKey(String language);

    int insert(Language record);

    int insertSelective(Language record);

    List<String> getLangs();
}