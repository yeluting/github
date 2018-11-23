package cn.springmvcGithub.service;

import cn.springmvcGithub.dao.ProjectLanguageMapper;
import cn.springmvcGithub.model.ProjectLanguage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YLT on 2018/5/5.
 */
class LanguagePercentCal implements Runnable{

    private ArrayList<Integer> allProjectIds;
    private Object mutex = new Object();

    ProjectLanguageMapper projectLanguageMapper;

    public LanguagePercentCal(ProjectLanguageMapper projectLanguageMapper){
        this.projectLanguageMapper = projectLanguageMapper;
       // allProjectIds = projectLanguageMapper.getAllProjectIds();
        allProjectIds = projectLanguageMapper.getProjectIdsFilter1();
        System.out.println(allProjectIds.size());
    }

    public void run() {
        int k =0;
        while (allProjectIds.size() > 0){
            System.out.println(k++);
            ArrayList<Integer> calList;
            int calLength = -1;
            synchronized (mutex){
                if (allProjectIds.size() > 200){
                    calLength = 200;
                }else {
                    calLength = allProjectIds.size();
                }
                calList = new ArrayList<Integer>(allProjectIds.subList(0,calLength));
                allProjectIds.subList(0,calLength).clear();
                //allProjectIds.removeAll(calList);
            }

            ArrayList<ProjectLanguage> toUpdate = new ArrayList<ProjectLanguage>();
            //这边再加一些处理的代码
            ArrayList<String> projectLans= projectLanguageMapper.getProjectLanBatch(calList);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int size = projectLans.size();

            for (int i = 0; i < size; i ++){
                String[] strs = projectLans.get(i).split("==");
                String [] languages = strs[1].split("%%");
                String[] byteStr = strs[2].split("%%");
                int languageTypes = byteStr.length;
                int byteSum= 0;
                int [] bytes = new int[languageTypes];
                double [] bytePercent = new double[languageTypes];
                for (int j = 0; j < languageTypes; j ++){
                    bytes[j] = Integer.parseInt(byteStr[j]);
                    byteSum += bytes[j];
                }

                if (byteSum == 0 || languages.length!= byteStr.length){
                    continue;
                }
                for (int j = 0; j < languageTypes; j ++){
                    try {
                        bytePercent[j] = bytes[j]/(double)byteSum;
                        ProjectLanguage temp = new ProjectLanguage();
                        temp.setProjectId(Integer.valueOf(strs[0]));
                        temp.setLanguage(languages[j]);
                        temp.setPercent(bytePercent[j]);
                        temp.setBytes(bytes[j]);
                        try {
                            temp.setCreatedAt(sdf.parse(strs[3]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        toUpdate.add(temp);
                    }catch (Exception e){
                        System.out.println("projectId:"+strs[0]);
                    }
                }
            }

            if (toUpdate.size() > 0) {
                try {
                    projectLanguageMapper.insertProjectLanguageFilter1(toUpdate);
                }catch (Exception e){
                    for (int cal:calList){
                        System.out.print(cal +";");
                    }
                    System.out.println();
                }
            }
            //projectLanguageMapper.updateLanguagePercentBatch(toUpdate);
        }
    }


    //删除被筛选出项目中，无language占比信息的project
    public void deleteProjectWOLanguage(){
        ArrayList<HashMap<Object ,Object >> projectWithPercentTotal = projectLanguageMapper.getLanguagePercentTotal();

        int length = projectWithPercentTotal.size();
        for (int i = 0 ; i < length; i ++){
            int projectId = Integer.parseInt((projectWithPercentTotal.get(i).get("projectId").toString()));
            double percentTotal = Double.parseDouble(projectWithPercentTotal.get(i).get("total").toString());

            if (percentTotal < 0.5){
                projectLanguageMapper.deleteProjectWOLan(projectId);
            }
        }
    }
}
