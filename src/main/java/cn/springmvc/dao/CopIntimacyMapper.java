package cn.springmvc.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface CopIntimacyMapper {

    LinkedList<Integer> getUserId();

    List<Map<String, Long>> getTeamProejct(List<Integer> users);
}
