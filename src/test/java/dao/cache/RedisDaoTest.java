package dao.cache;

import dao.SeckillDao;
import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest{

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception{

        Seckill seckill=redisDao.getSeckill(1001);
        if (seckill == null){
            seckill=seckillDao.queryById(1001);
            if (seckill!=null){
                String result=redisDao.putSeckill(seckill);
                System.out.println(result);
                Seckill e=redisDao.getSeckill(1001);
                System.out.println(e);
            }
        }
    }
}