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

    @Autowired
    private LangGroup langGroup;

    @Test
    public void test() {
//        recommend.setSetID(1);
//        recommend.setRecType(0);
//        int[] memberNeeded = {2,1,2};
//        String[] skills = {"javascript", "shell", "ruby"};
//        System.out.println(recommend.recommend(2, "github", memberNeeded, skills));
        langGroup.group(4);
    }

}
