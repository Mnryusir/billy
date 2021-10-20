package com.yuzhi.crm.settings.web.controller;

import com.yuzhi.crm.common.BaseResultEntity;
import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.settings.domain.User;
import com.yuzhi.crm.settings.service.UserService;
import com.yuzhi.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> login(String loginAct, String loginPwd, HttpServletRequest request,
                                    HttpServletResponse response,String flag){

        /*Map<String, Object> resultMap =new HashMap<>();*/
        //获取ip地址
        //此时我们不能使用localhost访问了，因为localhost无法获取访问ip
        String ip = request.getRemoteAddr();
        String md5Pwd= MD5Util.getMD5(loginPwd);
//        User user=userService.findByLoginPwdAndLoginPwd(loginAct,md5Pwd);
        Map<String, Object> resultMap = userService.findByLoginPwdAndLoginPwdCondition(loginAct,md5Pwd,ip);
        User user=(User) resultMap.get("data");//通过data获取用户对象
        if (user==null){

            return resultMap;
        }
/*        if(user == null){
            resultMap.put("code",10001);
            resultMap.put("msg","用户名或密码错误");
            resultMap.put("data",null);
            //1.用户名或密码错误
            //2.账号已过期
            //3.账号已锁定
            //4.ip地址受限
            return resultMap;
        }*/
        //登录成功
        //将User对象存入到Session中
        //后续就可以进行权限校验
        request.getSession().setAttribute("user",user);
        //十天免登录操作：向cookie中存入用户名以及密码
        if ("a".equalsIgnoreCase(flag)){
            Cookie loginActCookie = new Cookie("loginAct",loginAct);
            Cookie loginPwdCookie= new Cookie("loginPwd",md5Pwd);
            //设置cookie参数：path与过期时间
            //如果path没有设置为根目录，其他目录获取不到cookie的
            loginActCookie.setPath("/");
            loginPwdCookie.setPath("/");
            loginActCookie.setMaxAge(60*60*24*10);
            loginPwdCookie.setMaxAge(60*60*24*10);
            //将cookie写回浏览器
            response.addCookie(loginActCookie);
            response.addCookie(loginPwdCookie);
        }
 /*       resultMap.put("code",10000);
        resultMap.put("msg","登陆成功");
        resultMap.put("data",null);*/
        //登陆成功,清空data信息
        resultMap.remove("data");
        return resultMap;
     /*   request.getSession().setAttribute("user",user);
        resultMap.remove("data");
        return resultMap;*/
    }
    //跳转到登录页面
    @RequestMapping(value = "toLogin.do")
    public String toLogin(HttpServletRequest request){
//获得cookie对象
        Cookie[] cookies = request.getCookies();
        //定义一个存储用户名与密码的容器,该容器与上面添加到cookie中用户名与密码对象的变量值相同
        String loginAct=null;
        String loginPwd=null;
        //判断cookies是否为Null

        if(cookies!=null&&cookies.length>0){
            //对cookie完成一个遍历
            for (Cookie cookie : cookies) {
                //判断容器与cookie中的变量是否相同，相同则赋值给容器
                //完成了从cookie中获取数据的功能
                String name=cookie.getName();
                if ("loginAct".equalsIgnoreCase(name)){
                    loginAct=cookie.getValue();
                    continue;
                }
                if ("loginPwd".equalsIgnoreCase(name)){
                    loginPwd=cookie.getValue();
                    continue;
                }
            }

            String ip = request.getRemoteAddr();
            //自动登录,调用find方法查询用户并返回
            Map<String, Object> requestMap = userService.findByLoginPwdAndLoginPwdCondition(loginAct,loginPwd,ip);
            //根据返回的data判断user是否为空
            User user = (User) requestMap.get("data");
            if (user!=null){
                //user不为空，登陆成功，重新将user对象放入session中
                request.getSession().setAttribute("user",user);
                return "redirect:/workbench/toIndex.do";
            }
        } //不成功返回登陆页面
        return "/login";
    }
    @RequestMapping(value = "logout.do")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //清楚session中的user
        request.getSession().removeAttribute("user");
        //清除cookie
        Cookie loginAct = new Cookie("loginAct","");
        Cookie loginPwd = new Cookie("loginPwd","");
        loginAct.setPath("/");
        loginPwd.setPath("/");
        loginAct.setMaxAge(0);
        loginPwd.setMaxAge(0);
        response.addCookie(loginAct);
        response.addCookie(loginPwd);
        return "redirect:/settings/user/toLogin.do";
    }
    @RequestMapping("updatePwd.do")
    @ResponseBody
    public Map<Object,Object> updatePwd(String id,String loginPwd,String oldPwd,String newPwd){
        Map<Object, Object> resultMap = new HashMap<>();
        System.out.println("UserId:"+id);
        User user=userService.updatePwd(id,loginPwd,oldPwd,newPwd);
        //两种方法二选一
        /*更新session中数据
          如果十天免登录更新cookie中的数据*/
        if (user==null){
            resultMap.put("code",10001);
            resultMap.put("msg","原密码错误，请重新输入");
            return resultMap;
        }
        //更新就是创建新的cookie对象，然后赋值通过response写入浏览器

        resultMap.put("code",10000);
        resultMap.put("msg","更新成功");
        /*清除cookie与session中的数据
        重新登陆*/
        return resultMap;
    }
    @RequestMapping(value = "/findAll.do")
    @ResponseBody
    public BaseResultEntity<List<User>> findAll() throws AjaxRequestException {
        List<User> userList=userService.findAll();
        if (userList==null){
            throw new AjaxRequestException("查询失败");
        }
        return BaseResultEntity.ok(10000,"查询成功",userList);
    }
}
