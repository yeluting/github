package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YLT on 2018/5/21.
 */
public interface ProjectFilterMapper {
    void insertProjects_1(List<Project> projects);
    ArrayList<Integer> getProjectsFromWatcherNum();
    ArrayList<Project> getProjectBatch(ArrayList<Integer> projectIds);
}
