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
        Map<Long, List<Long>> updateValues = new HashMap<Long, List<Long>>();
        LinkedList<Integer> users = copIntimacyMapper.getUserId();
        System.out.printf("Get %d users.\n", users.size());
        while(!users.isEmpty()){
            List<Integer> userlist = new ArrayList<Integer>();
            for(int i = 0; i < batchsize && !users.isEmpty(); i++)
                userlist.add(users.poll());
            List<Map<String, Long>> values = copIntimacyMapper.getTeamProejct(userlist);
            for(Map<String, Long> value : values){
                Long v = value.get("pros");
                Long u = value.get("userA");
                List<Long> pl = updateValues.get(v);
                if(pl == null){
                    pl = new ArrayList<Long>();
                    pl.add(u);
                    updateValues.put(v, pl);
                }else{
                    pl.add(u);
                }
            }
            break;
        }
        for(Map.Entry<Long, List<Long>> value : updateValues.entrySet()){
            System.out.printf("%02d: ", value.getKey());
            for(Long v : value.getValue())
                System.out.printf("%d, ", v);
            System.out.println();
        }
        System.out.println("Start Update.");
    }

}
