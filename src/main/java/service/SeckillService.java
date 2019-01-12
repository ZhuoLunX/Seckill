package service;

import java.util.Date;
import java.util.List;

import dto.ExecutionSeckill;
import dto.Exposer;
import entity.Seckill;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;

/**
 * @author xiongzhuolun
 * 业务接口：站在使用者的角度设计
 * 三个方面，粒度，参数，返回类型（return，异常）
 */
public interface SeckillService {


    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();


    /**
     * 查询一条秒杀记录
     *
     * @return
     */
    Seckill getSeckill(long seckillId);


    /**
     * 输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    ExecutionSeckill executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀的存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    ExecutionSeckill executeSeckillProdcedure(long seckillId, long userPhone, String md5);
}
