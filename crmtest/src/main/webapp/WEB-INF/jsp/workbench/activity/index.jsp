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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<%--分页组件--%>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">
	$(function(){
		//查询第一页，每页显示两条数据
		//需要作为参数通过ajax传到后台
	findActivityList(1,2);

		//2.初始化日历控件样式
		$(".time").datetimepicker({

			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		$("#searchBtn").click(function () {
			/*var activityName = $("#search-activityName").val();
			var owner = $("#search-owner").val();
			var startDate = $("#search-startDate").val();
			var endDate = $("#search-endDate").val();*/
			//条件查询的数据存入到隐藏域中
			/*$("#hidden-activityName").val(activityName);
			$("#hidden-owner").val(owner);
			$("#hidden-startDate").val(startDate);
			$("#hidden-endDate").val(endDate);*/
			findActivityList(1,2);
		})
//点击创建按钮查询用户列表回显数据
$("#openCreatActivityModelBtn").click(function () {
	$.ajax({
		url:"settings/user/findAll.do",
		data:{

		},
		type:"post",
		dataType:"json",
		success:function (data) {
				if (data.code==10000){
					var html="";
					$.each(data.data,function (i,n) {
						html+="<option value="+n.id+">"+n.name+"</option>";
					})
					$("#create-owner").html(html);
					var userId="${user.id}";
					$("#create-owner").val(userId);
					//打开模态窗口
					$("#createActivityModal").modal("show");
				}
		}
	})
})
				$("#saveBtn").click(function (){
					var owner = $("#create-owner").val();
					var name = $("#create-name").val();
					var startDate = $("#create-startDate").val();
					var endDate = $("#create-endDate").val();
					var cost = $("#create-cost").val();
					var description = $("#create-description").val();
					if (name==""){
						alert("请输入市场活动名称");
						return ;
					}
					$.ajax({
						url:"workbench/activity/save.do",
						data:{
							"owner":owner ,
							"name":name ,
							"startDate":startDate ,
							"endDate":endDate ,
							"cost":cost ,
							"description":description
						},
						type:"post",
						dataType:"json",
						success:function (data) {
								if (data.data==10000){
									findActivityList(1,2);
									$("#createActivityModal").modal("hide");

								}
								else {
									alert(data.msg);
								}
						}
					})

				})
		$("#selectAll").click(function () {
			$("input[name=ck]").prop("checked",this.checked);
		})
		$("#activityListBody").on("click","input[name=ck]",function () {
		})


});
function findActivityList(pageNo,pageSize){
	//获取条件过滤参数
	/*var activityName = $("#hidden-activityName").val();
	var owner = $("#hidden-owner").val();
	var startDate = $("#hidden-startDate").val();
	var endDate = $("#hidden-endDate").val();*/
	var activityName = $("#search-activityName").val();
	var owner = $("#search-owner").val();
	var startDate = $("#search-startDate").val();
	var endDate = $("#search-endDate").val();
	$.ajax({
		url:"workbench/activity/findActivityList.do",
		data:{
			"pageNo":pageNo,
			"pageSize":pageSize,
			"activityName":activityName ,
			"owner":owner ,
			"startDate":startDate ,
			"endDate":endDate

		},
		type:"post",
		dataType:"json",
		success:function (data) {

			if (data.code==10000){
				console.log(data.data);
				var html="";
				$.each(data.data,function (i,n) {
				    html+='<tr class="'+(i%2==0?'active':'')+'">'
					html+='<td><input type="checkbox" name="ck" value='+n.id+'></td>'
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.html\';">'+n.name+'</a></td>'
					html+='<td>'+n.owner+'</td>'
					html+='<td>'+n.startDate+'</td>'
					html+='<td>'+n.endDate+'</td>'
					html+='</tr>'
				})
				//alert(data.data.length)
				$("#activityListBody").html(html);
				//5.初始化分页组件
				/*
                    注意:
                        分页组件所需的参数必须提供完整,差一个都不会显示该组件
                        当前页,每页条数,总页数,总记录数
                 */

				$("#activityPage").bs_pagination({
					currentPage: data.pageNo, // 页码
					rowsPerPage: data.pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: data.totalPages, // 总页数
					totalRows: data.totalCount, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//当点击了分页组件的页码(第几页,上一页,下一页,首页,尾页,跳转到第几页...)
					onChangePage : function(event, data){
						console.log("onChangePage被调用了,重新发送请求查询列表数据")
						findActivityList(data.currentPage , data.rowsPerPage);
					}
				});

			}
			else {
				alert(data.msg);
			}
		}
	});

	$("#openEditActivityModal").click(function () {
		var $cks = $("input[name=ck]:checked");


		//未选中或选中多个
		if($cks.length != 1){
			alert("修改操作,只能修改一条数据")
			return;
		}
		$.ajax({
			url:"settings/user/findAll.do",
			data:{

			},
			type:"post",
			dataType:"json",
			success:function (data) {
					if (data.code==10000){
						var html = "";

						$.each(data.data,function (i, n) {
							html += "<option value="+n.id+">"+n.name+"</option>";
						})

						$("#edit-owner").html(html);
						//所有者下拉框列表加载成功
						//发送第二次请求,根据市场活动id获取
						var activityId = $cks[0].value;

						$.ajax({
							url:"workbench/activity/findById.do",
							data:{
								"id":activityId
							},
							type:"post",
							dataType:"json",
							success:function (data) {
								if (data.code==10000){
									$("#edit-name").val(data.data.name);
									$("#edit-startDate").val(data.data.startDate);
									$("#edit-endDate").val(data.data.endDate);
									$("#edit-cost").val(data.data.cost);
									$("#edit-description").val(data.data.description);
									//将市场活动id,保存到隐藏域中
									$("#edit-activityId").val(data.data.id);

									//默认选中,当前的数据库中的所有者名称
									$("#edit-owner").val(data.data.owner);

									//打开模态窗口
									$("#editActivityModal").modal("show");
								}
								else {
									alert(data.msg);
								}
							}
						})
					}
			}
		})
	})
	$("#updateBtn").click(function () {
		//获取属性
		var id = $("#edit-activityId").val();
		var owner = $("#edit-owner").val();
		var startDate = $("#edit-startDate").val();
		var endDate = $("#edit-endDate").val();
		var cost = $("#edit-cost").val();
		var description = $("#edit-description").val();
		var name = $("#edit-name").val();
		if (owner==""){
			alert("所有者不能为空");
			return ;
		}
		if (name==""){
			alert("名称不能为空");
			return ;
		}
		$.ajax({
			url:"workbench/activity/updateById.do",
			data:{
				"id":id ,
				"owner":owner ,
				"startDate":startDate ,
				"endDate":endDate ,
				"cost":cost ,
				"description":description ,
				"name":name
			},
			type:"post",
			dataType:"json",
			success:function (data) {
				if (data.code==10000){
					var pageNo = $("#activityPage").bs_pagination("getOption","currentPage");
					var pageSize = $("#activityPage").bs_pagination("getOption","rowsPerPage");
					findActivityList(pageNo,pageSize);
					$("#editActivityModal").modal("hide");

				}
			}
		})
	})
	$("#importActivityBtn").click(function () {
		// alert("aa");
		$("#importActivityForm").submit();


	})
		
	
}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								 <%-- <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button"  id="saveBtn" class="btn btn-primary" data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<input type="hidden" id="edit-activityId">
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label " >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" value="2020-10-10 " readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label " >结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" value="2020-10-20 " readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="updateBtn" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls或.xlsx格式]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">

						<form id="importActivityForm" action="workbench/activity/importActivityFile.do" method="post" enctype="multipart/form-data">
							<%--//enctype文件上传需要指定该属性--%>
                        <input type="file" id="activityFile" name="activityFile">
						</form>
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS/XLSX的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  <input type="hidden" id="hidden-activityName">
				  <input type="hidden" id="hidden-owner">
				  <input type="hidden" id="hidden-startDate">
				  <input type="hidden" id="hidden-endDate">
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-activityName">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="search-owner" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" readonly type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time"  readonly type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="openCreatActivityModelBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
<%--					data-toggle="modal" data-target="#editActivityModal"--%>
					<button type="button" class="btn btn-default"  id="openEditActivityModal" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityListBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			<!--引入的分页组件会填充到div标签中-->
			<div id="activityPage"></div>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>