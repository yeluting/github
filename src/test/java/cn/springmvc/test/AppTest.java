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
    private RecommendGithub recommendGithub;

    @Test
    public void test() {
        for(int setId = 1; setId <= 3; setId++) {
            recommendGithub.setSetID(setId);
            int disId = 1;
            recommendGithub.setDisID(disId);
            recommendGithub.loadExpDataSet();
            for (int recType = 0; recType < 4; recType++) {
                recommendGithub.setRecType(recType);
                int[] tmp = new int[recommendGithub.memberNeeded.length];
                for (int i = 0; i < tmp.length; i++) tmp[i] = recommendGithub.memberNeeded[i];
                int i = 0;
                for (int userId : recommendGithub.users) {
                    recommendGithub.recommend(userId, "github", recommendGithub.memberNeeded, recommendGithub.skills);
                    System.out.printf("SET%d DIS%d RecType%d : %d/%d\n", setId, disId, recType, ++i, recommendGithub.users.size());
                    for (int j = 0; j < tmp.length; j++) recommendGithub.memberNeeded[j] = tmp[j];
                }
            }
        }
    }

}
