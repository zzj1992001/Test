<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp" %> 
<%@ include file="/WEB-INF/web/common/base_path.jsp" %> 

<section class="content-header">
	<h1>
       魔兽世界
        <small></small>
	</h1>
</section>


<section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box c-box-no-border">
            <div class="box-header">
              
              <a href="main/wow/index" target="div" data-cb-show="main_0" class="btn btn-sm btn-flat btn-primary ladda-button" data-style="slide-right"><i class="fa fa-fw fa-reorder"></i>列表</a>
              <a href="main/wow/getWowlist" target="_blank" data-cb-refresh="main_1" class="btn btn-sm btn-flat btn-success ladda-button" data-style="slide-right">同步</a>

              <div class="box-tools">
              </div>
              
            </div>
            <!-- /.box-header -->
            <div class="box-body" id="main_1">
              
              <%@include file="/WEB-INF/web/main/wow/list.jsp" %>
              
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
      </div>
</section>
