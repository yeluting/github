package cn.springmvc.service;

import cn.springmvc.dao.LangAbilityAnaMapper;
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

    public void calculate(){
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
        for(int offset = 0, limit = 10000; ; offset += limit) {
            ArrayList<Map<String, Object>> langAbilities = langAbilityAnaMapper.selectLangAbility(offset, limit);
            System.out.printf("offset:%d limit:%d size:%d\n", offset, limit, langAbilities.size());
            if(langAbilities.size() == 0) break;
            for (Map<String, Object> langAbility : langAbilities) {
                for (String lang : languages) {
                    double abilty = (Double) (langAbility.get(lang));
                    if (Double.compare(abilty, 0.0) == 0) {
                        Map<String, Object> tmp = output.get(lang);
                        tmp.put(mapKeys[1], ((Integer) (tmp.get(mapKeys[1]))) + 1);
                        tmp.put(mapKeys[2], ((Double) (tmp.get(mapKeys[2]))) + abilty);
                        double min = (Double) (tmp.get(mapKeys[3]));
                        double max = (Double) (tmp.get(mapKeys[4]));
                        if (abilty < min) tmp.put(mapKeys[3], abilty);
                        if (abilty > max) tmp.put(mapKeys[4], abilty);
                        output.put(lang, tmp);
                    }
                }
            }
            break;
        }
        System.out.println("Finish Calculating");
        List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
        for(Map.Entry<String, Map<String, Object>> t : output.entrySet()) {
            t.getValue().put(mapKeys[2], (Integer)(t.getValue().get(mapKeys[2])) / (Double)(t.getValue().get(mapKeys[1])));
            outputList.add(t.getValue());
        }
        System.out.println("Start inserting");
        langAbilityAnaMapper.saveLangAnalysis(outputList);
    }

}
