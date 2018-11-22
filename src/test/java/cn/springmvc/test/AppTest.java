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
        for(int disId = 5; disId <= 8; disId++){
            recommend.setDisID(disId);
            recommend.loadExpDataSet();
            for(int recType = 0; recType < 4; recType++) {
                recommend.setRecType(recType);
                int[] tmp = new int[recommend.memberNeeded.length];
                for (int i = 0; i < tmp.length; i++) tmp[i] = recommend.memberNeeded[i];
                int i = 0;
                for (int userId : recommend.users) {
                    recommend.recommend(userId, "github", recommend.memberNeeded, recommend.skills);
                    System.out.printf("SET%d DIS%d RecType%d : %d/%d\n", 1, disId, recType, ++i, recommend.users.size());
                    for (int j = 0; j < tmp.length; j++) recommend.memberNeeded[j] = tmp[j];
                }
            }
        }
    }

}
