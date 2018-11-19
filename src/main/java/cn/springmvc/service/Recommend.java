package cn.springmvc.service;

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

    private boolean flag = false;

    public String recommend(int userId, String platform, int[] memberNeeded, String[] skills){
        if(!flag) dataPreLoad.loadData();
        System.out.println("Data Loaded\n");
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
                    double tmpSuccessRate = teamSuccessRate.getTeamSuccessRate(team, i + 2, chosenLang);
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
            System.out.printf("Teammate %d : %d %s\n", i + 1, maxUserId, skills[maxSkillIndex]);
        }
        return teamSuccessRate.getTeamDetail(team, totalMember + 1, chosenLang);
    }

}
