package com.yuzhi.crm.settings.service.Imp;

import com.yuzhi.crm.settings.dao.UserDao;
import com.yuzhi.crm.settings.domain.User;
import com.yuzhi.crm.settings.service.UserService;
import com.yuzhi.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findByLoginPwdAndLoginPwd(String loginAct, String md5Pwd) {
        User user = userDao.findByLoginPwdAndLoginPwd(loginAct, md5Pwd);
        return user;
    }

    @Override
    public Map<String, Object> findByLoginPwdAndLoginPwdCondition(String loginAct, String md5Pwd,String ip) {
        //根据用户名密码查询用户
        User user = userDao.findByLoginPwdAndLoginPwd(loginAct,md5Pwd);
        Map<String, Object> resultMap = new HashMap<>();
        //用户名或密码错误
        if (user==null){
            resultMap.put("code",10001);
            resultMap.put("msg","用户名或密码错误");
            resultMap.put("data",null);
            return resultMap;
        }
        //校验当前用户是否过期
        //expireTime为设置的过期时间
        String expireTime = user.getExpireTime();
        //获取当前时间
        String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        //expireTime为null代表过期时间永不过期，不为Null才有必要去判断
        if (expireTime!=null){
   /*         System.out.println(expireTime);
            System.out.println(now);
            System.out.println(expireTime.compareTo(now));*/
            if (expireTime.compareTo(now)<0){//小于0为过期
                resultMap.put("code",10002);
                resultMap.put("msg","用户已过期");
                resultMap.put("data",null);
                return resultMap;
            }
        }
        //校验账号是否被锁定
        String lockState = user.getLockState();
        //lockState为null代表账号用不被锁定
        if (lockState!=null){
            if ("0".equals(lockState)){
                resultMap.put("code",10003);
                resultMap.put("msg","账号被锁定");
                resultMap.put("data",null);
                return resultMap;
            }
        }
        //ip地址是否受限
        String allowIps = user.getAllowIps();
        if (allowIps!=null){
            if (!allowIps.contains(ip)){
                resultMap.put("code",10004);
                resultMap.put("msg","ip已失效，请联系管理员");
                resultMap.put("data",null);
                return resultMap;
            }
        }
        resultMap.put("code",10000);
        resultMap.put("msg","登陆成功");
        resultMap.put("data",user);
        return resultMap;

    }

    @Override
    public User updatePwd(String id, String loginPwd, String oldPwd, String newPwd) {
        User user=userDao.findById(id);

        if (user!=null){
            if (user.getLoginPwd().equals(MD5Util.getMD5(oldPwd))){
                String md5=MD5Util.getMD5(newPwd);
                user.setLoginPwd(md5);
                userDao.updateLoginPwdById(user);
                return user;
            }
            else {
                return null;
            }
        }
        return null;

    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
