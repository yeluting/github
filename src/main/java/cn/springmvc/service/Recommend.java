package cn.springmvc.service;

import cn.springmvc.dao.RecommendMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Recommend {

    @Autowired
    private DataPreLoad dataPreLoad;

    @Autowired
    private RecommendSet recommendSet;

    @Autowired
    private TeamSuccessRate teamSuccessRate;

    @Autowired
    private RecommendMapper recommendMapper;

    private int SetID = 1;
    private int DisID = 1;
    private int RecType = 0;
    private boolean flag = false;

    public List<Integer> users = new ArrayList<Integer>();
    public String[] skills = new String[4];
    public int[] memberNeeded = new int[4];

    public void setRecType(int Type){
        RecType = Type % 4;
    }

    public void setSetID(int id){
        SetID = id;
    }

    public void setDisID(int id){
        DisID = id;
    }

    public void loadExpDataSet(){
        users = recommendMapper.loadUsers(SetID);
        Map<String, Object> tmp = recommendMapper.loadSkills(SetID, DisID).get(0);
        int size = (Integer) tmp.get("langSize");
        skills = new String[size];
        memberNeeded = new int[size];
        for(int i = 1; i <= size; i++){
            skills[i - 1] = (String) tmp.get(String.format("L%d", i));
            memberNeeded[i - 1] = (Integer) tmp.get(String.format("L%d_num", i));
        }
    }

    public String recommend(int userId, String platform, int[] memberNeeded, String[] skills){
        if(!flag) {
            dataPreLoad.loadData();
            flag = true;
            System.out.println("Data Loaded\n");
        }
        return recommendWeb(userId, platform, memberNeeded, skills).toString();
    }

    public JSONObject recommendWeb(int userId, String platform, int[] memberNeeded, String[] skills){
        int totalMember = 0;
        for(int memberN : memberNeeded) totalMember += memberN;
        int[] team = new int[totalMember + 1];
        team[0] = userId;
        Map<String, Set<Integer>> rSetByLangs = recommendSet.RSetByLangs(userId, skills);
        Set<String> chosenLang = new HashSet<String>();
        Set<Integer> chosen = new HashSet<Integer>();
        String memberString = String.format("%d", userId);
        for(int i = 0; i < totalMember; i++){
            double maxSuccessRate = -1;
            int maxSkillIndex = -1;
            int maxUserId = -1;
            for(int j = 0; j < skills.length; j++){
                if(memberNeeded[j] == 0) continue;
                boolean newAdded = false;
                if(!chosenLang.contains(skills[j])) {
                    newAdded = true;
                    chosenLang.add(skills[j]);
                }
                Set<Integer> rSet = rSetByLangs.get(skills[j]);
                for(int developer : rSet){
                    if(chosen.contains(developer)) continue;
                    team[i + 1] = developer;
                    double tmpSuccessRate = 0.0;
                    switch(RecType){
                        case 0: tmpSuccessRate = teamSuccessRate.getTeamSuccessRate(team, i + 2, chosenLang); break;
                        case 1: tmpSuccessRate = teamSuccessRate.getTeamSuccessRate_Closeness(team, i + 2, chosenLang); break;
                        case 2: tmpSuccessRate = teamSuccessRate.getTeamSuccessRate_Diff(team, i + 2, chosenLang); break;
                        case 3: tmpSuccessRate = teamSuccessRate.getTeamSuccessRate_Grow(team, i + 2, chosenLang); break;
                        default: tmpSuccessRate = teamSuccessRate.getTeamSuccessRate(team, i + 2, chosenLang);
                    }
                    if(tmpSuccessRate > maxSuccessRate){
                        maxSuccessRate = tmpSuccessRate;
                        maxSkillIndex = j;
                        maxUserId = developer;
                    }
                }
                if(newAdded) chosenLang.remove(skills[j]);
            }
            memberNeeded[maxSkillIndex]--;
            if(!chosenLang.contains(skills[maxSkillIndex]))
                chosenLang.add(skills[maxSkillIndex]);
            team[i + 1] = maxUserId;
            chosen.add(maxUserId);
            memberString += String.format("&%d", maxUserId);
        }
        JSONObject teamDetail = teamSuccessRate.getTeamDetail(team, totalMember + 1, chosenLang);
        JSONObject beRecommended = teamDetail.getJSONObject(String.format("%d", userId));
        recommendMapper.saveRecResult(SetID, DisID, RecType, userId, memberString,
                beRecommended.getDouble("closeness"), beRecommended.getDouble("diff"),
                beRecommended.getDouble("grow"), teamDetail.getDouble("willingness"));
        return teamDetail;
    }

}
