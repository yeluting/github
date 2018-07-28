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
    private Intimacy intimacy;

    @Autowired
    private LangFilter langFilter;

    @Autowired
    private Cooperation cooperation;

    @Autowired
    private TeamRecord teamRecord;

    @Autowired
    private LangAbility langAbility;

    @Test
    public void test() {
//        cooperation.calculate(10000, 1);
//        teamRecord.insert(1);
//        teamRecord.calculateAbility();
        langAbility.normalization();
    }


}
