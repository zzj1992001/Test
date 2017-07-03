<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>

<form id="pager" action="main/wow/list" data-cb-show="main_1">
	<input type="hidden" name="pageNum" value="${pageNum }" /> <input
		type="hidden" name="pageSize" value="${pageSize }" />
</form>
<table class="table table-hover table-striped table-bordered">
	<tbody>
		<tr>
			<th style="width: 5%;">序号</th>
			<th style="width: 20%;">名字</th>
			<th style="width: 20%;">图片</th>
			<th style="width: 15%;">操作</th>
		</tr>
		<c:forEach items="${list}" var="data" varStatus="s">
			<tr>
				<td>${s.index + 1 +(pageNum -1)*pageSize }</td>
				<td>${data.title }</td>
				<td><c:if test="${not empty data.img }">
					<img src="${data.img }" style="width: 200x; max-height: 100px;">
				</c:if></td>
				<td>
				<a href="main/wow/look?id=${data.id }" target="_blank">观看</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>					

<div class="c-diliver"></div>

<%@include file="/WEB-INF/web/common/pagination_bar_main_1.jsp"%>