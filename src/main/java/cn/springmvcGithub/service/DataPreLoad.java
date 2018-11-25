package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.DataPreLoadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    //用户Id与名字
    public static Map<Integer, String> NameMap = new HashMap<Integer, String>();

    private int threadCount = 10;

    public void loadData(){
        List<Map<String, Object>> LangList = dataPreLoadMapper.loadLanguage();
        List<Map<String, Object>> CompList = dataPreLoadMapper.loadCompetence();
        List<Map<String, Object>> CoefList = dataPreLoadMapper.loadCoefficient();
//        List<Map<String, Object>> IntiList = dataPreLoadMapper.loadIntimacy();
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
            NameMap.put(member_id, (String) Coef.get("name"));
        }
        List<RelationMT> mts = new ArrayList<RelationMT>();
        for(int k = 0; k < threadCount; k++)
            mts.add(new RelationMT(k, dataPreLoadMapper));
        int count = Thread.activeCount();
        for(int k = 0; k < threadCount; k++)
            new Thread(mts.get(k)).start();
        while(Thread.activeCount() != count);
        for(int k = 0; k < threadCount; k++) {
            for (Map<String, Object> Inti : mts.get(k).IntiList) {
                int userA = (Integer) Inti.get("userA");
                int userB = (Integer) Inti.get("userB");
                double intimacyA = (Double) Inti.get("intimacyA");
                double intimacyB = (Double) Inti.get("intimacyB");
                if (!IntimacyMap.containsKey(userA))
                    IntimacyMap.put(userA, new HashMap<Integer, Double>());
                IntimacyMap.get(userA).put(userB, intimacyA);
                if (!IntimacyMap.containsKey(userB))
                    IntimacyMap.put(userB, new HashMap<Integer, Double>());
                IntimacyMap.get(userB).put(userA, intimacyB);
            }
        }
    }

}

class RelationMT implements Runnable{

    private int tid;
    private DataPreLoadMapper dataPreLoadMapper;
    public List<Map<String, Object>> IntiList;

    public RelationMT(int id, DataPreLoadMapper dplm){
        tid = id;
        dataPreLoadMapper = dplm;
    }

    public void run(){
        IntiList = dataPreLoadMapper.loadIntimacy(tid);
    }

}