package com.rdc.bms.util;

import android.content.SharedPreferences;
import com.rdc.bms.app.App;
import com.rdc.bms.entity.User;

/**
 * Created by Lin Yaotian on 2018/5/15.
 */
public class UserUtil {

    /**
     * 登陆时记住帐号和密码
     * @param account
     * @param password
      */
    public static void rememberUser(String account, String password){
        User user = new User();
        user.setUserId(account);
        user.setPassword(password);
        String u = GsonUtil.gsonToJson(user);
        SharedPreferences.Editor editor = App.getContext().getSharedPreferences("user",0).edit();
        editor.putString("rememberUser",u);
        editor.apply();
    }

    /**
     * 获取登录时记住的帐号和密码
     * @return user if remembered User ,or return null
     */
    public static User getRememberedUser(){
        SharedPreferences sp = App.getContext().getSharedPreferences("user",0);
        return GsonUtil.gsonToBean(sp.getString("rememberUser",null),User.class);
    }

    /**
     * 清除记住的帐号和密码
     */
    public static void clearRememberUser(){
        SharedPreferences.Editor editor = App.getContext().getSharedPreferences("user",0).edit();
        editor.putString("rememberUser",null);
        editor.apply();
    }

    /**
     * 保存登录成功时的临时用户信息
     * @param user
     */
    public static void saveTemporaryUser(User user){
        String u = GsonUtil.gsonToJson(user);
        SharedPreferences.Editor editor = App.getContext().getSharedPreferences("user",0).edit();
        editor.putString("temporaryUser",u);
        editor.apply();
    }

    /**
     * 清除保存的临时用户信息
     */
    public static void clearTemporaryUser(){
        SharedPreferences.Editor editor = App.getContext().getSharedPreferences("user",0).edit();
        editor.putString("temporaryUser",null);
        editor.apply();
    }

    /**
     * 获取临时的用户信息
     * @return
     */
    public static User getTemporaryUser(){
        SharedPreferences sp = App.getContext().getSharedPreferences("user",0);
        String u = sp.getString("temporaryUser",null);
        return GsonUtil.gsonToBean(u,User.class);
    }


}
