package com.yuzhi.crm.settings.service;

import com.yuzhi.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {


    //普通的用户登陆
    User findByLoginPwdAndLoginPwd(String loginAct, String md5Pwd);
    //返回具体的登陆成功或失败的数据
    Map<String, Object> findByLoginPwdAndLoginPwdCondition(String loginAct, String md5Pwd,String ip);

    User updatePwd(String id, String loginPwd, String oldPwd, String newPwd);

    List<User> findAll();
}
