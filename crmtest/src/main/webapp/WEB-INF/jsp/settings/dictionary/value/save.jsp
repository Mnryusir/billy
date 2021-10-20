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
			findTypeList();
		})
		function findTypeList(){
			$.ajax({
				url:"settings/dictionary/type/findTypeList.do",
				data:{

				},
				type:"post",
				dataType:"json",
				success:function (data) {
					var html="";
					if (data.code==10000){
						alert("查询成功");
						$.each(data.data,function (i,n){
							if (i==0){
								html+="<option></option>";
							}
							html+='<option value="'+n.code+'">'+n.name+'</option>'
						})
						$("#create-typeCode").html(html);
						/*console.log(html);
						console.log("======");*/
					}
				}
			})
				$("#saveBtn").click(function (){
					var typeCode=$("#create-typeCode").val();
					var value=$("#create-value").val();
					var text=$("#create-text").val();
					var orderNo=$("#create-orderNo").val();
					if (typeCode==""){
						$("#codemsg").html("请选择关联的字典类型编码");
						return ;
					}
					$("#codemsg").html("");
					if (value==""){
						$("#valuemsg").html("请输入字典值名称")
						return ;
					}
					$("#valuemsg").html("");
					$.ajax({
						url:"settings/dictionary/value/saveDictionaryValue.do",
						data:{
							"text":text,
							"value":value,
							"orderNo":orderNo,
							"typeCode":typeCode
						},
						type:"post",
						dataType:"json",
						success:function (data) {
							if(data.code==10000){
								window.location.href = "settings/dictionary/value/toIndex.do";
							}
							else{
								$("msg").html(data.msg);
							}
						}
					})


				})

		}
	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典值</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-dicTypeCode" class="col-sm-2 control-label">字典类型编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-typeCode" style="width: 200%;">
				  <option></option>
				  <%--<option>性别</option>
				  <option>机构类型</option>--%>
				</select>
				<span id="codemsg" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-dicValue" class="col-sm-2 control-label">字典值<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-value" style="width: 200%;">
				<span id="valuemsg" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-text" class="col-sm-2 control-label">文本</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-text" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-orderNo" class="col-sm-2 control-label">排序号</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-orderNo" style="width: 200%;">
				<span id="msg"></span>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>