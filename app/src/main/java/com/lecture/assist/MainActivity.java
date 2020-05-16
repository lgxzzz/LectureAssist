package com.lecture.assist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.lecture.assist.base.BaseActivity;
import com.lecture.assist.fragment.AboutFragment;
import com.lecture.assist.fragment.ClassFragment;
import com.lecture.assist.fragment.AnswerBookFragment;
import com.lecture.assist.utils.FragmentUtils;

public class MainActivity extends BaseActivity {

    public static final String TAG ="MainActivity";

    private BottomNavigationView mBottomMenu;
    @Override
    protected void logicActivity(Bundle savedInstanceState) {

        //检测sdk是否正常工作
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello World!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.e(TAG,"success!");
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });

        if (savedInstanceState != null){
            //从保存的状态取出上次选择的选项卡
            showFragment(savedInstanceState.getInt("bottom_select_id"));
        }else {
            //默认课程选项卡
            showFragment(R.id.bottom_menu_class);
        }

        init();

    }

    public void init(){
        mBottomMenu = findViewById(R.id.bottom_menu);
        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });
    }

    /**
     * 根据id显示相应的页面
     * @param menu_id
     */
    private void showFragment(int menu_id) {
        switch (menu_id){
            case R.id.bottom_menu_class:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, ClassFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_msg:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, AnswerBookFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_about:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, AboutFragment.getInstance(),R.id.main_frame);
                break;

        }
    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存当前选项卡ID
        super.onSaveInstanceState(outState);
        outState.putInt("bottom_select_id",mBottomMenu.getSelectedItemId());
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }
}
