<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<section class="content-header">
	<h1>
        修改密码
        <small></small>
	</h1>
</section>


<section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box c-box-no-border">
            <div class="box-header">
            </div>
            <!-- /.box-header -->
            <div class="box-body" id="main_1">
              
              <form action="passModify" class="form-horizontal" style="width: 400px;" data-cb-alert="yes" data-cb-clearInput="yes" data-valid>
				<div class="box-body">
					<div class="form-group">
						<label class="col-sm-4 control-label">旧密码</label>

						<div class="col-sm-8">
							<input type="password" class="form-control" name="oldPass" required>
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-4 control-label">新密码</label>
						<div class="col-sm-8">
							<input type="password" class="form-control" name="newPass" required>
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-4 control-label">确认密码</label>
						<div class="col-sm-8">
							<input type="password" class="form-control" name="confirmPass" required>
						</div>
					</div>
					<button type="submit" class="btn btn-primary btn-flat pull-right ladda-button" data-style="expand-right"><span class="ladda-label">确认修改</span></button>
				</div>
			</form>
              
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
      </div>
</section>
