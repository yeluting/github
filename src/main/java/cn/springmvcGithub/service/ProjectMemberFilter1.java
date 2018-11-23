package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.ProjectMemberMapper;
import cn.springmvcGithub.model.ProjectMember;

import java.util.ArrayList;

/**
 * Created by YLT on 2018/6/4.
 */
public class ProjectMemberFilter1 implements Runnable {
    ProjectMemberMapper projectMemberMapper;
    ArrayList<Integer> projectFilter1;
    private Object mutex = new Object();

    public ProjectMemberFilter1(ProjectMemberMapper projectMemberMapper) {
        this.projectMemberMapper = projectMemberMapper;
        projectFilter1 = projectMemberMapper.getProjectSFilter1();
        System.out.println(projectFilter1.size());
    }

    public void run() {
        int k = 0;
        while (projectFilter1.size() > 0) {
            ArrayList<Integer> calList;
            int calLength = -1;
            synchronized (mutex) {
                if (projectFilter1.size() > 500) {
                    calLength = 500;
                } else {
                    calLength = projectFilter1.size();
                }
                calList = new ArrayList<Integer>(projectFilter1.subList(0, calLength));
                projectFilter1.subList(0, calLength).clear();
            }

            //这边再加一些处理的代码
            ArrayList<ProjectMember> projectMembers = projectMemberMapper.getProjectMemberBatch(calList);

            if (projectMembers.size()>0) {
                projectMemberMapper.insertProjectMemberFilter1(projectMembers);
                System.out.println(projectMembers.size());
            }
        }
    }


}
