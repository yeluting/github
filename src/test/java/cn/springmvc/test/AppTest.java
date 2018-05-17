package cn.springmvc.test;

import cn.springmvc.service.Ability;
import cn.springmvc.service.Gen;
//import cn.springmvc.service.ProjectAnalysis;
import cn.springmvc.service.ProjectAnalysis;
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
    private Ability ability;

    @Autowired
    private ProjectAnalysis projectAnalysis;

    @Test
    public void test(){
        projectAnalysis.calculate();
        //gen.generate();
        //System.out.println("Test is available.");
    }

    @Test
    public void test1(){
        projectAnalysis.test();
        //gen.generate();
        //System.out.println("Test is available.");
    }

}
