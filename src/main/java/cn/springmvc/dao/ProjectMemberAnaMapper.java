package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProjectMemberAnaMapper {

    ArrayList<Integer> getMemOfPrj();

    void saveMemPercent(List<Map<String, Object>> percents);
}
