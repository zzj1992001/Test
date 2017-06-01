<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/jstl.jsp"%>

<form action="${hl==null?'main/HL/submit':'main/HL/update' }" class="form-horizontal"  style="width: 60%;" data-valid data-cb-alert="yes">
	
	<div class="box-body">
	<div class="form-group">
	    <input type="hidden" class="form-control" name="hl.id" value="${hl.id }">
	</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">name</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" name="hl.name" value="${hl.name }"
					placeholder="name" required>
			</div>
		</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">content</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="hl.content" value="${hl.content }"
						placeholder="content" >
				</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">mark</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="hl.mark" value="${hl.mark }"
							placeholder="mark" >
					</div>
				</div>
				<br/>
				<div class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div class="col-sm-10">
						<button type="submit" class="btn btn-info btn ladda-button" data-style="expand-right">立即保存</button>
					</div>
				</div>
			</div>
		<!-- /.box-body -->
</form>