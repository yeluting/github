package cn.springmvc.service;

import cn.springmvc.dao.SocIntimacyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SocIntimacy {

    @Autowired
    private SocIntimacyMapper socIntimacyMapper;

    public void calculate(int batchsize, int upd){
        Map<Integer, LinkedList<Integer>> updateValues = new HashMap<Integer, LinkedList<Integer>>();
        LinkedList<Integer> users = socIntimacyMapper.getUserId();
        System.out.printf("Get %d users.\n", users.size());
        int si = users.size();
        while(!users.isEmpty()){
            List<Integer> userlist = new ArrayList<Integer>();
            for(int i = 0; i < batchsize && !users.isEmpty(); i++)
                userlist.add(users.poll());
            List<Map<String, Object>> values = socIntimacyMapper.getOrgs(userlist);
            for(Map<String, Object> value : values){
                int v = ((BigDecimal) value.get("orgs")).intValue();
                int u = (Integer) value.get("userA");
                LinkedList<Integer> pl = updateValues.get(v);
                if(pl != null){
                    pl.add(u);
                }else{
                    pl = new LinkedList<Integer>();
                    pl.add(u);
                    updateValues.put(v, pl);
                }
            }
            System.out.printf("%d %d\n", users.size(), si);
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
                for (int i = 0; i < upd && !v.isEmpty(); i++) {
                    tmp.add(v.poll());
                }
                if(tmp.size() == 0)break;
                socIntimacyMapper.updateOrg(value.getKey(), tmp);
                if(v.isEmpty()) break;
                System.out.printf("%d/%d : %d/%d\n", size, s, v.size(), ss);
            }
            size --;
        }
    }

}

