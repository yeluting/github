package cn.springmvc.service;

import cn.springmvc.dao.ProjectFilterMapper;
import cn.springmvc.model.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by YLT on 2018/5/21.
 */
class ProjectFilter1 implements Runnable{
    ProjectFilterMapper projectFilterMapper;
    ArrayList<Integer> projectWatchersLT5;
    private Object mutex = new Object();

    //从初始表project中选取watcher_num > 5的项目,并且fork=null
    public ProjectFilter1(ProjectFilterMapper projectFilterMapper){
        this.projectFilterMapper = projectFilterMapper;
        projectWatchersLT5 = projectFilterMapper.getProjectsFromWatcherNum();
        System.out.print(projectWatchersLT5.size());
    }

    public void run() {
        int k =0;
        while (projectWatchersLT5.size() > 0){
            System.out.println(k++);
            ArrayList<Integer> calList;
            int calLength = -1;
            synchronized (mutex){
                if (projectWatchersLT5.size() > 500){
                    calLength = 500;
                }else {
                    calLength = projectWatchersLT5.size();
                }
                calList = new ArrayList<Integer>(projectWatchersLT5.subList(0,calLength));
                projectWatchersLT5.subList(0,calLength).clear();
            }

            //这边再加一些处理的代码
            ArrayList<Project> projectLans= projectFilterMapper.getProjectBatch(calList);
            this.projectFilterMapper.insertProjects_1(projectLans);
        }
    }
}
