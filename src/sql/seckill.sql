DELIMITER $$ --转换console
--定义存储过程
--row_count()返回上一条sql语句的影响行数(delete,insert,update)
--row_count()返回0：未修改，<0：sql错误/未执行sql,>0:修改的行数
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp ,out r_result int)
  begin
    DECLARE  insert_count int default 0;
    start transaction ;
    insert ignore into success_killed(seckill_id,user_phone,create_time)
    values(v_seckill_id,v_phone,v_kill_time);
    select row_count() into insert_count;
    if(insert_count = 0)then
      rollback ;
      set r_result=-1;
    elseif(insert_count < 0)then
      rollback ;
      set r_result=-2;
    else
      update seckill
      set number=number-1
      where seckill_id=v_seckill_id
      and end_time>v_kill_time
      and start_time<kill_time
      and number >0;
      select row_count() into insert_count;
      if(insert_count = 0)then
        rollback ;
        set r_result=0;
      elseif(insert_count < 0)then
        rollback ;
        set r_result=-2;
      else
        commit ;
        set r_result=1;
      end if;
    end if;
  end;
$$
--存储过程定义结束

