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
    public TeamRecord teamRecord;

    @Autowired
    public SocIntimacy socIntimacy;

    @Autowired
    public Intimacy intimacy;

    @Test
    public void test() {
        teamRecord.calculateIntimacy(1);
//        socIntimacy.calculate(16,100000, 10000);
//        intimacy.calculate(10000, 16);
//        teamRecord.calculateAbility();
    }


}
