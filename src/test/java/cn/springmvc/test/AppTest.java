package cn.springmvc.test;

import cn.springmvc.service.*;
//import cn.springmvc.service.ProjectAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;

 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:conf/applicationContext.xml")

/**
 * Unit test for simple App.
 */
public class AppTest
{

    @Autowired
    private LangAbility langAbility;

    @Autowired
    private ProjectAnalysis projectAnalysis;

    @Autowired
    private Cooperation cooperation;

    @Autowired
    private Organization organization;

    @Test
    public void test(){
        langAbility.calculate(7);
//        cooperation.calculate(5000, 7);
//        organization.calculate(5000, 7);
//        projectAnalysis.getProjectCommitsFilter1();
//        gen.generate();
//        System.out.println("Test is available.");
    }


}
