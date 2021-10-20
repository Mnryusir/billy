<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function (){
			$("#selectAll").click(function (){

				var ck=this.checked;
				var $cks=$("input[name=ck]");

				for (var i=0;i<$cks.length;i++){
					if (ck){
						$cks[i].checked=true;
					}else {
						$cks[i].checked=false;
					}
				}
			})
			//反选

			$("input[name=ck]").click(function (){
				//获取所有复选框的数量
				var ckLength=$("input[name=ck]").length;
				//获取所有选中复选框的数量
				var ckdLength=$("input[name=ck]:checked").length;
				//console.log(ckdLength);
				//console.log(ckLength);
				if (ckLength==ckdLength){
					$("#selectAll").prop("checked",true);
				}
				else {
					$("#selectAll").prop("checked",false);
				}
			})
				$("#toEditBtn").click(function (){
					var len=$("input[name=ck]:checked")
					if (len.length==0){
						alert("至少勾选一个");
						return ;
					}
					else if (len.length!=1){
						alert("最多勾选一个");
						return ;
					}
					var ckValue=len[0].value;
					//console.log(ckValue);
					/*$.ajax({
						url:"settings/dictionary/type/toEdit.do",
						data:{
							"code":ckValue,


						},
						type:"post",
						dataType:"json",
						success:function (data){

						}
					})*/
					//仅仅跳转页面的话可以不用ajax来完成，用window.location完成
					window.location.href="settings/dictionary/type/toEdit.do?code="+ckValue;
				})
			$("#batchDeleteBtn").click(function (){
				var $cks=$("input[name=ck]:checked");
				if ($cks.length==0){
					alert("请至少勾选一个！");
					return ;
				}

				var param="";
				var names="";

				for (var i = 0; i <$cks.length; i++) {
					param+="codes="+$cks[i].value;
					names+=$("#name_"+$cks[i].value).html();
					if (i<$cks.length-1){
						param+="&";
						names+="、";
					}
					//console.log(param);

				}
				if (confirm("是否执行删除"+names+"？")){
					$.ajax({
						url:"settings/dictionary/type/deleteDicTypesByCodes.do?"+param,
						data:{
						},
						type:"post",
						dataType:"json",
						errsuccess:function (data) {

							//data:{code:10000/10001,msg:xxx}
							//删除成功后,跳转到列表页面
							if(data.code == 10000){
								window.location.href = "settings/dictionary/type/toIndex.do?"+param;
							}else{
								alert("aaa");
								alert(msg.responseText);
							}

						}
					})
				}
				/*console.log(param);
				console.log(names);*/
			})

		})

	</script>
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典类型列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/type/toSave.do'"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button id="toEditBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button id="batchDeleteBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input id="selectAll" type="checkbox" /></td>
					<td>序号</td>
					<td>编码</td>
					<td>名称</td>
					<td>描述</td>
				</tr>
			</thead>
			<tbody>
			<!--
                varStatus参数
                index:从零开始计数，
                count:从一开始计数
                current:当前遍历的对象
                first:是否是第一个 true/false
                last:是否是最后一个 true/false

                -->
			<c:forEach items="${dictionaryTypeList}" var="dt" varStatus="ds">
				<tr class="${ds.index%2==0?'active':''}">
					<td><input name="ck" value="${dt.code}" type="checkbox" /></td>
					<td>${ds.count}</td>
					<td id="codes">${dt.code}</td>
					<td id="name_${dt.code}">${dt.name}</td>
					<td>${dt.description}</td>
				</tr>
			</c:forEach>
<%--			<c:forEach items="${dictionaryTypeList}" var="dt" varStatus="ds">
				<tr class="active">
					<td><input  type="checkbox" /></td>
					<td>${ds.count}</td>
					<td>${dt.code}</td>
					<td>${dt.name}</td>
					<td>${dt.description}</td>
				</tr>
			</c:forEach>--%>
				<%--<tr class="active">
					<td><input type="checkbox" /></td>
					<td>1</td>
					<td>sex</td>
					<td>性别</td>
					<td>性别包括男和女</td>
				</tr>--%>
			</tbody>
		</table>
	</div>
	
</body>
</html>