package com.yuzhi.crm.exception.resolver;

import com.yuzhi.crm.exception.AjaxRequestException;
import com.yuzhi.crm.exception.InterceptorException;
import com.yuzhi.crm.exception.LoginException;
import com.yuzhi.crm.exception.TranditionRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionResolver {
//异常处理器
    @ExceptionHandler(value= LoginException.class)
    @ResponseBody
    public Object loginExceptionResolver(Exception e){

        e.printStackTrace();

        Map<String,Object> map = new HashMap<>();
        map.put("code",10001);
        map.put("msg",e.getMessage());

        return map;

    }

    @ExceptionHandler(value= InterceptorException.class)
    public String interceptorExceptionResolver(Exception e){

        System.out.println("处理没登陆,重定向到登录页面");

        e.printStackTrace();

        return "redirect:/settings/user/toLogin.do";

    }

    @ExceptionHandler(value= AjaxRequestException.class)
    @ResponseBody
    public Object ajaxRequestExceptionResolver(Exception e){

        e.printStackTrace();

        System.out.println("---------------------====================================");

        Map<String,Object> map = new HashMap<>();
        map.put("code",10001);

        return map;

    }
    @ExceptionHandler(value= TranditionRequestException.class)
    public String traditionRequestExceptionResolver(Exception e){

        e.printStackTrace();

        return "redirect:/fail.jsp";

    }

    @ExceptionHandler(value= Exception.class)
    public String exceptionResolver(Exception e){

        e.printStackTrace();

        return "redirect:/fail.jsp";

    }
}





































