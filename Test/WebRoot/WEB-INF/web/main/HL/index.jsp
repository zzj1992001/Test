<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp" %> 
<%@ include file="/WEB-INF/web/common/base_path.jsp" %> 

<section class="content-header">
	<h1>
        英雄联盟
        <small></small>
	</h1>
</section>


<section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box c-box-no-border">
            <div class="box-header">
              
              <a href="HL/index" target="div" data-cb-show="main_0" class="btn btn-sm btn-flat btn-primary ladda-button" data-style="slide-right"><i class="fa fa-fw fa-reorder"></i>列表</a>
              <a href="HL/Add" target="div" data-cb-show="main_1" class="btn btn-sm btn-flat btn-success ladda-button" data-style="slide-right"><i class="fa fa-fw fa-plus"></i>添加</a>

              <div class="box-tools">
              </div>
              
            </div>
            <!-- /.box-header -->
            <div class="box-body" id="main_1">
              
              <%@include file="/WEB-INF/web/main/HL/list.jsp" %>
              
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
      </div>
</section>
