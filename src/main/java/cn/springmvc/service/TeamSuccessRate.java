package cn.springmvc.service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TeamSuccessRate {

    @Autowired
    private TeamRecord teamRecord;

    @Autowired
    private Intimacy intimacy;

    final private double[] addCoef = {5,1,5};

    public double getTeamSuccessRate(int[] team, int teamSize, Set<String> chosenLang){
        double[][] memberCompetence = new double[teamSize][chosenLang.size()];
        loadCompetence(team, teamSize, chosenLang, memberCompetence);

        double[] memberCost = new double[teamSize];
        double[] memberDiff = new double[teamSize];
        double[] memberGrow = new double[teamSize];
        intimacy.calculateTeam(team, teamSize, memberCost);
        teamRecord.getAbilityDiff(memberCompetence, memberDiff);
        teamRecord.getGrowSpace(memberCompetence, memberGrow);

        double successRate = 0.0;
        for(int i = 0; i < teamSize; i++){
            double[] coef = DataPreLoad.Coefficient.get(team[i]);
            successRate +=
                    coef[0] * addCoef[0] * (1 - memberCost[i]) +
                    coef[1] * addCoef[1] * (1 - memberDiff[i]) +
                    coef[2] * addCoef[2] * memberGrow[i];
        }
        return successRate;
    }

    public double getTeamSuccessRate_Closeness(int[] team, int teamSize, Set<String> chosenLang){
        double[] memberCost = new double[teamSize];
        intimacy.calculateTeam(team, teamSize, memberCost);
        double successRate = 0.0;
        for(int i = 0; i < teamSize; i++){
            successRate += addCoef[0] * (1 - memberCost[i]);
        }
        return successRate;
    }

    public double getTeamSuccessRate_Diff(int[] team, int teamSize, Set<String> chosenLang){
        double[][] memberCompetence = new double[teamSize][chosenLang.size()];
        loadCompetence(team, teamSize, chosenLang, memberCompetence);
        double[] memberDiff = new double[teamSize];
        teamRecord.getAbilityDiff(memberCompetence, memberDiff);
        double successRate = 0.0;
        for(int i = 0; i < teamSize; i++){
            successRate += addCoef[1] * (1 - memberDiff[i]);
        }
        return successRate;
    }

    public double getTeamSuccessRate_Grow(int[] team, int teamSize, Set<String> chosenLang){
        double[][] memberCompetence = new double[teamSize][chosenLang.size()];
        loadCompetence(team, teamSize, chosenLang, memberCompetence);
        double[] memberGrow = new double[teamSize];
        teamRecord.getGrowSpace(memberCompetence, memberGrow);
        double successRate = 0.0;
        for(int i = 0; i < teamSize; i++){
            successRate += addCoef[2] * memberGrow[i];
        }
        return successRate;
    }

    private void loadCompetence(int[] team, int teamSize, Set<String> langs, double[][] memberCompetence){
        for(int i = 0; i < teamSize; i++){
            double[] wholeComp = DataPreLoad.Competence.get(team[i]);
            memberCompetence[i] = new double[langs.size()];
            int j = 0;
            for(String lang : langs)
                memberCompetence[i][j++] = wholeComp[DataPreLoad.LangIndexMap.get(lang)];
        }
    }

    public JSONObject getTeamDetail(int[] team, int teamSize, Set<String> chosenLang){
        JSONObject resultJson = new JSONObject();
        double[][] memberCompetence = new double[teamSize][chosenLang.size()];
        loadCompetence(team, teamSize, chosenLang, memberCompetence);

        double[] memberCost = new double[teamSize];
        double[] memberDiff = new double[teamSize];
        double[] memberGrow = new double[teamSize];
        intimacy.calculateTeam(team, teamSize, memberCost);
        teamRecord.getAbilityDiff(memberCompetence, memberDiff);
        teamRecord.getGrowSpace(memberCompetence, memberGrow);
        String[] teamMemberStr = new String[teamSize];
        double successRate = 0.0;
        for(int i = 0; i < teamSize; i++){
//            System.out.println(memberCost[i]+","+memberDiff[i]+","+memberGrow[i]);
            JSONObject memberJson = new JSONObject();
            String memberId = String.format("%d", team[i]);
            double[] coef = DataPreLoad.Coefficient.get(team[i]);
            double tmpsuccessRate =
                    coef[0] * addCoef[0] * (1 - memberCost[i]) +
                    coef[1] * addCoef[1] * (1 - memberDiff[i]) +
                    coef[2] * addCoef[2] * memberGrow[i];
            successRate += tmpsuccessRate;
            memberJson.put("closeness", 1 - memberCost[i]);
            memberJson.put("diff", memberDiff[i]);
            memberJson.put("grow", memberGrow[i]);
            memberJson.put("willEach", tmpsuccessRate);
            memberJson.put("closenessPercent", coef[0]);
            memberJson.put("diffPercent", coef[1]);
            memberJson.put("growPercent", coef[2]);
            resultJson.put(memberId, memberJson);
            teamMemberStr[i] = memberId;
        }
        resultJson.put("members", teamMemberStr);
        System.out.println(successRate / teamSize);
        resultJson.put("willingness", successRate / teamSize);
        return resultJson;
    }

}
