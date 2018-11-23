package cn.springmvc.service;

import cn.springmvc.dao.DataPreLoadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataPreLoad {

    @Autowired
    private DataPreLoadMapper dataPreLoadMapper;

    //语言的映射，数据库列名c++
    public static Map<String, String> LangMap = new HashMap<String, String>();
    //语言与数字id对应
    public static Map<String, Integer> LangIndexMap = new HashMap<String, Integer>();
    //开发者能力
    public static Map<Integer, double[]> Competence = new HashMap<Integer, double[]>();
    //偏好
    public static Map<Integer, double[]> Coefficient = new HashMap<Integer, double[]>();
    //亲密度
    public static Map<Integer, Map<Integer, Double>> IntimacyMap = new HashMap<Integer, Map<Integer, Double>>();


    public void loadData(){
        List<Map<String, Object>> LangList = dataPreLoadMapper.loadLanguage();
        List<Map<String, Object>> CompList = dataPreLoadMapper.loadCompetence();
        List<Map<String, Object>> CoefList = dataPreLoadMapper.loadCoefficient();
        List<Map<String, Object>> IntiList = dataPreLoadMapper.loadIntimacy();
        int i = 0;
        for(Map<String, Object> Lang : LangList){
            String language = (String) Lang.get("language");
            LangMap.put(language, (String) Lang.get("LField"));
            LangIndexMap.put(language, i++);
        }
        for(Map<String, Object> Comp : CompList){
            int author_id = (Integer) Comp.get("author_id");
            double[] competence = new double[i];
            int j = 0;
            for(String LFiled : LangMap.values())
                competence[j++] = (Double) Comp.get(LFiled);
            Competence.put(author_id, competence);
        }
        for(Map<String, Object> Coef : CoefList){
            int member_id = (Integer) Coef.get("member_id");
            double[] coefficient = {(Double) Coef.get("alpha"), (Double) Coef.get("beta"), (Double) Coef.get("gamma")};
            Coefficient.put(member_id, coefficient);
        }
        for(Map<String, Object> Inti : IntiList){
            int userA = (Integer) Inti.get("userA");
            int userB = (Integer) Inti.get("userB");
            double intimacy = (Double) Inti.get("intimacy");
            if(!IntimacyMap.containsKey(userA))
                IntimacyMap.put(userA, new HashMap<Integer, Double>());
            IntimacyMap.get(userA).put(userB, intimacy);
        }
    }

}
