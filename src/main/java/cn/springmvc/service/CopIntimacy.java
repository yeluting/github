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
            List<Map<String, Integer>> values = copIntimacyMapper.getTeamProejct(userlist);
            for(Map<String, Integer> value : values){
                List<Integer> pl = updateValues.get(value.get("pros"));
                if(pl == null){
                    pl = new ArrayList<Integer>();
                    pl.add(value.get("userA"));
                    updateValues.put(value.get("pros"), pl);
                }else{
                    pl.add(value.get("userA"));
                }
            }
            break;
        }
        for(Map.Entry<Integer, List<Integer>> value : updateValues.entrySet()){
            System.out.printf("%02d: ", value.getKey());
            for(Integer v : value.getValue())
                System.out.printf("%d, ", v);
            System.out.println();
        }
        System.out.println("Start Update.");
    }

}
