package cn.springmvc.service;


import cn.springmvc.dao.LangCompetenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LangCompetence {

    @Autowired
    private LangCompetenceMapper lcm;

    public static int commit_count;

    final private int TABLE_NUM = 1;
    private Map<String, Map<String, Integer>> suffixMap;
    public int id;
    public ArrayList<Integer> project_ids;
    public LangCompetenceMapper langCompetenceMapper;

    public void multiThreadCalc(int threadCount, int limit){
        ArrayList<Integer> pids = lcm.getProjectID(limit);
        int total = pids.size();
        List<ArrayList<Integer>> pidList = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < threadCount; i++) pidList.add(new ArrayList<Integer>());
        for(int i = 0; i < total;){
            for(int j = 0; j < threadCount && i < total; j++, i++)
                pidList.get(j).add(pids.get(i));
        }
        Thread[] threads = new Thread[threadCount];
        for(int i = 0; i < threadCount; i++){
            threads[i] = new Thread(new LangCompetenceMultiThread(i, pidList.get(i), lcm));
        }
        long startTime = System.currentTimeMillis();
        int startCount = Thread.activeCount();
        for(int i = 0; i < threadCount; i++)
            threads[i].start();
        int endCount = Thread.activeCount();
        while(endCount != startCount) endCount = Thread.activeCount();
        long endTime=System.currentTimeMillis();
        System.out.println("Used : " + (endTime - startTime) + "ms");
        System.out.println("Calculated : " + commit_count + " commits");
    }

    public void calculate(){
        loadSuffixMap();
        int i = 0;
        for(int project_id : project_ids){
            System.out.printf("Thread:%d\tProject ID : %d %d/%d\n", id, project_id, ++i, project_ids.size());
            Map<Integer, Map<String, Integer>> LangContribution = new HashMap<Integer, Map<String, Integer>>();
            calcContribution(project_id, LangContribution);
            updateCompetence(LangContribution);
        }
    }

    private void calcContribution(int project_id, Map<Integer, Map<String, Integer>> LangContribution){
        String[] langs = langCompetenceMapper.getLang(project_id);
        ArrayList<Integer> commit_ids = new ArrayList<Integer>();
        loadCommits(project_id, commit_ids);
        if(commit_ids.size() == 0) return;
        ArrayList<Map<String, Object>> commitDetails = langCompetenceMapper.getCommitDetail(commit_ids);
        Set<String> suffixes = new HashSet<String>();
        for(Map<String, Object> commitDetail : commitDetails){
            commit_count++;
            if(commitDetail.get("fileType") == null) continue;
            String[] fileTypes = ((String) commitDetail.get("fileType")).split("#");
            for(String suffix : fileTypes)
                if(!suffixes.contains(suffix)) suffixes.add(suffix);
        }
        Map<String, String> suffixLangMap = mapSuffix(suffixes, langs);
        for(Map<String, Object> commitDetail : commitDetails){
            if(commitDetail.get("fileType") == null || commitDetail.get("fileModify") == null) continue;
            int author_id = (Integer) commitDetail.get("author_id");
            String[] fileTypes = ((String) commitDetail.get("fileType")).split("#");
            String[] fileModify = ((String) commitDetail.get("fileModify")).split("#");
            if(fileTypes.length != fileModify.length)continue;
            for(int i = 0; i < fileTypes.length; i++){
                String lang = suffixLangMap.get(fileTypes[i]);
                if(lang == null) {
                    langCompetenceMapper.saveMissed(project_id, fileTypes[i]);
                    continue;
                }
                String[] tmp = fileModify[i].split(";");
                int modify = Integer.parseInt(tmp[0]) + Integer.parseInt(tmp[1]);
                if(!LangContribution.containsKey(author_id))
                    LangContribution.put(author_id, new HashMap<String, Integer>());
                Map<String, Integer> secondMap = LangContribution.get(author_id);
                if(!secondMap.containsKey(lang)) secondMap.put(lang, modify);
                else secondMap.put(lang, secondMap.get(lang) + modify);
            }
        }
    }

    private void loadSuffixMap(){
        suffixMap = new HashMap<String, Map<String, Integer>>();
        ArrayList<Map<String, Object>> mappers = langCompetenceMapper.loadSuffixMap();
        for(Map<String, Object> mapper : mappers){
            String language = (String) mapper.get("language");
            String suffix = (String) mapper.get("suffix");
            int freq = (Integer) mapper.get("freq");
            if(!suffixMap.containsKey(suffix))
                suffixMap.put(suffix, new HashMap<String, Integer>());
            Map<String, Integer> secondMap = suffixMap.get(suffix);
            secondMap.put(language, freq);
        }
    }

    private Map<String, String> mapSuffix(Set<String> suffixes, String[] langs){
        Map<String, String> result = new HashMap<String, String>();
        for(String suffix : suffixes){
            if(!suffixMap.containsKey(suffix)) continue;
            Map<String, Integer> tmp = suffixMap.get(suffix);
            int max_index = -1;
            int max = -1;
            for(int i = 0; i < langs.length; i++){
                if(!tmp.containsKey(langs[i])) continue;
                int value = tmp.get(langs[i]);
                if(value > max){
                    max = value;
                    max_index = i;
                }
            }
            if(max_index != -1) result.put(suffix, langs[max_index]);
        }
        return result;
    }

    private void loadCommits(int project_id, ArrayList<Integer> commit_ids){
        for(int i = 1; i <= TABLE_NUM; i++)
            commit_ids.addAll(langCompetenceMapper.getCommits(project_id, i));
    }

    private void updateCompetence(Map<Integer, Map<String, Integer>> LangContributions){
        for(Map.Entry<Integer, Map<String, Integer>> LangContribution : LangContributions.entrySet())
            langCompetenceMapper.updateCompetenceByUser(LangContribution.getKey(), LangContribution.getValue());
    }

}

class LangCompetenceMultiThread implements Runnable{


    private LangCompetenceMapper lcm;
    private ArrayList<Integer> project_ids;
    private int id;

    public LangCompetenceMultiThread(int tid, ArrayList<Integer> pids, LangCompetenceMapper langCompetenceMapper){
        project_ids = pids;
        id = tid;
        lcm = langCompetenceMapper;
    }

    public void run(){
        LangCompetence langCompetence = new LangCompetence();
        langCompetence.id = id;
        langCompetence.project_ids = project_ids;
        langCompetence.langCompetenceMapper = lcm;
        langCompetence.calculate();
    }

}