package cn.springmvc.service;

import cn.springmvc.dao.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class Organization {

    @Autowired
    private OrganizationMapper organizationMapper;

    private Map<Integer, Map<Integer, Integer>> relation;

    public void calculate(int size, int max_thread){
        int count = 0;
        relation = new HashMap<Integer, Map<Integer, Integer>>();
        ArrayList<Integer> orgs = organizationMapper.selectOrgId();
        for(int p = 0; p < orgs.size(); p++){
            int org = orgs.get(p);
            ArrayList<Integer> members = organizationMapper.selectMembersByOrgId(org);
            for(int i = 0; i < members.size(); i++){
                Map<Integer, Integer> memberMap = relation.get(members.get(i));
                if(memberMap == null) {
                    memberMap = new HashMap<Integer, Integer>();
                    relation.put(members.get(i), memberMap);
                }
                for(int j = i + 1; j < members.size(); j++){
                    Integer value = memberMap.get(members.get(j));
                    if(value == null){
                        count += 2;
                        memberMap.put(members.get(j), 1);
                        relation.put(members.get(i), memberMap);
                        Map<Integer, Integer> tmp = relation.get(members.get(j));
                        if(tmp == null){
                            tmp = new HashMap<Integer, Integer>();
                            tmp.put(members.get(i), 1);
                            relation.put(members.get(j), tmp);
                        }else{
                            tmp.put(members.get(i), 1);
                        }
                    }else{
                        memberMap.put(members.get(j), value + 1);
                        relation.put(members.get(i), memberMap);
                        relation.get(members.get(j)).put(members.get(i), value + 1);
                    }
                }
            }
            if(count >= size || ((count >0) && (p == orgs.size() - 1))){
                List<Map<String, Integer>> output = new ArrayList<Map<String, Integer>>();
                int csize = 0;
                for(Map.Entry<Integer, Map<Integer, Integer>> entry : relation.entrySet()){
                    int userA = entry.getKey();
                    for(Map.Entry<Integer, Integer> en : entry.getValue().entrySet()){
                        Map<String, Integer> tmp = new HashMap<String, Integer>();
                        tmp.put("userA", userA);
                        tmp.put("userB", en.getKey());
                        tmp.put("org", en.getValue());
                        output.add(tmp);
                        csize++;
                        if(csize >= size) {
                            while(Thread.activeCount() >= max_thread);
                            new Thread(new OrganizationDB(output, this.organizationMapper)).start();
                            csize = 0;
                            output.clear();
                        }
                    }
                }
                if(csize > 0)
                    new Thread(new OrganizationDB(output, this.organizationMapper)).start();
                relation.clear();
                count = 0;
            }
        }
    }
}


class OrganizationDB implements Runnable{

    private OrganizationMapper organizationMapper;

    private List<Map<String, Integer>> relation;

    public OrganizationDB(List<Map<String, Integer>> relation, OrganizationMapper organizationMapper){
        this.relation = new ArrayList<Map<String, Integer>>();
        for(Map<String, Integer> rel : relation)
            this.relation.add(rel);
        this.organizationMapper = organizationMapper;
    }

    public void run(){
        organizationMapper.insertOrganizationBatch(relation);
    }

}