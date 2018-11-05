package cn.springmvc.service;


import cn.springmvc.dao.LangFilterMapper;
import cn.springmvc.dao.LangSuffixMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class LangSuffix {

    @Autowired
    private LangFilterMapper langFilterMapper;

    @Autowired
    private LangSuffixMapper langSuffixMapper;

    final private int ROWMAX = 10000;

    public void statLangSuffix(){
        ArrayList<Map<String, Object>> langParser = langFilterMapper.loadLangParser();
        Map<String, String> parser = new HashMap<String, String>();
        for(Map<String, Object> lang : langParser)
            parser.put((String) lang.get("language"), (String) lang.get("LField"));
        for(int i = 0;; i += ROWMAX){
            ArrayList<Map<String, Object>> rows = langSuffixMapper.readRow(i, ROWMAX);
            if(rows.isEmpty()) break;
            Map<String, Map<String, Integer>> data = new HashMap<String, Map<String, Integer>>();
            for(int j = 0; j < rows.size(); j++){
                Map<String, Object> row = rows.get(j);
                String lang = parser.get(row.get("language"));
                String[] suffixes = ((String) row.get("fileType")).split("#");
                if(!data.containsKey(lang))
                    data.put(lang, new HashMap<String, Integer>());
                Map<String, Integer> suffixFreq = data.get(lang);
                for(String suffix : suffixes){
                    if(!suffixFreq.containsKey(suffix)) suffixFreq.put(suffix, 1);
                    else suffixFreq.put(suffix, suffixFreq.get(suffix) + 1);
                }
            }
            langSuffixMapper.updateLangsuffix(data);
            System.out.println(i);
        }
    }

}
