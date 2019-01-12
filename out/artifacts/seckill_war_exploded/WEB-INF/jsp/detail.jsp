<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<title>秒杀详情页</title>
		<%@include file="common/head.jsp"%>
	</head>
	<body>
			<div class="panel panel-default text-center">
				<div class="panel-heading">
					<h1>${seckill.name }</h1></div>
				<div class="panel-body">
					<h2 class="text-danger">
						<!-- 展示time图标 -->
						<span class="glyphicon glyphicon-time" ></span>
						<!-- 显示倒计时 -->
	  					<span class="glyphicon" id="seckill-box"></span>
					</h2>
				</div>
			</div>
			<div class="modal fade" id="killPhoneModal">
                <div class="modal-dialog">
                    <div class="modal-content" >
                        <div class="modal-header" >
                            <h3 class="modal-title text-center">
                               <span class="glyphicon glyphicon-phone"></span>秒杀电话：
                            </h3>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-xs-8 col-xs-offset-2">
                                    <input type="text"  name="killPhone" id="killPhoneKey" placeholder="填手机号" class="form-control" />
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <span id="killPhoneMessage" class="glyphicon"></span>
                            <button type="button"  class="btn btn-success" id="killPhoneBtn">
                                <span class="glyphicon glyphicon-phone" ></span>
                                Submit
                            </button>
                        </div>
                    </div>
                </div>

            </div>
	</body>
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script
		src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- 使用cdn获取jquery插件 cookie插件-->
    <script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
    <!-- 获取jquery倒计时插件-->
    <script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>

    <script type="text/javascript" src="/resources/js/seckill.js"></script>

    <script type="text/javascript">
        $(function () {
                seckill.detail.init({
                    seckillId:${seckill.seckillId},
                    startTime:${seckill.startTime.getTime()},
                    endTime:${seckill.endTime.getTime()}
                })
        })
    </script>
</html>