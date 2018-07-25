package cn.springmvc.service;

import cn.springmvc.dao.TeamRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamRecord {

    @Autowired
    private TeamRecordMapper teamRecordMapper;

    final private String[] heads = {"project_id", "members", "team", "member_id", "teamMemberSum", "teamMember"};

    public void insert(int max_thread){
        ArrayList<Integer> pids = teamRecordMapper.getProjectId("filter1_project_id");
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
                memberteam.put(heads[4], committers.size());
                memberteam.put(heads[5], members);
                memberteams.add(memberteam);
            }
            i++;
            total++;
            if(i >= size){
                System.out.printf("%d / %d : %d, %d\n", total, size, teamrecords.size(), memberteams.size());
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
        if(teamrecords == null) teamRecordMapper.insertMembers(memberteams);
        else if(memberteams == null) teamRecordMapper.insertTeams(teamrecords);
    }

}


