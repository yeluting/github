package cn.springmvcGithub.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecommendMapper {

    void saveRecResult(@Param("setId") int set_id, @Param("disId") int dis_id, @Param("recType") int recType,
                       @Param("userId") int userId, @Param("members") String members,
                       @Param("cost") double cost, @Param("diff") double diff, @Param("grow") double grow,
                       @Param("TSR") double teamSuccessRate);

    List<Integer> loadUsers(@Param("setId") int set_id);

    List<Map<String, Object>> loadSkills(@Param("setId") int set_id, @Param("disId") int dis_id);

}
