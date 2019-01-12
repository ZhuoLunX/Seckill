package dto;

import entity.SuccessKilled;
import enums.SeckillStatEnum;


/**
 * 封装秒杀执行后的结果
 * @author xiongzhuolun
 *
 */
public class ExecutionSeckill {

	
	private long seckillId;
	
	//执行秒杀状态
	private int state;
	
	//状态描述
	private String stateInfo;
	
	//执行成功后返回秒杀成功表
	private SuccessKilled successKilled;

	public ExecutionSeckill(long seckillId, SeckillStatEnum stateEnum,
			SuccessKilled successKilled) {
		this.seckillId = seckillId;
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	public ExecutionSeckill(long seckillId, SeckillStatEnum stateEnum) {
		this.seckillId = seckillId;
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	
	
	
}
