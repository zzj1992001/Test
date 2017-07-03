<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>
<%@ include file="/WEB-INF/web/common/base_path.jsp"%>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<base href="<%=basePath%>">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="assets/wecome.css">
<link rel="stylesheet" href="assets/ladda/ladda-themeless.min.css">
</head>

<body>
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
		<div class="cover-container">
			<div class="masthead clearfix">
				<div class="inner">
					<h3 class="masthead-brand">个人网站</h3>
					<nav>
						<ul class="nav masthead-nav">
							<li class="active"><a href="wow">魔兽世界无广告直播</a></li>
							<li><a href="welcome" >不会有</a></li>
							<li><a href="welcome">反应的</a></li>
						</ul>
					</nav>
				</div>
			</div>
			<div class="inner cover">
				<h1 class="cover-heading">Welcome My World</h1>
				<p class="lead"></p>
				<p class="lead">
					<a href="main" class="btn btn-lg btn-default">login</a>
				</p>
			</div>
			<div class="mastfoot">
				<div class="inner">
				<p>Copyright 2017-2017, All Right Reserved.</p></div>
			</div>
		</div>
		</div>
	</div>
	<!-- REQUIRED JS SCRIPTS -->
	<script src="assets/adminlte/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<script src="assets/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	(function () {
		  'use strict';

		  if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
		    var msViewportStyle = document.createElement('style')
		    msViewportStyle.appendChild(
		      document.createTextNode(
		        '@-ms-viewport{width:auto!important}'
		      )
		    )
		    document.querySelector('head').appendChild(msViewportStyle)
		  }

		})();
	</script>
		<script type="text/javascript" src="assets/ladda/spin.min.js"></script>
	<script type="text/javascript" src="assets/ladda/ladda.min.js"></script>
	<script src="assets/AdminJS/AdminJS.js"></script>
</body>
</html>