package com.lecture.assist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.lecture.assist.base.BaseActivity;


public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText editUsernameLogin;
    EditText editPasswordLogin;
    Button login;
    TextView forgetLogin;
    TextView registerBtn;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_login;
    }

    public void init(){

        editUsernameLogin = findViewById(R.id.edit_username_login);
        editPasswordLogin = findViewById(R.id.edit_password_login);
        login = findViewById(R.id.login);
        forgetLogin = findViewById(R.id.forget_login_text);
        registerBtn = findViewById(R.id.register_btn_text);

        login.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    public void onLoginClicked() {
        String phone_number = editUsernameLogin.getText().toString().trim();
        String password = editPasswordLogin.getText().toString().trim();
        AVUser.loginByMobilePhoneNumberInBackground(phone_number, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    Toast.makeText(activity,"登陆成功",Toast.LENGTH_SHORT).show();
                    final_user = AVUser.getCurrentUser();
                    activity.finish();

                }else {
                    Snackbar.make(findViewById(R.id.login),"登陆失败"+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    //忘记密码
    public void onForgetLoginClicked() {

    }
    //注册用户
    public void onRegisterBtnClicked() {
        Intent intent = new Intent(this,RegistActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                onLoginClicked();
                break;
            case R.id.register_btn_text:
                onRegisterBtnClicked();
                break;
            case R.id.forget_login_text:
                onForgetLoginClicked();
                break;
        }
    }
}
