package cn.springmvcGithub.service;


import cn.springmvcGithub.dao.LangAbilityMapper;
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

    private Map<String, String> LangParser;

    //按项目的语言占比计算用户的能力
    public void calculate(int max_thread){
        loadParser();
        ArrayList<Integer> projects = langAbilityMapper.selectProjectId_Filter1();
        int size = projects.size();
        int finish = 0;
        for(int project : projects){
            int watchNum = 0;
            ArrayList<Integer> wc = langAbilityMapper.getWatchNum(project);
            if(wc.size() != 1)continue;
            else watchNum = wc.get(0);
            if(watchNum == 0) continue;
            ArrayList<Map<String, Object>> authorTimes = langAbilityMapper.selectAuthorTime(project);
            ArrayList<Map<String, Object>> langPercent = langAbilityMapper.selectLangPercent(project);
            if(authorTimes.size() == 0 || langPercent.size() == 0)continue;
            List<String> langs = new ArrayList<String>();
            List<Double> std_score = new ArrayList<Double>();
            for(Map<String, Object> lp : langPercent){
                langs.add(LangParser.get(lp.get("language")));
                std_score.add(((Double) lp.get("percent")) * watchNum);
            }
            double totalTimes = 0.0;
            for(Map<String, Object> author : authorTimes) totalTimes += (Integer) author.get("times");
            Map<Integer, List<Double>> langAbility = new HashMap<Integer, List<Double>>();
            for(Map<String, Object> author : authorTimes){
                List<Double> ability = new ArrayList<Double>();
                for(Double score :  std_score)
                    ability.add(score * ((Integer) author.get("times")) / totalTimes);
                langAbility.put((Integer) author.get("author_id"), ability);
            }
            while(Thread.activeCount() >= max_thread);
            new Thread(new LangAbilityDB(langAbilityMapper, langs, langAbility)).start();
            finish++;
            if(finish >= 300000)System.out.printf("%d/%d\n", finish, size);
        }
    }

    //语言映射
    private void loadParser(){
        List<Map<String, Object>> parser = langAbilityMapper.selectLangParser();
        LangParser = new HashMap<String, String>();
        for(Map<String, Object> pair :  parser)
            LangParser.put((String) pair.get("language"), (String) pair.get("LField"));
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

    public void normalization(){
        List<Map<String, Object>> max = langAbilityMapper.getMax();
        Map<String, Double> param = new HashMap<String, Double>();
        for(Map<String, Object> m : max)
            param.put((String) m.get("language"), (Double) m.get("maxAbility"));
        langAbilityMapper.norm(param);
    }

}

class LangAbilityDB implements Runnable{

    private LangAbilityMapper langAbilityMapper;

    private List<String> langs;

    private Map<Integer, List<Double>> langAbility;

    public LangAbilityDB(LangAbilityMapper lam, List<String> langs, Map<Integer, List<Double>> langAbility){
        langAbilityMapper = lam;
        this.langs = langs;
        this.langAbility = langAbility;
    }

    public void run() {
        langAbilityMapper.insertAbilityByProject(langs, langAbility);
    }
}
