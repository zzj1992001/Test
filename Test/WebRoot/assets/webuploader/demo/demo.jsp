<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp" %>  
<%@ include file="/WEB-INF/web/common/base_path.jsp" %>
<!DOCTYPE html>
<html>
<head>
   	<base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>百度WebUploader插件Demo</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  	<link rel="stylesheet" href="assets/adminlte/font-awesome/4.5.0/css/font-awesome.min.css">
  	<link rel="stylesheet" href="assets/adminlte/ionicons/2.0.1/css/ionicons.min.css">
  
  	<link rel="stylesheet" type="text/css" href="assets/webuploader/css/webuploader.css">
  	<link rel="stylesheet" type="text/css" href="assets/webuploader/css/demo.css">
</head>
<body>
	
	<div class="web-uploader" id="uploader" data-serverUrl="common/uploadImage" data-inputForResult="input_link">
	    <div class="queueList">
	        <div class="placeholder">
				<div class="filePicker"></div>	            
	            <p>或将图片拖到这里，建议单次上传不超过50张</p>
	        </div>
	    </div>
	    <div class="statusBar" style="display:none;">
	        <div class="progress">
	            <span class="text">0%</span>
	            <span class="percentage"></span>
	        </div>
	        <div class="info"></div>
	        <div class="btns">
	            <div class=continueFilePicker></div>
	            <div class="uploadBtn">开始上传</div>
	        </div>
	    </div>
	</div>
	
	<div>
		<input type="hidden" id="input_link">
	</div>
	
	<script src="assets/adminlte/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<script src="assets/bootstrap/js/bootstrap.min.js"></script>
	
	<script src="assets/webuploader/js/webuploader.js"></script>
	<script src="assets/webuploader/js/demo.js"></script>

	<script type="text/javascript">
	var webUI = WebUploaderUI('uploader');
	</script>
</body>
</html>