package com.yuzhi.crm.settings.dao;

import com.yuzhi.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User findByLoginPwdAndLoginPwd(@Param("loginAct") String loginAct,@Param("loginPwd") String md5Pwd);

    User findById(String id);

    void updateLoginPwdById(User user);

    List<User> findAll();
}
