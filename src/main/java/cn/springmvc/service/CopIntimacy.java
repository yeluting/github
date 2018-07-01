package cn.springmvc.service;

import cn.springmvc.dao.CopIntimacyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CopIntimacy {

    @Autowired
    private CopIntimacyMapper copIntimacyMapper;

    public void calculate(int batchsize){
        Map<Integer, List<Integer>> updateValues = new HashMap<Integer, List<Integer>>();
        LinkedList<Integer> users = copIntimacyMapper.getUserId();
        System.out.printf("Get %d users.\n", users.size());
        while(!users.isEmpty()){
            List<Integer> userlist = new ArrayList<Integer>();
            for(int i = 0; i < batchsize && !users.isEmpty(); i++)
                userlist.add(users.poll());
            List<Map<String, Object>> values = copIntimacyMapper.getTeamProejct(userlist);
            for(Map<String, Object> value : values){
                int v = (Integer) value.get("pros");
                int u = (Integer) value.get("userA");
                List<Integer> pl = updateValues.get(v);
                if(pl == null){
                    pl = new ArrayList<Integer>();
                    pl.add(u);
                    updateValues.put(v, pl);
                }else{
                    pl.add(u);
                }
            }
        }
        System.out.println("Start Update.");
        System.out.printf("size : %d\n", updateValues.size());
        int size = updateValues.size();
        for(Map.Entry<Integer, List<Integer>> value : updateValues.entrySet()){
            copIntimacyMapper.updateCop(value.getKey(), value.getValue());
            size --;
            if(size % 10 == 0)System.out.println(size);
        }
    }

}
