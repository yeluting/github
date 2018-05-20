package cn.springmvc.service;

import cn.springmvc.dao.ProjectLanguageMapper;
import cn.springmvc.model.ProjectLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by YLT on 2018/5/5.
 */
@Service
public class ProjectAnalysis {

   @Autowired
   public ProjectLanguageMapper projectLanguageMapper;

    public void calculate(){
        LanguagePercentCal lan = new LanguagePercentCal(projectLanguageMapper);

        Thread[] threads = new Thread[8];
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
        }
    }


    public void test(){
        ArrayList<Integer> projects = new ArrayList<Integer>();
//        projects.add(361289);
//        projects.add(350058);
//        projects.add(58021);
        projects.add(1);
        projects.add(2);
        projects.add(3);
        projects.add(5);
        projects.add(6);
        ArrayList<String> result = projectLanguageMapper.getProjectLanBatch(projects);
        for (int i = 0; i < result.size();i++){
            System.out.println(result.get(i));
        }
        /*ProjectLanguage pl1 = new ProjectLanguage();
        pl1.setProjectId(1);
        pl1.setLanguage("ruby");
        pl1.setPercent(0.2);
        ProjectLanguage pl2 = new ProjectLanguage();
        pl2.setProjectId(1);
        pl2.setLanguage("javascript");
        pl2.setPercent(0.8);
        ArrayList<ProjectLanguage> toUpdate = new ArrayList<ProjectLanguage>();
        toUpdate.add(pl1);
        toUpdate.add(pl2);
        projectLanguageMapper.updateLanguagePercentBatch(toUpdate);*/

    }
}
