package cn.springmvc.service;

import cn.springmvc.dao.IntimacyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class Intimacy {

    @Autowired
    private IntimacyMapper intimacyMapper;

    final private double MAXDIS = 100;
    final private double[] coeff = {0.7, 0.5};

    public void calculate(int limit, int MAXTHREAD){
        int t_init = Thread.activeCount();
        int size = ((BigDecimal) intimacyMapper.getSize()).intValue();
        for(int offset = 0; offset <= size; offset += limit){
            while(Thread.activeCount() >= t_init + MAXTHREAD);
            new Thread(new updateThread(limit, offset, intimacyMapper)).run();
        }
        while(Thread.activeCount() != t_init);
    }

    public void calculateTeam(){
        LinkedList<Integer> projects = intimacyMapper.getProjectId();
        System.out.printf("Get %d projects.\n", projects.size());
        int si = projects.size();
        while(!projects.isEmpty()){
            int project = projects.poll();
            List<Integer> members = intimacyMapper.getMembersByPid(project);
            double[][] p_int = new double[members.size()][members.size()];
            for(int i = 0; i < members.size(); i++){
                for(int j = 0; j < members.size(); j++){
                    if(i == j) p_int[i][j] = 0.0;
                    else{
                        Double tmp = intimacyMapper.getPairIntimacy(members.get(i), members.get(j));
                        if(tmp == null) p_int[i][j] = MAXDIS;
                        else p_int[i][j] = tmp;
                    }
                }
            }
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            for(int i = 0; i <  members.size(); i++){
                for(int j = 0; j < members.size(); j++){
                    if(i == j) continue;
                    Map<String, Object> tmp = new HashMap<String, Object>();
                    tmp.put("pid", project);
                    tmp.put("userA", members.get(i));
                    tmp.put("userB", members.get(j));
                    tmp.put("value", shortestWeightedPath(i, j, p_int));
                }
            }
            intimacyMapper.insertTeamIntimacy(values);
            System.out.printf("%d / %d\n", projects.size(), si);
        }
    }

    //Copy From https://github.com/yeluting/kaggle/blob/master/src/main/java/cn/springmvc/service/CompetitorIntimacy.java
    //Line 135
    //广度优先计算start-->end的最短加权路径，权值为方阵表示
    private double shortestWeightedPath(int start, int end, double[][] weights) {
        double minWeight = MAXDIS; //Double.MAX_VALUE;
        Queue<Integer> subNodes = new LinkedList<Integer>();
        Queue<Double> minWeights = new LinkedList<Double>();
        subNodes.offer(start);
        minWeights.offer(0.0);
        while (!subNodes.isEmpty()) {
            int currentNode = subNodes.poll();
            double currentWeight = minWeights.poll();
            for (int i = 0; i < weights[currentNode].length; i++) {
                //回头路不走，不通的路不走，已经大于等于现有最小权值的路不走（不入队）
                if (i == currentNode || currentWeight >= minWeight) {
                    continue;
                    //遇终点判断权值大小，不入队
                } else if (i == end) {
                    if (currentWeight + weights[currentNode][i] < minWeight) {
                        minWeight = currentWeight + weights[currentNode][i];
                    }
                    //入队
                } else {
                    subNodes.offer(i);
                    minWeights.offer(currentWeight + weights[currentNode][i]);
                }
            }
        }
        return minWeight;
    }

}

class updateThread implements Runnable{

    private int limit;
    private int offset;
    private IntimacyMapper intimacyMapper;

    public updateThread(int limit, int offset, IntimacyMapper intimacyMapper){
        this.limit = limit;
        this.offset = offset;
        this.intimacyMapper = intimacyMapper;
    }

    public void run(){
        System.out.printf("From %d to %d START.", offset, limit + offset);
        intimacyMapper.updateIntimacy(limit, offset);
        System.out.printf("From %d to %d END.", offset, limit + offset);
    }

}
