package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.LangAbilityAnaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LangAbilityAna {

    @Autowired
    private LangAbilityAnaMapper langAbilityAnaMapper;

    public void calculate(int limit){
        String[] mapKeys = {"lang", "count", "avg", "min", "max"};
        ArrayList<String> languages = langAbilityAnaMapper.selectLang();
        Map<String, Map<String, Object>> output = new HashMap();
        for(String lang : languages){
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put(mapKeys[0], lang);
            tmp.put(mapKeys[1], 0);
            tmp.put(mapKeys[2], 0.0);
            tmp.put(mapKeys[3], Double.MAX_VALUE);
            tmp.put(mapKeys[4], -1.0);
            output.put(lang, tmp);
        }
        System.out.println("Finish Selecting");
        for(int offset = 0; ; offset += limit) {
            ArrayList<Map<String, Object>> langAbilities = langAbilityAnaMapper.selectLangAbility(offset, limit);
            System.out.printf("offset:%d limit:%d size:%d\n", offset, limit, langAbilities.size());
            if(langAbilities.size() == 0) break;
            for (Map<String, Object> langAbility : langAbilities) {
                for (String lang : languages) {
                    double ability = (Double) (langAbility.get(lang));
                    if (ability != 0) {
                        Map<String, Object> tmp = output.get(lang);
                        tmp.put(mapKeys[1], ((Integer) (tmp.get(mapKeys[1]))) + 1);
                        tmp.put(mapKeys[2], ((Double) (tmp.get(mapKeys[2]))) + ability);
                        double min = (Double) (tmp.get(mapKeys[3]));
                        double max = (Double) (tmp.get(mapKeys[4]));
                        if (ability < min) tmp.put(mapKeys[3], ability);
                        if (ability > max) tmp.put(mapKeys[4], ability);
                        output.put(lang, tmp);
                    }
                }
            }
        }
        System.out.println("Finish Calculating");
        List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
        for(Map.Entry<String, Map<String, Object>> t : output.entrySet()) {
            if ((Integer) (t.getValue().get(mapKeys[1])) == 0){
                t.getValue().put(mapKeys[3], 0.0);
                t.getValue().put(mapKeys[4], 0.0);
                outputList.add(t.getValue());
            }else {
                t.getValue().put(mapKeys[2], (Double) (t.getValue().get(mapKeys[2])) / (Integer) (t.getValue().get(mapKeys[1])));
                outputList.add(t.getValue());
            }
        }
        System.out.println("Start inserting");
        langAbilityAnaMapper.saveLangAnalysis(outputList);
    }

    public void countSmall(double value, int limit){
        ArrayList<String> languages = langAbilityAnaMapper.selectLang();
        Map<String, Integer> output = new HashMap();
        for(String lang : languages) output.put(lang, 0);
        System.out.println("Finish Selecting");
        for(int offset = 0; ; offset += limit) {
            ArrayList<Map<String, Object>> langAbilities = langAbilityAnaMapper.selectLangAbility(offset, limit);
            System.out.printf("offset:%d limit:%d size:%d\n", offset, limit, langAbilities.size());
            if(langAbilities.size() == 0) break;
            for (Map<String, Object> langAbility : langAbilities) {
                for (String lang : languages) {
                    double ability = (Double) (langAbility.get(lang));
                    if (ability > 0 && ability < value) {
                        output.put(lang, output.get(lang) + 1);
                    }
                }
            }
        }
        System.out.println("Finish Calculating");
        for(Map.Entry<String, Integer> lang : output.entrySet()){
            langAbilityAnaMapper.updateCount(lang.getKey(), lang.getValue());
        }
    }

    public void normed_dis(){
        ArrayList<String> languages = langAbilityAnaMapper.selectLang();
        Map<String, int[]> l_dis = new HashMap<String, int[]>();
        for(String language : languages){
            ArrayList<Double> values = langAbilityAnaMapper.selectNormedLA(language);
            int[] dis = new int[10];
            for(double v : values) {
                int vv = v == 1 ? 9 : (int) (v * 10);
                if(vv >= 10) vv = 9;
                dis[vv]++;
            }
            l_dis.put(language, dis);
        }
        langAbilityAnaMapper.insertNormedDis(l_dis);
    }

}
