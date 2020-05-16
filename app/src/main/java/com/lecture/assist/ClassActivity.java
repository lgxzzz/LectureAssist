package com.lecture.assist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.lecture.assist.base.BaseActivity;
import com.lecture.assist.fragment.DoFragment;
import com.lecture.assist.fragment.MemberFragment;
import com.lecture.assist.fragment.ShowFragment;
import com.lecture.assist.utils.FragmentUtils;


public class ClassActivity extends BaseActivity {

    private static final String TAG = "ClassActivity";
    FrameLayout classFrame;
    BottomNavigationView classBottomMenu;
    private String class_random_number;


    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        Intent intent =getIntent();
        class_random_number = intent.getExtras().getString("class_random_number");

        if (savedInstanceState != null){
            Log.i(TAG, "ClassActivity: "+savedInstanceState.getInt("bottom_id_class"));
            showFragment(savedInstanceState.getInt("bottom_id_class"));
        }else {
            FragmentUtils.replaceFragmentToActivity(fragmentManager, MemberFragment.getInstance(class_random_number),R.id.class_frame);
        }
        classBottomMenu = findViewById(R.id.class_bottom_menu);
        classBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
            case R.id.bottom_person:
                FragmentUtils.replaceFragmentToActivity(fragmentManager,MemberFragment.getInstance(class_random_number),R.id.class_frame);
                break;
            case R.id.bottom_do:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, DoFragment.getInstance(class_random_number),R.id.class_frame);
                break;
            case R.id.bottom_show:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, ShowFragment.getInstance(class_random_number),R.id.class_frame);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("bottom_id_class",classBottomMenu.getSelectedItemId());
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_clas;
    }

}
