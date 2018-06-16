package cn.springmvc.service;


import cn.springmvc.dao.LangAbilityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class LangAbility {

    @Autowired
    LangAbilityMapper langAbilityMapper;

    public void calculate(){
        ArrayList<Integer> projects = langAbilityMapper.selectProjectId_Filter1();
        for(int project : projects){
            int watchNum = 0;
            ArrayList<Integer> wc = langAbilityMapper.getWatchNum(project);
            if(wc.size() != 1)continue;
            else watchNum = wc.get(0);
            if(watchNum == 0) continue;
            ArrayList<Map<String, Object>> authorTimes = langAbilityMapper.selectAuthorTime(project);
            ArrayList<Map<String, Object>> langPercent = langAbilityMapper.selectLangPercent(project);
            break;
        }
        Map<String, Double> test = new HashMap<String, Double>();
        test.put("abap", 1.0);
        test.put("actionscript", 0.5423);
        langAbilityMapper.insertLangAbility(-1, test);
        langAbilityMapper.insertLangAbility(-1, test);
        langAbilityMapper.insertLangAbility(-1, test);
    }

}
