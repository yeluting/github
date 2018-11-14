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

    public void calculate(int batchsize, int MAXTHREAD){
        int t_init = Thread.activeCount();
        ArrayList<Integer> users = intimacyMapper.getSize();
        for(int i = 0; i < users.size(); i += batchsize + 1){
            int end = i + batchsize >= users.size() - 1 ? users.size() - 1 : i + batchsize;
            while(Thread.activeCount() >= t_init + MAXTHREAD);
            new Thread(new updateThread(users.get(i), users.get(end), intimacyMapper)).start();
        }
        while(Thread.activeCount() != t_init);
    }

    public Map<Integer, Double> calculateTeam(int project){
        List<Integer> members = intimacyMapper.getMembersByPid(project);
        if(members.size() <= 1) return new HashMap<Integer, Double>();
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
        Map<Integer, Double> values = new HashMap<Integer, Double>();
        for(int i = 0; i <  members.size(); i++){
            double tmp = 0.0;
            for(int j = 0; j < members.size(); j++){
                if(i == j) continue;
                tmp += shortestWeightedPath(i, j, p_int);
            }
            values.put(members.get(i), tmp / (members.size() - 1));
        }
        return values;
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

    private int userA;
    private int userB;
    private IntimacyMapper intimacyMapper;

    public updateThread(int userA, int userB, IntimacyMapper intimacyMapper){
        this.userA = userA;
        this.userB = userB;
        this.intimacyMapper = intimacyMapper;
    }

    public void run(){
        System.out.printf("From %d to %d START.", userA, userB);
        intimacyMapper.updateIntimacy(userA, userB);
        System.out.printf("From %d to %d END.", userA, userB);
    }

}
