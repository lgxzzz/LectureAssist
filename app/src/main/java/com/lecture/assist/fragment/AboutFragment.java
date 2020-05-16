package com.lecture.assist.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
//import com.lecture.assist.LoginActivity;
import com.lecture.assist.LoginActivity;
import com.lecture.assist.R;
import com.lecture.assist.base.BaseFragment;

import java.util.List;

public class AboutFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "AboutFragment";
    //头像
    ImageView mHeadIcon;
    //姓名
    TextView mName;
    //专业
    TextView mMajor;
    //登陆
    ImageView mLogin;
    //积分
    TextView mScoreText;
    //加入的课程数
    TextView mJoinnumText;
    //退出按钮
    Button mLogoutBtn;

    public static AboutFragment getInstance() {

        return new AboutFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        Log.i(TAG, "onStart: success");
        super.onStart();
        final_user = AVUser.getCurrentUser();
        if (final_user != null){
            
            mName.setText(final_user.getUsername().toString());
            mMajor.setText(final_user.getString("school")+" "+final_user.get("major"));

            final int[] cr = {0};
            final int[] jo = {0};
            AVQuery<AVUser> avUserAVQuery = new AVQuery<>("_User");
            avUserAVQuery.getInBackground(final_user.getObjectId(), new GetCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null){
                        mScoreText.setText(avUser.get("all_rank")+"");
                        Log.i(TAG, "onStart: score =  "+avUser.get("all_rank"));
                        List<String> list_create = avUser.getList("create_wclass");
                        List<String> list_join = avUser.getList("join_wclass");
                        if (list_create != null){
                            cr[0] = list_create.size();
                            Log.i(TAG, "done: "+list_create.size()+" "+cr[0]);
                        }
                        if (list_join != null){
                            jo[0] = list_join.size();
                            Log.i(TAG, "done: "+list_join.size()+" "+jo[0]);
                        }
                        mJoinnumText.setText(cr[0] + jo[0] + "");
                    }
                }
            });
        }else {
            setEmpty();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void logic() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: onresume");
    }

    private void setEmpty() {
        mName.setText("请先登录");
        mMajor.setText("");
        mScoreText.setText("");
        mJoinnumText.setText("");
    }
    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

        mHeadIcon = mView.findViewById(R.id.me_image_avatar);
        mName = mView.findViewById(R.id.me_name);
        mMajor = mView.findViewById(R.id.me_major);
        mLogin = mView.findViewById(R.id.me_login);
        mJoinnumText = mView.findViewById(R.id.me_joinnum_text);
        mLogoutBtn = mView.findViewById(R.id.logout_btn);
        mScoreText = mView.findViewById(R.id.me_score_text);
        mLogin.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_about;
    }

    public void onLoginClicked() {
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
    }
    public void onLogoutBtnClicked() {
        if (final_user != null){
            AVUser.logOut();// 清除缓存用户对象
            final_user = null;
            Log.i(TAG, "onLogoutBtnClicked: "+AVUser.getCurrentUser());
        }
        setEmpty();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_login:
                onLoginClicked();
                break;
            case R.id.logout_btn:
                onLogoutBtnClicked();
                break;
        }
    }
}
