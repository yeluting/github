package cn.springmvc.service;

import cn.springmvc.dao.CopIntimacyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class CopIntimacy {

    @Autowired
    private CopIntimacyMapper copIntimacyMapper;

    public void calculate(int batchsize){
        Map<Integer, LinkedList<Integer>> updateValues = new HashMap<Integer, LinkedList<Integer>>();
        LinkedList<Integer> users = copIntimacyMapper.getUserId();
        System.out.printf("Get %d users.\n", users.size());
        while(!users.isEmpty()){
            List<Integer> userlist = new ArrayList<Integer>();
            for(int i = 0; i < batchsize && !users.isEmpty(); i++)
                userlist.add(users.poll());
            List<Map<String, Object>> values = copIntimacyMapper.getTeamProejct(userlist);
            for(Map<String, Object> value : values){
                int v = ((Long) value.get("pros")).intValue();
                int u = (Integer) value.get("userA");
                LinkedList<Integer> pl = updateValues.get(v);
                if(pl == null){
                    pl = new LinkedList<Integer>();
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
        int s = size;
        for(Map.Entry<Integer, LinkedList<Integer>> value : updateValues.entrySet()){
            LinkedList<Integer> v = value.getValue();
            int ss = v.size();
            while(true) {
                List<Integer> tmp = new ArrayList<Integer>();
                for (int i = 0; i < 10 && !v.isEmpty(); i++) {
                    tmp.add(v.poll());
                }
                if(tmp.size() == 0)break;
                copIntimacyMapper.updateCop(value.getKey(), tmp);
                if(v.isEmpty()) break;
                System.out.printf("%d/%d : %d/%d\n", size, s, v.size(), ss);
            }
            size --;
        }
    }

}
