<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>
<%@ include file="/WEB-INF/web/common/base_path.jsp"%>


<!DOCTYPE html>

<html>
<head>
<meta charset="utf-8">
<base href="<%=basePath%>">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理台</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="assets/adminlte/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet"
	href="assets/adminlte/ionicons/2.0.1/css/ionicons.min.css">
<link rel="stylesheet" href="assets/adminlte/css/AdminLTE.min.css">
<link rel="stylesheet"
	href="assets/adminlte/css/skins/skin-blue.min.css">
<link rel="stylesheet"
	href="assets/adminlte/plugins/datepicker/datepicker3.css">
<link rel="stylesheet"
	href="assets/adminlte/plugins/timepicker/bootstrap-timepicker.min.css">
<link rel="stylesheet"
	href="assets/adminlte/plugins/dialog/bootstrap-dialog.min.css">
<link rel="stylesheet" href="assets/ladda/ladda-themeless.min.css">
<link rel="stylesheet" href="assets/toastr/toastr.css">
<link rel="stylesheet" href="assets/SweetAlert/sweetalert.css">
<link rel="stylesheet" href="assets/AdminJS/nprogress.css">
<link rel="stylesheet" href="assets/AdminJS/AdminJS.css?v=1.0">
<link rel="stylesheet" href="assets/css/app.css">

<link rel="stylesheet" type="text/css"
	href="assets/webuploader/css/webuploader.css">
<link rel="stylesheet" type="text/css"
	href="assets/webuploader/css/demo.css">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper" style="background-color: #ecf0f5;">

		<header class="main-header">

			<a href="" class="logo"> <span class="logo-mini">1</span>
				<span class="logo-lg">游戏平台</span>
			</a>

			<nav class="navbar navbar-static-top">
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
					role="button"> <span class="sr-only">Toggle navigation</span>
				</a>
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> <img
								src="assets/img/user_0.png" class="user-image" alt="User Image">
								<span class="hidden-xs">管理员</span>
						</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header"><img src="assets/img/user_0.png"
									class="img-circle" alt="User Image"></li>
								<li class="user-footer">
									<div class="pull-left">
										<a href="passModifyEdit" target="div"
											data-cb-show="main_0" class="btn btn-default btn-flat">修改密码</a>
									</div>
									<div class="pull-right">
										<a href="logout" class="btn btn-default btn-flat">退出登录</a>
									</div>
								</li>
							</ul></li>
					</ul>
				</div>

			</nav>
		</header>


		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- sidebar menu: : style can be found in sidebar.less -->
				<ul class="sidebar-menu">
					<li class="header">MAIN NAVIGATION</li>
					<li class="active treeview"><a href="#"> <i
							class="fa fa-dashboard"></i> <span>玩游戏</span> <span
							class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu js-menu-click">
							<li class="active"><a href="HL/index" target="div" data-cb-show="main_0"><i
									class="fa fa-circle-o"></i> 英雄联盟</a></li>
							<li><a href=""><i class="fa fa-circle-o"></i>
									Dota2</a></li>
						</ul></li>
					<li class="treeview"><a href="#"> <i class="fa fa-laptop"></i>
							<span>看电视剧</span> <span class="pull-right-container">
								<i class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a href=""><i
									class="fa fa-circle-o"></i> 冰与火之歌</a></li>
							<li><a href=""><i
									class="fa fa-circle-o"></i> 生活大爆炸</a></li>
							<li><a href=""><i
									class="fa fa-circle-o"></i> 纸牌屋</a></li>
							<li><a href=""><i
									class="fa fa-circle-o"></i> 犯罪心理</a></li>
							<li><a href=""><i
									class="fa fa-circle-o"></i> 绝命毒师</a></li>
							<li><a href=""><i
									class="fa fa-circle-o"></i> 越狱</a></li>
						</ul></li>

				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>


		<!-- 动态内容 -->
		<div class="content-wrapper" id="main_0"></div>

	</div>
	<!-- ./wrapper -->


	<!-- REQUIRED JS SCRIPTS -->
	<script src="assets/adminlte/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<script src="assets/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/adminlte/js/app.min.js"></script>

	<script src="assets/AdminJS/nprogress.js"></script>
	<script src="assets/toastr/toastr.min.js"></script>
	<script src="assets/SweetAlert/sweetalert.min.js"></script>
	<script type="text/javascript" src="assets/ladda/spin.min.js"></script>
	<script type="text/javascript" src="assets/ladda/ladda.min.js"></script>

	<script src="assets/AdminJS/validator.min.js"></script>
	<script src="assets/AdminJS/jquery.ajaxsubmit.js"></script>
	<script src="assets/AdminJS/AdminJS.js?v=1.2"></script>

	<script type="text/javascript">
		AdminJS.init();
		AdminJS.initAjaxProgress();

		$('ul.js-menu-click li a').click(function() {
			$('ul.js-menu-click li').removeClass("active");
			var $me = $(this);
			$me.parent().addClass("active");
		});

		$('ul.js-menu-click li a:first').click();
	</script>

	<script type="text/javascript"
		src="assets/adminlte/plugins/datepicker/bootstrap-datepicker.js"></script>
	<script type="text/javascript"
		src="assets/adminlte/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
	<script type="text/javascript"
		src="assets/adminlte/plugins/dialog/bootstrap-dialog.min.js"></script>
	<script type="text/javascript"
		src="assets/adminlte/plugins/timepicker/bootstrap-timepicker.min.js"></script>
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=lyqEdnGNmjvZkb9tL4x6q5uFqiXOGykB"></script>
	<!-- Optionally, you can add Slimscroll and FastClick plugins.
     Both of these plugins are recommended to enhance the
     user experience. Slimscroll is required when using the
     fixed layout. -->

</body>
</html>
