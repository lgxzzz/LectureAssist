package com.lecture.assist.mgr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lecture.assist.bean.User;


public class LoginManager {

    private static LoginManager gManager= null;
    private User mLoginUser;
    private SharedPreferences sp;
    private Context mContext;

    private LoginManager(Context context) {
        this.mContext = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static LoginManager getInstance(Context context) {
        if (gManager==null){
            gManager = new LoginManager(context);
        }
        return gManager;
    }

    //登录
    public void doLogin(final User user, final LoginListenter listenter){

        if (isRemindPwd())
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name",user.getName());
            editor.putString("pwd",user.getPwd());
            editor.commit();
        }


    }

    //登出
    public void logout(){
        mLoginUser = null;
        savePWd(true,"","");
        setIsLogin(false);
    }

    //是否记住密码
    public void savePWd(boolean save, String name, String pwd){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("remind_pwd",save);
        if (save){
            editor.putString("name",name);
            editor.putString("pwd",pwd);
        }else {
            editor.remove("name");
            editor.remove("pwd");
        }

        editor.commit();
    }

    public boolean isRemindPwd(){
        boolean isRemind = sp.getBoolean("remind_pwd",false);
        return isRemind;
    }

    public boolean isLogin(){
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }

    public void setIsLogin(boolean isLogin){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",isLogin);
        editor.commit();
    }

    public String getPreName(){
        return sp.getString("name","");
    }

    public String getPrePwd(){
        return sp.getString("pwd","");
    }

    public User getUser(){
        return mLoginUser;
    }

    public void setUser(User user){
        this.mLoginUser = user;
    }

    public interface LoginListenter{
        public void onSuccess();
        public void onError(int error);
    }
}
