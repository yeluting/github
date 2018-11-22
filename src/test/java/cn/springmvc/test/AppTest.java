package cn.springmvc.test;

import cn.springmvc.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:conf/applicationContext.xml")

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Autowired
    private Recommend recommend;

    @Test
    public void test() {
        recommend.setSetID(1);
        recommend.setDisID(1);
        recommend.setRecType(0);
        List<Integer> users = new ArrayList<Integer>();
        String[] skills = new String[4];
        int[] memberNeeded = new int[4];
        recommend.loadExpDataSet(users, skills, memberNeeded);
        int i = 0;
        for(int userId : users) {
            System.out.printf("%d/%d\n", ++i, users.size());
            recommend.recommend(userId, "github", memberNeeded, skills);
        }
    }

}
