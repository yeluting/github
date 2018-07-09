package cn.springmvc.service;

import cn.springmvc.dao.ProjectMemberAnaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectMemberAna {

    @Autowired
    private ProjectMemberAnaMapper projectMemberAnaMapper;

    public void calculate(){
        double size = 0.0;
        Map<Integer, Integer> memMap = new HashMap<Integer, Integer>();
        List<Integer> memByPrj = projectMemberAnaMapper.getMemOfPrj();
        for(int count : memByPrj){
            size ++;
            Integer time = memMap.get(count);
            if(time == null) memMap.put(count, 1);
            else memMap.put(count, time + 1);
        }
        System.out.printf("Size : %f\n", size);
        List<Map<String, Object>> output = new ArrayList<Map<String, Object>>();
        for(Map.Entry<Integer, Integer> mem : memMap.entrySet()){
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("member", mem.getKey());
            tmp.put("count", mem.getValue());
            tmp.put("percent", (mem.getValue() / size));
            output.add(tmp);
        }
        System.out.println("Start inserting");
        projectMemberAnaMapper.saveMemPercent(output);
    }

}
