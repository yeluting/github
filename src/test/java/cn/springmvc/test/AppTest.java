package cn.springmvc.test;

import cn.springmvcGithub.service.LangCompetence;
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
    private LangCompetence langCompetence;

    @Test
    public void test() {
        langCompetence.calculate("projectAna", "project_id");
    }

}
