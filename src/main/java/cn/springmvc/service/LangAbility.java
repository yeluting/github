package cn.springmvc.service;


import cn.springmvc.dao.LangAbilityMapper;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        }
    }

    public void test(){
        Map<String, Double> test = new HashMap<String, Double>();
        test.put("abap", 1.0);
        test.put("actionscript", 0.5423);
        langAbilityMapper.insertLangAbility(-3, test);
        langAbilityMapper.insertLangAbility(-3, test);
        langAbilityMapper.insertLangAbility(-3, test);
    }

    public void test2(){
        List<String> langs = new ArrayList<String>();
        langs.add("abap");
        langs.add("actionscript");
        Map<Integer, List<Double>> langAbility = new HashMap<Integer, List<Double>>();
        List<Double> ability1 = new ArrayList<Double>();
        List<Double> ability2 = new ArrayList<Double>();
        ability1.add(0.25);
        ability1.add(0.34);
        ability2.add(0.75);
        ability2.add(0.66);
        langAbility.put(-3, ability1);
        langAbility.put(-4, ability2);
        langAbilityMapper.insertAbilityByProject(langs, langAbility);
        langAbilityMapper.insertAbilityByProject(langs, langAbility);
    }


}
