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
        recommend.loadExpDataSet();
        int[] tmp = new int[recommend.memberNeeded.length];
        for(int i = 0; i < tmp.length; i++) tmp[i] = recommend.memberNeeded[i];
        int i = 0;
        for(int userId : recommend.users) {
            recommend.recommend(userId, "github", recommend.memberNeeded, recommend.skills);
            System.out.printf("%d/%d\n", ++i, recommend.users.size());
            for(int j = 0; j < tmp.length; j++) recommend.memberNeeded[j] = tmp[j];
        }
    }

}
