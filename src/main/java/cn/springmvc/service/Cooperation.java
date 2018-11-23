package cn.springmvc.service;

import cn.springmvc.dao.CooperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class Cooperation {

    @Autowired
    private CooperationMapper cooperationMapper;

    private Map<Integer, Map<Integer, Integer>> relation;

    //计算开发者间的协作次数
    public void calculate(int size, int max_thread){
        int count = 0;
        int t = Thread.activeCount();
        relation = new HashMap<Integer, Map<Integer, Integer>>();
        ArrayList<Integer> projects = cooperationMapper.selectProjectId_Filter1();
        for(int p = 0; p < projects.size(); p++){
            int project = projects.get(p);
            ArrayList<Integer> members = cooperationMapper.selectMembersByProjectId(project);
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
            if(count >= size || ((count >0) && (p == projects.size() - 1))){
//                System.out.printf("Size:%d\n", count);
                List<Map<String, Integer>> output = new ArrayList<Map<String, Integer>>();
                int csize = 0;
                for(Map.Entry<Integer, Map<Integer, Integer>> entry : relation.entrySet()){
                    int userA = entry.getKey();
                    for(Map.Entry<Integer, Integer> en : entry.getValue().entrySet()){
                        Map<String, Integer> tmp = new HashMap<String, Integer>();
                        tmp.put("userA", userA);
                        tmp.put("userB", en.getKey());
                        tmp.put("cop", en.getValue());
                        output.add(tmp);
                        csize++;
                        if(csize >= size) {
                            while(Thread.activeCount() >= max_thread + t);
                            new Thread(new CooperationDB(output, this.cooperationMapper)).start();
                            csize = 0;
                            output.clear();
                        }
                    }
                }
                if(csize > 0)
                    new Thread(new CooperationDB(output, this.cooperationMapper)).start();
                relation.clear(); 
                count = 0;
            }
        }
    }
}


class CooperationDB implements Runnable{

    private CooperationMapper cooperationMapper;

    private List<Map<String, Integer>> relation;

    public CooperationDB(List<Map<String, Integer>> relation, CooperationMapper cooperationMapper){
        this.relation = new ArrayList<Map<String, Integer>>();
       for(Map<String, Integer> rel : relation)
           this.relation.add(rel);
        this.cooperationMapper = cooperationMapper;
    }

    public void run(){
//        System.out.println("Saving.");
        cooperationMapper.insertCooperationBatch(relation);
    }

}