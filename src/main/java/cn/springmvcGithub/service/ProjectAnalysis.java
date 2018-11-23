package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.CommitsFilterMapper;
import cn.springmvcGithub.dao.ProjectFilterMapper;
import cn.springmvcGithub.dao.ProjectLanguageMapper;
import cn.springmvcGithub.dao.ProjectMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by YLT on 2018/5/5.
 */
@Service
public class ProjectAnalysis {

   @Autowired
   public ProjectLanguageMapper projectLanguageMapper;

    @Autowired
    public ProjectFilterMapper projectFilterMapper;

    @Autowired
    public ProjectMemberMapper projectMemberMapper;

    @Autowired
    public CommitsFilterMapper commitsFilterMapper;

    //计算项目语言占比
    public void languageCalculate(){
        LanguagePercentCal lan = new LanguagePercentCal(projectLanguageMapper);
        lan.deleteProjectWOLanguage();

        /*Thread[] threads = new Thread[6];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(lan);
            threads[i].start();
        }

        for (Thread thread: threads ) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }


    //filter1筛选project
    public void projectFilter1(){
        ProjectFilter1 projectFilter1 = new ProjectFilter1(projectFilterMapper);
        Thread[] threads = new Thread[2];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(projectFilter1);
            threads[i].start();
        }

        for (Thread thread: threads ) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //获得filter1方式中筛选获得的project中，project的members。
    public void getProjectMembersFilter1(){
        ProjectMemberFilter1 projectMemberFilter1 = new ProjectMemberFilter1(projectMemberMapper);
        Thread[] threads = new Thread[1];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(projectMemberFilter1);
            threads[i].start();
        }

        for (Thread thread: threads ) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getProjectCommitsFilter1(){
        CommitsFilter1 commitsFilter1 = new CommitsFilter1(commitsFilterMapper);
        Thread[] threads = new Thread[6];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(commitsFilter1);
            threads[i].start();
        }

        for (Thread thread: threads ) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
