package com.lecture.assist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lecture.assist.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class CreateClassActivity extends BaseActivity {

    private static final String TAG = "CreateClassActivity";
    ImageView createImage;
    EditText classNameEdit;
    EditText classMajorEdit;
    Button createClassBtn;
    EditText classLongtimeEdit;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_create_class;
    }

    public void init(){
        createImage = findViewById(R.id.create_image);
        classNameEdit = findViewById(R.id.class_name_edit);
        classMajorEdit = findViewById(R.id.class_major_edit);
        createClassBtn = findViewById(R.id.create_class_btn);
        classLongtimeEdit = findViewById(R.id.class_longtime_edit);

        createClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateClassBtnClicked();
            }
        });
    }

    //创建
    public void onCreateClassBtnClicked() {
        if (final_user != null){
            String class_class = classNameEdit.getText().toString().trim();
            String class_name = classMajorEdit.getText().toString().trim();
            String class_duration = classLongtimeEdit.getText().toString().trim();
            final String class_number = (int)Math.rint((Math.random() * 9 + 1) * 100000)+"";
            String class_owner_user = final_user.getObjectId();
            AVObject w_class =new AVObject("ClassBean");
            w_class.put("class_class",class_class);
            w_class.put("class_name",class_name);
            w_class.put("class_duration",class_duration);
            w_class.put("class_owner_user",class_owner_user);
            w_class.put("class_random_number",class_number);
            w_class.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null){
                        Snackbar.make(findViewById(R.id.create_class_btn),"创建成功", Snackbar.LENGTH_LONG)
                                .setAction("进入课堂", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(activity, ClassActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("class_random_number",class_number);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                }).show();
                        final AVQuery<AVUser> avQuery = new AVQuery<>("_User");
                        avQuery.getInBackground(final_user.getObjectId(), new GetCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                List<String>list = avUser.getList("create_wclass");
                                if (list == null){
                                    list = new ArrayList<>();
                                }
                                list.add(class_number);
                                avUser.put("create_wclass",list);
                                avUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null){
                                            Log.i(TAG, "done: success update");
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        Toast.makeText(activity, "创建失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(activity,"请先登录",Toast.LENGTH_SHORT).show();
        }

    }

}
