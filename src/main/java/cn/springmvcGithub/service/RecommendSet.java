package cn.springmvcGithub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RecommendSet {

    @Autowired
    private DataPreLoad dataPreLoad;

    //找出相应语言下的开发者候选集
    public Map<String, Set<Integer>> RSetByLangs(int userId, String[] langs){
        Map<String, Set<Integer>> LangUserMap = new HashMap<String, Set<Integer>>();
        for(int i = 0; i < langs.length; i++) LangUserMap.put(langs[i], new HashSet<Integer>());
        for(Map.Entry<Integer, double[]> userComp : DataPreLoad.Competence.entrySet()){
            if(userComp.getKey() == userId) continue;
            double[] comp = userComp.getValue();
            for(String lang : langs)
                if(comp[DataPreLoad.LangIndexMap.get(lang)] > 0)
                    LangUserMap.get(lang).add(userComp.getKey());
        }
        return LangUserMap;
    }

}
