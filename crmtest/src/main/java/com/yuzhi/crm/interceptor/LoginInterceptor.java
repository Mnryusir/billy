package com.yuzhi.crm.interceptor;

import com.yuzhi.crm.exception.InterceptorException;
import com.yuzhi.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//工程刚搭建,先放行任何请求
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            //跳转到登陆页面
            //通过后台跳转
            //放到jsp文件夹内（为了使视图解析器可以生效）
            //jsp文件移动后会在路径后自动添加../，需要将其移除
            //跳转到某页面方法都是以to开头命名的方法
//            return "redirect:/settings/user/tologin.do";
/*后台跳转的两种方法:
    1.通过return "redirect:/settings/user/tologin.do"这种方式
    2.抛出异常让异常处理器类来完成跳转
 */
            throw new InterceptorException();


//            return false;
            /*redirect:重定向    重定向到Usercontroller(/settings/user为controller的别名)
            的tologin.do方法，该方法目前还没写，意思就是只要发现session中的user为空
            就跳转到登陆页面，防止用户通过内容网址的方式绕过登陆直接访问*/
//重定向次数过多是因为拦截器没有放行当前请求
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
