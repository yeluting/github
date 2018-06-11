package cn.springmvc.dao;


import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrganizationMapper {

    ArrayList<Integer> selectOrgId();

    ArrayList<Integer> selectMembersByOrgId(@Param("org_Id") Integer orgId);

    void insertOrganizationBatch(List<Map<String, Integer>> orgs);

}
