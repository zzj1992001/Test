<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp" %> 
<%@ include file="/WEB-INF/web/common/base_path.jsp" %> 

<section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box c-box-no-border">
            <div class="box-header">
              
              <div class="box-tools">
              </div>
              
            </div>
            <!-- /.box-header -->
            <div class="box-body" id="main_1">
					<div class="c-diliver"></div>
			<input type="hidden" name="id" value="${roomid }"/>

				<object type="application/x-shockwave-flash" data="http://staticlive.douyutv.com/common/share/play.swf?room_id=${roomid }" width="900" height="552" allowscriptaccess="always" allowfullscreen="true" allowfullscreeninteractive="true"><param name="quality" value="high"><param name="bgcolor" value="#ffffff"><param name="allowscriptaccess" value="always"><param name="allowfullscreen" value="true"><param name="wmode" value="transparent"><param name="allowFullScreenInteractive" value="true"></object>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
      </div>
</section>