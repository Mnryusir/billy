<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script>
	$(function (){
		$("#updateBtn").click(function (){
			var code=$("#code").val();
			var name=$("#name").val();
			var description=$("#description").val();
			if (code==""){
				alert("数据加载异常，请重新进入该页面");
				return ;
			}
			$.ajax({
				url:"settings/dictionary/type/edit.do",
				data:{
					"code":code,
					"name":name,
					"description":description
				},
				type:"post",
				dataType:"json",
				success:function (data) {
						if (data.code==10000){
							window.location.href= "settings/dictionary/type/toIndex.do"
						}
						else{
							$("msg").html(data.msg);
						}
									}
			})

		})
	})
</script>

</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>修改字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="updateBtn" type="button" class="btn btn-primary">更新</button>
			<button id="button" type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" readonly class="form-control" id="code" style="width: 200%;" value="${dictionaryType.code}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="name" style="width: 200%;" value="${dictionaryType.name}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="description" style="width: 200%;">${dictionaryType.description}</textarea>
				<span id="msg"></span>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>