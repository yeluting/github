package cn.springmvc.test;

import cn.springmvcGithub.service.RecommendGithub;
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
    private RecommendGithub recommendGithub;

    @Test
    public void test() {
        int[] memberNeeded = {1,4,1,4};
        String[] skills = {"css", "javascript", "html", "ruby"};
        System.out.println(recommendGithub.recommend(2, "github", memberNeeded, skills));
    }

}
