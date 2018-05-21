package cn.springmvc.service;

import cn.springmvc.dao.ProjectFilterMapper;
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

    @Autowired
    public ProjectFilterMapper projectFilterMapper;

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


    public void projectFilter1(){
        ProjectFilter projectFilter1 = new ProjectFilter(projectFilterMapper);
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


    public void test(){
    }
}
