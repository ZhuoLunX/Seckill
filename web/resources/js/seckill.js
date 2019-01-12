var seckill= {

    URl: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        executionSeckill:function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/executionSeckill';
        }
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //判断时间
        if (nowTime > endTime) {
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //秒杀未开始
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                seckill.handleSeckillKill(seckillId,seckillBox);
            });
        } else {
            seckill.handleSeckillKill(seckillId,seckillBox);
        }
    },
    handleSeckillKill:function(seckillId,node){
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URl.exposer(seckillId),{},function (result) {
            if(result && result['success']){
                var exposer=result['data'];
                if (exposer['exposed']) {
                    var md5=exposer['md5'];
                    var killUrl=seckill.URl.executionSeckill(seckillId,md5);
                    $("#killBtn").one('click',function () {
                        //禁用按钮
                        $(this).addClass('disabled');
                        //执行秒杀
                        $.post(killUrl,{},function(result) {
                            if(result && result['success']){
                                var killResult=result['data'];
                                var state=killResult['state'];
                                var stateInfo=killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }else {
                                console.log('result:'+result);
                            }
                        });
                    });
                    node.show();
                }else {
                    var now=exposer['now'];
                    var start=exposer['start'];
                    var end=exposer['end'];
                    seckill.countDown(seckillId,now,start,end);
                }
            }else {
                console.log("result:"+result);
            }
        })
    },
    validataPhone: function (phone) {
        if (phone && phone.length== 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    detail: {
        init: function (params) {
            //手机验证和登陆，计时交互
            //规划我们的交互体验
            var userPhone = $.cottc0okie('userPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            if (!seckill.validataPhone(userPhone)) {
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, //显示
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false //关闭键盘事件
                });
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validataPhone(inputPhone)) {
                        $.cookie('userPhone', inputPhone, {expires: 7, path: '/seckill'})
                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号输入错误!</label>').show(300);
                    }
                });
            }
            $.get(seckill.URl.now(), {}, function (result) {
                if (result && result["success"]) {
                    var nowTime = result['data'];
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            });

        }

    }
}