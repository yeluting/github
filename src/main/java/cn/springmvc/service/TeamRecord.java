package cn.springmvc.service;

import cn.springmvc.dao.IntimacyMapper;
import cn.springmvc.dao.TeamRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamRecord {

    @Autowired
    private TeamRecordMapper teamRecordMapper;

    @Autowired
    private IntimacyMapper intimacyMapper;

    @Autowired
    private Intimacy intimacy;

    final private String[] heads = {"project_id", "members", "team", "member_id", "project_id", "teamMemberSum", "teamMember"};

    public void insert(int max_thread){
        ArrayList<Integer> pids = teamRecordMapper.getProjectId("projects_3");
        int size = pids.size() / 1000;
        List<Map<String, Object>> teamrecords = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> memberteams = new ArrayList<Map<String, Object>>();
        int init_thread = Thread.activeCount();
        int i = 0, total = 0;
        for(int pid : pids){
            ArrayList<Integer> committers = teamRecordMapper.getCommitter(pid);
            Map<String, Object> teamrecord = new HashMap<String, Object>();
            teamrecord.put(heads[0], pid);
            String members = "";
            for(int committer : committers) members += String.format("%d&", committer);
            teamrecord.put(heads[1], members);
            teamrecord.put(heads[2], committers.size() <= 1 ? 0 : 1);
            teamrecords.add(teamrecord);
            for(int committer : committers) {
                Map<String, Object> memberteam = new HashMap<String, Object>();
                memberteam.put(heads[3], committer);
                memberteam.put(heads[4], pid);
                memberteam.put(heads[5], committers.size());
                memberteam.put(heads[6], members);
                memberteams.add(memberteam);
            }
            i++;
            total++;
            if(i >= size){
                System.out.printf("%d / %d : %d, %d\n", total, pids.size(), teamrecords.size(), memberteams.size());
                TeamRecordInsertDB teamRecordInsertDB_member = new TeamRecordInsertDB(memberteams, teamRecordMapper, 0);
                TeamRecordInsertDB teamRecordInsertDB_team = new TeamRecordInsertDB(teamrecords, teamRecordMapper, 1);
                while(Thread.activeCount() >= max_thread * 2+ init_thread);
                new Thread(teamRecordInsertDB_member).start();
                new Thread(teamRecordInsertDB_team).start();
                i = 0;
                teamrecords = new ArrayList<Map<String, Object>>();
                memberteams = new ArrayList<Map<String, Object>>();
            }
        }
        if(!teamrecords.isEmpty()){
            TeamRecordInsertDB teamRecordInsertDB_member = new TeamRecordInsertDB(memberteams, teamRecordMapper, 0);
            TeamRecordInsertDB teamRecordInsertDB_team = new TeamRecordInsertDB(teamrecords, teamRecordMapper, 1);
            while(Thread.activeCount() >= max_thread * 2+ init_thread);
            new Thread(teamRecordInsertDB_member).start();
            new Thread(teamRecordInsertDB_team).start();
        }
        while(Thread.activeCount() != init_thread);
        return;
    }

    public void calculateAbility(){
        int total = 0;
        List<Map<String, Object>> teamRecords = teamRecordMapper.getTeamRecord();
        for(Map<String, Object> teamRecord : teamRecords){
            int project_id = (Integer) teamRecord.get("project_id");
            String[] memberString = ((String) teamRecord.get("members")).split("&");
            int[] members = new int[memberString.length];
            for(int i = 0; i < memberString.length; i++) members[i] = Integer.parseInt(memberString[i]);
            List<String> langs = teamRecordMapper.getProjectLang(project_id);
            if(langs.size() == 0) continue;
            double[][] tmpLangAbility = new double[members.length][langs.size()];
            int avaLength = members.length;
            for(int i = 0; i < members.length; i++) {
                try {
                    tmpLangAbility[i] = teamRecordMapper.getLangAbility(members[i], langs);
                    if (tmpLangAbility[i].length == 0) avaLength--;
                }catch (Exception ex){
                    System.out.println(++total + " " + ex.toString());
                }
            }
            if(avaLength <= 1) continue;
            int[] avaMembers = new int[avaLength];
            double[][] LangAbility = new double[avaLength][langs.size()];
            for(int i = 0, j = 0; i < members.length; i++){
                if(tmpLangAbility[i].length == 0) continue;
                else{
                    LangAbility[j] = tmpLangAbility[i];
                    avaMembers[j++] = members[i];
                }
            }
            double[] growSpace = new double[avaLength];
            double[] abilityDiff = new double[avaLength];
            getGrowSpace(LangAbility, growSpace);
            getAbilityDiff(LangAbility, abilityDiff);
            for(int i = 0; i < avaLength; i++)
                teamRecordMapper.updateGrowDiff(avaMembers[i], project_id, growSpace[i], abilityDiff[i]);
        }
    }

    public void calculateIntimacy(){
        List<Integer> project_ids = teamRecordMapper.getTeamRecordAnalysisProject();
        int i = 0;
        for(int project_id : project_ids){
            Map<Integer, Double> values = intimacy.calculateTeam(project_id);
            for(Map.Entry<Integer, Double> value : values.entrySet())
                teamRecordMapper.updateCost(project_id, value.getKey(), value.getValue());
            System.out.printf("%d / %d\n", ++i, project_ids.size());
        }
    }

    public void getGrowSpace(double[][] LangAbility, double[] growSpace){
        int n = LangAbility.length;
        int m = LangAbility[0].length;
        double[] TeamAbility = new double[m];
        for(int i = 0; i < m; i++){
            TeamAbility[i] = 1;
            double tmp = 1;
            for(int j = 0; j < n; j++) tmp *= (1 - LangAbility[j][i]);
            TeamAbility[i] -= tmp;
        }
        for(int i = 0; i < n; i++){
            growSpace[i] = 0;
            for(int j = 0; j < m; j++) growSpace[i] += (TeamAbility[j] - LangAbility[i][j]) * LangAbility[i][j];
            growSpace[i] /= m;
        }
    }

    public void getAbilityDiff(double[][] LangAbility, double[] abilityDiff){
        int n = LangAbility.length;
        double[][] memberDiff = new double[n][n];
        for(int i = 0; i < n; i++){
            memberDiff[i][i] = 0;
            for(int j = i + 1; j < n; j++){
                double tmp = diff(LangAbility[i], LangAbility[j]);
                memberDiff[i][j] = tmp;
                memberDiff[j][i] = tmp;
            }
        }
        for(int i = 0; i < n; i++){
            abilityDiff[i] = 0;
            for(int j = 0; j < n; j++) abilityDiff[i] += memberDiff[i][j];
            abilityDiff[i] /= (n - 1);
        }
    }

    public double diff(double[] abilityA, double[] abilityB){
        double sum = 0;
        for(int i = 0; i < abilityA.length; i++) sum += Math.pow(abilityA[i] - abilityB[i], 2);
        return Math.sqrt(sum);
    }

}

class TeamRecordInsertDB implements Runnable{

    private TeamRecordMapper teamRecordMapper;
    private List<Map<String, Object>> teamrecords;
    private List<Map<String, Object>> memberteams;

    public TeamRecordInsertDB(List<Map<String, Object>> map, TeamRecordMapper teamRecordMapper, int flag){
        this.teamRecordMapper = teamRecordMapper;
        memberteams = null;
        teamrecords = null;
        if(flag == 0) {
            memberteams = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> m : map) {
                Map<String, Object> tmp = new HashMap<String, Object>();
                for (Map.Entry<String, Object> member : m.entrySet())
                    tmp.put(member.getKey(), member.getValue());
                memberteams.add(tmp);
            }
        }else if(flag == 1){
            teamrecords = new ArrayList<Map<String, Object>>();
            for(Map<String, Object> t : map){
                Map<String, Object> tmp = new HashMap<String, Object>();
                for(Map.Entry<String, Object> record : t.entrySet())
                    tmp.put(record.getKey(), record.getValue());
                teamrecords.add(tmp);
            }
        }
    }

    public void run(){
        try {
            if (teamrecords == null) teamRecordMapper.insertMembers(memberteams);
            else if (memberteams == null) teamRecordMapper.insertTeams(teamrecords);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}


