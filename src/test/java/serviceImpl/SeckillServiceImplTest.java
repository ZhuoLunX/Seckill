package serviceImpl;

import dao.SeckillDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillServiceImplTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testKillSeckillProcedure(){
        long id=1003;
        long phone=1857113965;

    }
}