package cn.springmvc.service;


import cn.springmvc.dao.LangAbilityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class LangAbility {

    @Autowired
    LangAbilityMapper langAbilityMapper;

    public void calculate(){
        ArrayList<Integer> projects = langAbilityMapper.selectProjectId_Filter1();
        System.out.printf("Projects: %d\n", projects.size());
        System.out.printf("Project No.1: %d\n", projects.get(0));
        int test_project = projects.get(1);
        ArrayList<Map<String, Object>> authorTimes = langAbilityMapper.selectAuthorTime(test_project);
        ArrayList<Map<String, Object>> langPercent = langAbilityMapper.selectLangPercent(test_project);
        ArrayList<Integer> wc = langAbilityMapper.getWatchNum(test_project);
        if(wc.size() != 0)System.out.printf("size: %d\n", wc.get(0));
        else System.out.println("ERROR");
        Map<String, Object> t = authorTimes.get(0);
        int author_id = (Integer) t.get("author_id");
        int times = (Integer) t.get("times");
        System.out.printf("%d, %d\n", author_id, times);
        Map<String, Object> langs = langPercent.get(0);
        String lang = (String) langs.get("language");
        Double percent = (Double) langs.get("percent");
        System.out.printf("%s, %f\n", lang, percent);
    }

}
