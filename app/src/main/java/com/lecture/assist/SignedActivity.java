package com.lecture.assist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lecture.assist.base.BaseActivity;

import java.util.List;


public class SignedActivity extends BaseActivity {

    private static final String TAG = "SignedActivity";
    EditText keyEdit;
    Button startSignedBtn;
    Button endSignedBtn;
    TextView signedText;
    private String random_number;
    private String key;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        random_number = getIntent().getExtras().getString("random_number");
        init();
    }

    public void init(){
        keyEdit = findViewById(R.id.key_edit);
        startSignedBtn = findViewById(R.id.start_signed_btn);
        endSignedBtn = findViewById(R.id.end_signed_btn);
        signedText = findViewById(R.id.signed_text);

        startSignedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartSignedBtnClicked();
            }
        });

        endSignedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEndSignedBtnClicked();
            }
        });
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_signed;
    }

    public void onStartSignedBtnClicked() {
        AVQuery<AVObject> query = new AVQuery<>("ClassBean");
        query.whereEqualTo("class_random_number", random_number);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    Log.i(TAG, "done: 获取房间信息成功");
                    if (avObject != null) {
                        //获取课堂的管理者
                        key = keyEdit.getText().toString().trim();
                        avObject.put("class_signin_number",key);
                        avObject.put("user_position",LectureAssistApplication.mLongitude+","+LectureAssistApplication.mLatitude);
                        Log.i(TAG, "done: "+key);
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null){
                                    Toast.makeText(SignedActivity.this, "签到成功开始", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(activity, "房间不存在", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        refreshMsg();
    }
    public void onEndSignedBtnClicked() {
        mHandler.removeCallbacksAndMessages(null);
        AVQuery<AVObject> query = new AVQuery<>("ClassBean");
        query.whereEqualTo("class_random_number", random_number);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    Log.i(TAG, "done: 获取房间信息成功");
                    if (avObject != null) {
                        avObject.put("class_signin_number","");
                        Log.i(TAG, "done: "+key);
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null){
                                    Toast.makeText(SignedActivity.this, "签到成功关闭", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(activity, "房间不存在", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    public static final int MSG_REFRESH = 0x01;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MSG_REFRESH:
                    querySigneList();
                    refreshMsg();
                    break;
            }
            return false;
        }
    });

    public void refreshMsg(){
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MSG_REFRESH,1000);
    }

    public void querySigneList(){
        AVQuery<AVObject> query = new AVQuery<>("ClassBean");
        query.whereEqualTo("class_random_number", random_number);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    if (avObject != null) {
                        avObject.put("class_signin_number","");
                        Log.i(TAG, "done: "+key);
                        List<String>user_list = avObject.getList("user_list");//所有用户列表
                        if (user_list != null) {
                            List<String> user_sigin = avObject.getList("user_sigin");
                            if (user_sigin != null) {
                                final List<String> temp = user_list;
                                final String[] s = {""};
                                for (int i = 0; i < temp.size(); i++) {
                                    final AVQuery<AVUser> avQuery = new AVQuery<>("_User");
                                    avQuery.getInBackground(temp.get(i), new GetCallback<AVUser>() {
                                        @Override
                                        public void done(AVUser avUser, AVException e) {
                                            s[0] += avUser.getUsername() + " "+avUser.get("st_number")+"   ";
                                            signedText.setText(s[0]);
                                        }
                                    });
                                }

                            }

                        }
                    }
                }

            }
        });
    }
}
