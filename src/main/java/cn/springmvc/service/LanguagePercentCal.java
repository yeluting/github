package cn.springmvc.service;

import cn.springmvc.dao.ProjectLanguageMapper;
import cn.springmvc.model.ProjectLanguage;
import java.util.ArrayList;

/**
 * Created by YLT on 2018/5/5.
 */
class LanguagePercentCal implements Runnable{

    private ArrayList<Integer> allProjectIds;
    private Object mutex = new Object();
    ProjectLanguageMapper projectLanguageMapper;

    public LanguagePercentCal(ProjectLanguageMapper projectLanguageMapper){
        allProjectIds = projectLanguageMapper.getAllProjectIds();
        System.out.println(allProjectIds.size());
        this.projectLanguageMapper = projectLanguageMapper;
    }

    public void run() {
        int k =0;
        while (allProjectIds.size() > 0){
            System.out.println(k++);
            ArrayList<Integer> calList;
            int calLength = -1;
            synchronized (mutex){
                if (allProjectIds.size() > 500){
                    calLength = 500;
                }else {
                    calLength = allProjectIds.size();
                }
                calList = new ArrayList<Integer>(allProjectIds.subList(0,calLength));
                allProjectIds.subList(0,calLength).clear();
                mutex.notify();
            }
            //这边再加一些处理的代码
            ArrayList<String> projectLans= projectLanguageMapper.getProjectLanBatch(calList);
            ArrayList<ProjectLanguage> toUpdate = new ArrayList<ProjectLanguage>();
            int size = projectLans.size();

            for (int i = 0; i < size; i ++){
                String[] strs = projectLans.get(i).split("==");
                String [] languages = strs[1].split("##");
                String[] byteStr = strs[2].split("##");
                int languageTypes = byteStr.length;
                int byteSum= 0;
                int [] bytes = new int[languageTypes];
                double [] bytePercent = new double[languageTypes];
                for (int j = 0; j < languageTypes; j ++){
                    bytes[j] = Integer.parseInt(byteStr[j]);
                    byteSum += bytes[j];
                }

                for (int j = 0; j < languageTypes; j ++){
                    bytePercent[i] = bytes[i]/(double)byteSum;
                    ProjectLanguage temp = new ProjectLanguage();
                    temp.setProjectId(Integer.valueOf(strs[0]));
                    temp.setLanguage(languages[j]);
                    temp.setPercent(bytePercent[j]);
                    toUpdate.add(temp);
                }
            }
            projectLanguageMapper.updateLanguagePercentBatch(toUpdate);
        }
    }
}
