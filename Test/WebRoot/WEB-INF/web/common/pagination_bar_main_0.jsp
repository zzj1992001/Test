<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${totalRow gt 0 }">
	<ul class="pagination c_pagination" data-totalRow="${totalRow }" data-pageSize="${pageSize }" data-pageNum="${pageNum }" data-showNum="5" data-pageFormContainer="main_0">
	</ul>
</c:if>
