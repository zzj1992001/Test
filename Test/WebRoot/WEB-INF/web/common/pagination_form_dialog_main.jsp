<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form id="pager" action="${currentUrl }" data-cb-show="dialog_main">
	<input type="hidden" name="pageNum" value="${pageNum }"/>
	<input type="hidden" name="pageSize" value="${pageSize }" />
</form>