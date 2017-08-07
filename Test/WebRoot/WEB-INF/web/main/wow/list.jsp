<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>

<form id="pager" action="main/wow/list" data-cb-show="main_1">
	<input type="hidden" name="pageNum" value="${pageNum }" /> <input
		type="hidden" name="pageSize" value="${pageSize }" />
</form>
<div class="content">
  	<div class="row">
  	<c:forEach items="${list }" var="data">
  		<div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box" style="background-color: #56cccc;">
            <div class="inner" >
              <p style="width: 100%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; ">${data.title }</p>

              <img alt="" src="${data.img }" style=" width: 100%;height: 100%">
            </div>
            <div class="icon">
            </div>
            <a href="main/wow/look?id=${data.id }"  target="div" data-cb-show="main_1" class="small-box-footer">
              查看更多 <i class="fa fa-arrow-circle-right"></i>
            </a>
          </div>
        </div>
  	</c:forEach>
  	</div>					
</div>
<div class="c-diliver"></div>

<%@include file="/WEB-INF/web/common/pagination_bar_main_1.jsp"%>