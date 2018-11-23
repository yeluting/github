package cn.springmvcGithub.dao;

import cn.springmvcGithub.model.Commit;

import java.util.ArrayList;

/**
 * Created by YLT on 2018/6/4.
 */
public interface CommitsFilterMapper {
    ArrayList<Integer> getProjectSFilter1();
    ArrayList<Commit> getCommitsFilter1(ArrayList<Integer> projectIds);
    void insertProjectCommitFilter1(ArrayList<Commit> CommitsFilter);
}
