<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href= "<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function (){
			$.ajax({
				url:"settings/dictionary/type/findTypeList.do",
				data:{
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.code==10000){
						var html="";
						$.each(data.data,function (i,n) {
							html+="<option value='"+n.code+"'>"+n.name+"</option>";
						})
						$("#edit-typeCode").html(html);
						var typeCode="${dictionaryValue.typeCode}";
						$("#edit-typeCode").val(typeCode)
					}
				}

			})
				$("#updateBtn").click(function (){
					var id="${dictionaryValue.id}";
					var typeCode=$("#edit-typeCode").val();
					var value=$("#edit-value").val();
					var  text=$("#edit-text").val();
					var orderNo=$("#edit-orderNo").val();
					if (value==""){
						$("#msg").html("字典值不能为空");
						return ;
					}
					$.ajax({
						url:"settings/dictionary/value/updateDictionaryValue.do",
						data:{
							"id":id,
							"value":value,
							"text":text,
							"orderNo":orderNo,
							"typeCode":typeCode
						},
						type:"post",
						dataType:"json",
						success:function (data){
							if (data.code==10000){
								window.location.href="settings/dictionary/value/toIndex.do"
							}
							else {
								$("#msg").html(data.msg);
							}
						}

					})
				})
		}
		)

	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>修改字典值</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="updateBtn" type="button" class="btn btn-primary">更新</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="edit-dicTypeCode" class="col-sm-2 control-label">字典类型编码</label>
			<%--<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-typeCode" style="width: 200%;" value="性别" readonly>
			</div>--%>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-typeCode" style="width: 200%;">
					<option></option>
					<%--<option>性别</option>
                    <option>机构类型</option>--%>
				</select>
				<span id="msg" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-dicValue" class="col-sm-2 control-label">字典值<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-value" style="width: 200%;" value="${dictionaryValue.value}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-text" class="col-sm-2 control-label">文本</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-text" style="width: 200%;" value="${dictionaryValue.text}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-orderNo" class="col-sm-2 control-label">排序号</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-orderNo" style="width: 200%;" value="${dictionaryValue.orderNo}">
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>