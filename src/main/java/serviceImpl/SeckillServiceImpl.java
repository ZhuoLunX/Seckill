package serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import dao.cache.RedisDao;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import service.SeckillService;
import dao.SeckillDao;
import dao.SuccessKilledDao;
import dto.ExecutionSeckill;
import dto.Exposer;
import entity.Seckill;
import entity.SuccessKilled;
import enums.SeckillStatEnum;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;

/**
 * 秒杀业务实现类
 *
 * @author xiongzhuolun
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    // 日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // MD5混淆盐值
    private final String slat = "fafknkn#$#%*(#%@";

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    /**
     * 使用注解控制事务的优点
     * 1：开发团队达成一致约定，明确标志事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短,不要穿插其他操作，如RPC/http请求/或者剥离到事务方法外部
     * 3:不是所有的方法都需要事务,如只有一条修改操作，或者只读操作。
     */
    @Transactional
    public ExecutionSeckill executeSeckill(long seckillId, long userPhone,
                                           String md5) throws SeckillException, RepeatKillException,
            SeckillCloseException {
        try {
            // 获取秒杀数据
            Seckill seckill = seckillDao.queryById(seckillId);
            // 判断是否有此秒杀
            if (seckill == null) {
                throw new SeckillException("seckill not exsit");
            }
            // 校验秒杀
            if (md5 == null || !md5.equals(getMD5(seckillId))) {
                throw new SeckillException("seckill data rewrite");
            }
            Date nowDate = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, nowDate);
            if (updateCount <= 0) {
                // 没有更新记录，秒杀结束
                throw new SeckillCloseException("seckill has closed ");
            } else {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(
                        seckillId, userPhone);
                if (insertCount <= 0) {
                    // 秒杀记录插入失败，重复插入
                    throw new RepeatKillException("repeat second kill");
                } else {
                    // 获取秒杀成功明细
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new ExecutionSeckill(seckillId, SeckillStatEnum.SUCCESS,
                            successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化缓存redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }
        Date now = new Date();
        Date start = seckill.getStartTime();
        Date end = seckill.getEndTime();
        // 判断秒杀时间
        if (now.getTime() < start.getTime() || now.getTime() > end.getTime()) {
            return new Exposer(false, now, start, end);
        }
        // 获取加密MD5
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    public ExecutionSeckill executeSeckillProdcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            new ExecutionSeckill(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date nowDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", nowDate);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new ExecutionSeckill(seckillId, SeckillStatEnum.SUCCESS, sk);
            } else {
                return new ExecutionSeckill(seckillId, SeckillStatEnum.stateof(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ExecutionSeckill(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public Seckill getSeckill(long seckillId) {
        // TODO Auto-generated method stub
        return seckillDao.queryById(seckillId);
    }

    public List<Seckill> getSeckillList() {
        // TODO Auto-generated method stub
        return seckillDao.queryAll(0, 4);
    }

}
