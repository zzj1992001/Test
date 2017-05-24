<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>
<%@ include file="/WEB-INF/web/common/base_path.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<title>百度WebUploader插件Demo</title>
<meta
	content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'
	name='viewport'>
<link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="assets/adminlte/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet"
	href="assets/adminlte/ionicons/2.0.1/css/ionicons.min.css">

<link rel="stylesheet" type="text/css"
	href="assets/webuploader/css/webuploader.css">
<link rel="stylesheet" type="text/css"
	href="assets/webuploader/css/demo.css">
</head>
<body>

	<c:if test="${not empty images }">
		<div class="web-uploader">
			<div class="queueList filled">
				<ul class="filelist">
					<c:forEach items="${images }" var="img">
						<li class="js-img-item" id="img_${img.id }">
							<p class="imgWrap">
								<img src="${img.url }" class="js-beautify-show">
							</p>
							<div class="file-panel js-opt-bar"
								style="height: auto; display: none;">
								<a
									href="common/appImageDelete?id=${img.id }&ownerId=${img.ownerId}&type=${img.type}"
									target="json" data-cb-remove="#img_${img.id }"><span
									class="cancel">删除</span></a>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</c:if>

	<script src="assets/adminlte/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<script src="assets/bootstrap/js/bootstrap.min.js"></script>

	<script src="assets/AdminJS/AdminJS.js"></script>

	<script type="text/javascript">
	BeautifyImg.init('img.js-beautify-show');
	BeautifyImg.initOperate('li.js-img-item', '.js-opt-bar');
	</script>
</body>
</html>