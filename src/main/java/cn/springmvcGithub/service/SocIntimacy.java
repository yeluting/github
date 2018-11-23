package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.SocIntimacyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SocIntimacy {

    @Autowired
    private SocIntimacyMapper socIntimacyMapper;

    public void calculate(int threads, int batchsize, int upd){
        LinkedList<Integer> users = socIntimacyMapper.getUserId();
        List<LinkedList<Integer>> sub_users = new ArrayList<LinkedList<Integer>>();
        for(int i = 0; i < threads; i++) sub_users.add(new LinkedList<Integer>());
        for(int j = 0; j < users.size();){
            for(int i = 0; i < threads && j < users.size(); i++, j++)
                sub_users.get(i).add(users.get(j));
        }
        int init_thread = Thread.activeCount();
        for(int i = 0; i < threads; i++)
            new Thread(new SocIntimacyParallel(batchsize, upd, socIntimacyMapper, sub_users.get(i))).start();
        while(Thread.activeCount() != init_thread);
    }

}

class SocIntimacyParallel implements Runnable{

    private LinkedList<Integer> users;
    private int batchsize;
    private int upd;
    private SocIntimacyMapper socIntimacyMapper;

    public SocIntimacyParallel(int bs, int u, SocIntimacyMapper smapper, LinkedList<Integer> us){
        users = us;
        batchsize = bs;
        upd = u;
        socIntimacyMapper = smapper;
    }

    //计算社交关系下的亲密度
    public void run(){
        Map<Integer, LinkedList<Integer>> updateValues = new HashMap<Integer, LinkedList<Integer>>();
        System.out.printf("Get %d users.\n", users.size());
        int si = users.size();
        while(!users.isEmpty()){
            int uid = users.poll();
            List<Map<String, Object>> values = socIntimacyMapper.getOrgs(uid);
            if(values.size() != 1) continue;
            Map<String, Object> value = values.get(0);
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
            System.out.printf("%d %d\n", users.size(), si);
        }
        System.out.println("Start Update.");
        System.out.printf("size : %d\n", updateValues.size());
        for(Map.Entry<Integer, LinkedList<Integer>> value : updateValues.entrySet()){
            LinkedList<Integer> v = value.getValue();
            int ss = v.size();
            int i = 0;
            for(int vv : v){
                if(value.getKey() == 0)
                    socIntimacyMapper.updateOrg0(vv);
                else
                    socIntimacyMapper.updateOrg(value.getKey(), vv);
                System.out.printf("%d/%d\n", ++i, ss);
            }
        }
    }

}

