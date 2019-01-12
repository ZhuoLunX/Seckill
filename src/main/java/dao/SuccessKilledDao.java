package dao;

import entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;


/**
 * @author xiongzhuolun
 *
 */
public interface SuccessKilledDao {

	
	
	/**
	 * 执行秒杀，插入秒杀信息
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
	
	
	
	/**
	 * 查询秒杀成功产品明细
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
