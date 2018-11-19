package cn.springmvc.test;

import cn.springmvc.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

 
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
        int[] memberNeeded = {2,1,2};
        String[] skills = {"javascript", "shell", "ruby"};
        System.out.println(recommend.recommend(2, "github", memberNeeded, skills));
    }

}
