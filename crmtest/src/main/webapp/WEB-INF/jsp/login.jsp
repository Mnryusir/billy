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
    <!--前端业务操作-->
    <script>

        $(function (){
            $("#loginBtn").click(function (){

                var loginAct=$("#loginAct").val();
                var loginPwd=$("#loginPwd").val();

                if (loginAct==null||loginAct==""){

                    $("#msg").html("请输入用户名");
                    return ;
                }
                if (loginPwd==null||loginPwd==""){
                    $("#msg").html("请输入密码");
                }
                var flag=$("#flag").val();
                $("#msg").html("");

                $.ajax({
                    url:"settings/user/login.do",
                    data:{
                        "loginAct":loginAct,
                        "loginPwd":loginPwd,
                        "flag":flag
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data){
                        if (data.code==10000){
                            // $("#msg").html(data.msg);
                            window.location.href="workbench/toIndex.do";
                        }
                        else {
                            $("#msg").html(data.msg);
                        }
                    }
                })
            })
            $("#ck").click(function (){
                var ck=this.checked;//获取复选框的状态
                console.log("ck",ck);//通过日志的方式来直观感受复选框选中【可有可无，不影响代码】

                if (ck){
                    //选中十天免登录
                    //选中的话存储一个标记
                    $("#flag").val("a")
                }
                else {
                    //未选中10天免登录
                    //未选中存储一个空字符串
                    $("#flag").val("")

                }
            })
            $("#ck").prop("checked",false);//清空十天免登录的选中状态

        })

    </script>
</head>
<body>

<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: #ffffff; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <!--workbench/index.jsp-->
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" id="loginAct" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" id="loginPwd" type="password" placeholder="密码">
                </div>
                <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
                    <label>
                        <input type="checkbox" id="ck"> 十天内免登录1
                        <input type="hidden" id="flag">
                    </label>
                    &nbsp;&nbsp;
                    <span id="msg"></span>
                </div>
                <button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>