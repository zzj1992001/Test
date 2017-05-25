<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp" %>  
<%@ include file="/WEB-INF/web/common/base_path.jsp" %>
<!DOCTYPE html>
<html>
<head>
   	<base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>登陆台</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  	<link rel="stylesheet" href="assets/adminlte/font-awesome/4.5.0/css/font-awesome.min.css">
 	<link rel="stylesheet" href="assets/adminlte/ionicons/2.0.1/css/ionicons.min.css">
  	<link rel="stylesheet" href="assets/adminlte/css/AdminLTE.min.css">
  	<link rel="stylesheet" href="assets/ladda/ladda-themeless.min.css">
</head>
<body class="hold-transition login-page" style="background:url('assets/background/1436495787140.jpg') no-repeat;"> 
<nav class="navbar " style="background-color: rgba(50,50,50,0.6);">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="http://www.baidu.com">
      <strong><font color="#F8F8FF">12345</font></strong>
      </a>
    </div>
  </div>
</nav>
	<div class="login-box">
	  <!-- /.login-logo -->
	  <div class="login-box-body " style="background-color: rgba(50,50,50,0.6);">
	  	<div class="login-logo">
	    <a><strong><font color="#F8F8FF">管理台</font></strong></a>
	  	</div>	
	    <form action="ajaxLogin" method="post" onsubmit="return ajaxLogin(this);">
	      <div class="form-group has-feedback">
	        <input type="text" id="account" name="account" class="form-control" placeholder="用户名">
	        <span class="glyphicon glyphicon-user form-control-feedback"></span>
	      </div>
	      <div class="form-group has-feedback">
	        <input type="password" id="password" name="password" class="form-control" placeholder="密码">
	        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
	      </div>
	      <div class="row">
	        <div class="col-xs-7">
	          <label id="error_label" class="text-danger"></label>
	        </div>
	        <!-- /.col -->
	        <div class="col-xs-5">
	          <button type="submit" id="submit_btn" class="btn btn-primary btn-block btn-flat ladda-button" data-style="expand-right">登&nbsp;&nbsp;&nbsp;&nbsp;录</button>
	        </div>
	        <!-- /.col -->
	      </div>
	    </form>
	
	  </div>
	  <!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->

    <script src="assets/adminlte/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<script src="assets/bootstrap/js/bootstrap.min.js"></script>   
    <script src="assets/js/md5.js" type="text/javascript"></script>
    <script type="text/javascript">
    function ajaxLogin(form){
   		var account = $.trim($("#account").val());
   		var password = $.trim($("#password").val());
   		if(account == ''){
   			$("#error_label").html("用户名不能为空!");
   			$("#account").focus();
   			return false;
   		}
   		if(password == ''){
   			$("#error_label").html("密码不能为空！").show();
   			$("#password").focus();
   			return false;
   		}
   		var $form = $(form);
   		var pwd = MD5(password);
   		$("#error_label").html("");
   		
   		var $loadingBtn = new LoadingBtn($('#submit_btn'));
		$loadingBtn.start();
   		
   		$.ajax({
   			type: 'post',
   			url: $form.attr("action"),
   			data: {account: account, password: pwd},
   			dataType: 'json',
   			success: function(json){
   				$("#error_label").html(json.status_msg);
   				if (json.status_code == 200) {
   					$("#error_label").removeClass("text-danger");
   					$("#error_label").addClass("text-success");
   					setTimeout(function(){
   						window.location.href = "index";
   					}, 300);
   				}else {
   					setTimeout(function(){
   						$loadingBtn.stop();
   					}, 300);
   				}
   			}
   		});
   		return false;
   	}
       
    $("input.form-control").change(function(){
    	$("#error_label").html("");
    });
	</script>   
	
	<script type="text/javascript" src="assets/ladda/spin.min.js"></script>
	<script type="text/javascript" src="assets/ladda/ladda.min.js"></script>
	<script src="assets/AdminJS/AdminJS.js"></script>
</body>
</html>