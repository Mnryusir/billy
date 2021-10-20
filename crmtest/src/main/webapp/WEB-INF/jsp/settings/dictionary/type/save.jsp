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
			$("#code").blur(function (){
				var code=$("#code").val();
				if (code==""){
					$("#msg").html("code不允许为空！")
					return;
				}
				$.ajax({
					url:"settings/dictionary/type/findDictionaryTypeByCode.do",
					data:{
						"code":code
					},
					type:"post",
					dataType:"json",
					success:function (data) {

						//data:{code:10000/10001,msg:xxx}
						if(data.code == 10000){
							//数据库中无该编码,可以插入
							//清除提示信息
							$("#msg").html("");
						}else{
							$("#msg").html(data.msg);
						}

					}

						})
			}

			)

			$("#saveBtn").click(function (){
				var code=$("#code").val();
				var name=$("#name").val();
				var description=$("#description").val();
				var text=$("#msg").html();
				if (code==""){
					$("#msg").html("字符集编码不能为空！");
					return ;
				}
				if (text!=""){
					$("#msg").html("字符集编码不能重复！");
					return ;
				}
				$.ajax({
					url:"settings/dictionary/type/save.do",
					data:{
						"code":code,
						"name":name,
						"description":description
					},
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.code==10000){

							window.location="settings/dictionary/type/toIndex.do"}
						else{
							$("#msg").html(data.msg);
						}
					}

				})

			})
		})


	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="code" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="name" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="description" style="width: 200%;"></textarea>
				<span id="msg" style="color: red"></span>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>