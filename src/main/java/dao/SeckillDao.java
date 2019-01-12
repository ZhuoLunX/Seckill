package dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import entity.Seckill;




/**
 * @author xiongzhuolun
 *
 */
public interface SeckillDao {
	

	
	/**
	 * 减少库存，返回值为更新结果集的条数
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
	
	
	/**
	 * 根据seckillId查询秒杀单明细
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	
	/**
	 * 根据偏移量和条数查询秒杀单结果集
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);


	/**
	 * 执行存储过程
	 * @param paramMap
	 */
	void killByProcedure(Map<String,Object> paramMap);
}
