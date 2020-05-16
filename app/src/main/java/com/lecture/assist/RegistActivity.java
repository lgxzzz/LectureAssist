package com.lecture.assist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.lecture.assist.base.BaseActivity;

public class RegistActivity extends BaseActivity implements View.OnClickListener{


    private static final String TAG = "RegistActivity";
    ImageView backRegisterImage;
    EditText usernameRegister;
    EditText passwordRegister;
    EditText phoneRegister;
    EditText stNumber;
    EditText schoolRegister;
    EditText majorRegister;
    Button registerBtn;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        init();
    }

    public void init(){

        backRegisterImage = findViewById(R.id.back_register_image);
        usernameRegister = findViewById(R.id.username_register);
        passwordRegister = findViewById(R.id.password_register);
        phoneRegister = findViewById(R.id.phone_register);
        stNumber = findViewById(R.id.st_number);
        schoolRegister = findViewById(R.id.school_register);
        majorRegister = findViewById(R.id.major_register);
        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(this);
        backRegisterImage.setOnClickListener(this);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_regist;
    }


    public void onBackRegisterImageClicked() {
        activity.finish();
    }

    public void onRegisterBtnClicked() {
        String username = usernameRegister.getText().toString().trim();
        String password = passwordRegister.getText().toString().trim();
        String phone_number = phoneRegister.getText().toString().trim();
        String st_number = stNumber.getText().toString().trim();
        String school = schoolRegister.getText().toString().trim();
        String major = majorRegister.getText().toString().trim();
        AVUser avUser = new AVUser();
        avUser.setMobilePhoneNumber(phone_number);
        avUser.setUsername(username);
        avUser.setPassword(password);
        avUser.put("st_number",st_number);
        avUser.put("school",school);
        avUser.put("major",major);
        avUser.put("all_rank",0);
        avUser.put("all_class",0);
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    Log.i(TAG, "done: success login");
                    Snackbar.make(findViewById(R.id.register_btn),"注册成功", Snackbar.LENGTH_LONG).
                            setAction("点击登录", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(activity,LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                }else {
                    Snackbar.make(findViewById(R.id.register_btn),"注册失败"+e.getMessage(), Snackbar.LENGTH_LONG)
                           .show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn:
                onRegisterBtnClicked();
                break;
            case R.id.back_register_image:
                onBackRegisterImageClicked();
                break;
        }
    }
}
