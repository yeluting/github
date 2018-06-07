package cn.springmvc.service;

import cn.springmvc.dao.CommitsFilterMapper;
import cn.springmvc.model.Commit;

import java.util.ArrayList;

/**
 * Created by YLT on 2018/6/4.
 */
public class CommitsFilter1 implements Runnable{
    CommitsFilterMapper commitsFilterMapper;
    ArrayList<Integer> projectIdFilter1;
    private Object mutex = new Object();

    public CommitsFilter1(CommitsFilterMapper commitsFilterMapper){
        this.commitsFilterMapper = commitsFilterMapper;
        projectIdFilter1 = commitsFilterMapper.getProjectSFilter1();
        System.out.println(projectIdFilter1.size());
    }

    public void run(){
        int k = 0;
        while (projectIdFilter1.size() > 0){
            System.out.println(++k);
            int calLength = -1;
            ArrayList<Integer> calList;
            synchronized(mutex){
                if (projectIdFilter1.size() > 1000) {
                    calLength = 1000;
                } else {
                    calLength = projectIdFilter1.size();
                }
                calList = new ArrayList<Integer>(projectIdFilter1.subList(0, calLength));
                projectIdFilter1.subList(0, calLength).clear();
            }

            ArrayList<Commit> commitArr = commitsFilterMapper.getCommitsFilter1(calList);
            System.out.println(commitArr.size());
            commitsFilterMapper.insertProjectCommitFilter1(commitArr);
        }
    }

}
