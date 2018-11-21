package cn.springmvc.service;

import cn.springmvc.dao.LangFilterMapper;
import cn.springmvc.dao.LangGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LangGroup {

    @Autowired
    private LangFilterMapper langFilterMapper;

    @Autowired
    private LangGroupMapper langGroupMapper;

    public void group(int limit){
        ArrayList<Map<String, Object>> langParser = langFilterMapper.loadLangParser();
        Map<String, String> langP = new HashMap<String, String>();
        for(Map<String, Object> lp : langParser){
            langP.put((String) lp.get("language"), (String) lp.get("LField"));
        }
        List<Integer> projects = langGroupMapper.getProjects();
        int i = 0;
        for(int project : projects){
            System.out.printf("%d/%d\n", ++i, projects.size());
            String[] proLang = langGroupMapper.getproLangs(project, limit);
            if(proLang.length < limit) continue;
            for(int j = 0; j < proLang.length; j++) proLang[j] = langP.get(proLang[j]);
            Arrays.sort(proLang);
            langGroupMapper.updateGroup(proLang, limit);
        }
    }

}
