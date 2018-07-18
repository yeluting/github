package cn.springmvc.service;

import cn.springmvc.dao.LangFilterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class LangFilter {

    @Autowired
    private LangFilterMapper langFilterMapper;

    public void PageRank(){
        ArrayList<Map<String, Object>> langParser = langFilterMapper.loadLangParser();
        int N = langParser.size();
        int[][] graph = new int[N][N];
        Map<String, String> langP = new HashMap<String, String>();
        Map<String, Integer> langToIndex = new HashMap<String, Integer>();
        int i = 0;
        for(Map<String, Object> lp : langParser){
            langP.put((String) lp.get("language"), (String) lp.get("LField"));
            langToIndex.put((String) lp.get("language"), i);
            graph[i++] = new int[langParser.size()];
        }
        int total = loadGraph(graph, langToIndex);
        int[] out = new int[N];
        for(i = 0; i < N; i++){
            out[i] = 0;
            for(int j = 0; j < N; j++) out[i] += (graph[i][j] == 0) ? 0 : 1;
        }
        double[] pr = new double[N];
        for(i = 0; i < pr.length; i++) pr[i] = 1.0 / N;
        int MAXSTEP = 1000000;
        double delta = 0.00001;
        double alpha = 0.85;
        double bias = (1 - alpha) / N;
        boolean flag = false;
        for(i = 0; i < MAXSTEP; i++){
            double change = 0;
            for(int j = 0; j < N; j++){
                double current = 0.0;
                for(int k = 0; k < N; k++)
                    current += (graph[j][k] == 0) ? 0 : (pr[k] * graph[j][k]) / (out[k] * total);
                current = alpha * current + bias;
                change += Math.abs(current - pr[j]);
                pr[j] = current;
            }
            if(change <= delta) flag = true;
            System.out.printf("INTERATION NO.%02d\n", i + 1);
        }
        if(flag) System.out.printf("Finished in %d iterations.\n", i);
        else System.out.printf("Finished out of %d iterations.\n", MAXSTEP);
        for(Map.Entry<String, Integer> entry : langToIndex.entrySet())
            System.out.printf("%s %f\n", entry.getKey(), pr[entry.getValue()]);
    }

    public int loadGraph(int[][] graph, Map<String, Integer> langToIndex){
        ArrayList<Integer> pids = langFilterMapper.getProjectId();
        for(int pid : pids){
            ArrayList<String> langs = langFilterMapper.getLangs(pid);
            for(int i = 0; i < langs.size(); i++){
                for(int j = i + 1; j < langs.size(); j++){
                    graph[langToIndex.get(langs.get(i))][langToIndex.get(langs.get(j))]++;
                    graph[langToIndex.get(langs.get(j))][langToIndex.get(langs.get(i))]++;
                }
            }
        }
        return pids.size();
    }

}
