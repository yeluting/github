package cn.springmvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LangGroupMapper {

    List<Integer> getProjects();

    String[] getproLangs(@Param("project_id") int project_id, @Param("limit") int limit);

    void updateGroup(@Param("Langs") String[] Langs, @Param("limit") int limit);

}
