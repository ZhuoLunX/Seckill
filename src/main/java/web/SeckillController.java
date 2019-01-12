package web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.SeckillService;
import dto.ExecutionSeckill;
import dto.Exposer;
import dto.SeckillResult;
import entity.Seckill;
import enums.SeckillStatEnum;
import exception.RepeatKillException;
import exception.SeckillCloseException;

/**
 * @author xiongzhuolun
 * 
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	/**
	 * 获取列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}

	/**
	 * 获取详情页
	 * 
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getSeckill(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	/**
	 * 暴露接口
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result=new SeckillResult<Exposer>(true,exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 执行秒杀接口
	 * @param seckillId
	 * @param md5
	 * @param userPhone
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/{md5}/executionSeckill",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<ExecutionSeckill> executionSeckill(@PathVariable("seckillId") long seckillId,@PathVariable("md5") String md5,@CookieValue(value="userPhone",required=false) Long userPhone){
		//springmvc valid
		if(userPhone==null){
			return new SeckillResult<ExecutionSeckill>(false,"请先注册");
		}
		SeckillResult<ExecutionSeckill> result;
		try{
			//ExecutionSeckill executionSeckill=seckillService.executeSeckill(seckillId, userPhone, md5);
			ExecutionSeckill executionSeckill=seckillService.executeSeckillProdcedure(seckillId, userPhone, md5);
			result=new SeckillResult<ExecutionSeckill>(true,executionSeckill);
		}catch(RepeatKillException e){
			ExecutionSeckill es=new ExecutionSeckill(seckillId,SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<ExecutionSeckill>(true,es);
		}catch(SeckillCloseException e){
			ExecutionSeckill es=new ExecutionSeckill(seckillId,SeckillStatEnum.END);
			return new SeckillResult<ExecutionSeckill>(true,es);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			ExecutionSeckill es=new ExecutionSeckill(seckillId,SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<ExecutionSeckill>(true,es);
		}
		return result;
	}

	/**
	 * 获取系统时间接口
	 * @return
	 */
	@RequestMapping(value="/time/now",method=RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Date> getSysterTime(){
		Date now=new Date();
	    return new SeckillResult<Date>(true,now);
	}
}
