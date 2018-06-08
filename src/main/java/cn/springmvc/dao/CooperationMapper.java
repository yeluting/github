package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CooperationMapper {

    ArrayList<Integer> selectProjectId_Filter1();

    ArrayList<Integer> selectMembersByProjectId(@Param("project_Id") Integer projectId);

    void insertCooperationBatch(List<Map<String, Integer>> coop);

}
