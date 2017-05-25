<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>

<form id="pager" action="HL/list" data-cb-show="main_1">
	<input type="hidden" name="pageNum" value="${pageNum }" /> <input
		type="hidden" name="pageSize" value="${pageSize }" />
</form>
<table class="table table-hover table-striped table-bordered">
	<tbody>
		<tr>
			<th style="width: 5%;">序号</th>
			<th style="width: 20%;">名字</th>
			<th style="width: 20%;">内容</th>
			<th style="width: 30%;">备注</th>
			<th style="width: 15%;">操作</th>
		</tr>
		<c:forEach items="${list}" var="data" varStatus="s">
			<tr>
				<td>${s.index + 1 +(pageNum -1)*pageSize }</td>
				<td>${data.name }</td>
				<td>${data.content }</td>
				<td>${data.mark }</td>
				<td><a href="HL/modifyEdit/${data.id }"
					target="div" data-cb-show="main_1" class="btn btn-xs btn-flat btn-success ladda-button" data-style="slide-right">编辑</a> 
					<a
					href="HL/delete?id=${data.id }" target="json"
					data-cb-refresh="main_1" data-cb-alert="yes"
					data-confirm-msg="确定删除该项目吗？"
					class="btn btn-xs btn-flat btn-danger ladda-button"
					data-style="slide-right">删除</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="c-diliver"></div>

<%@include file="/WEB-INF/web/common/pagination_bar_main_1.jsp"%>