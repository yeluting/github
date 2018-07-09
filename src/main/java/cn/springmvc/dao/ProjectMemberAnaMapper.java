package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjectMemberAnaMapper {

    List<Integer> getMemOfPrj();

    void saveMemPercent(List<Map<String, Object>> percents);
}
