package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

public interface LangSuffixMapper {

    ArrayList<Map<String, Object>> readRow(@Param("offset") int offset, @Param("limit") int limit);

    void updateLangsuffix(@Param("LangSuffixMap") Map<String, Map<String, Integer>> LangSuffixMap);

}
