package com.lecture.assist.question;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.lecture.assist.R;
import com.lecture.assist.base.BaseActivity;
import com.lecture.assist.question.adapter.MaterialAdapter;
import com.lecture.assist.question.module.HomeworkQuestionBean;

import java.util.ArrayList;



/**
 * @Author yinzh
 * @Date 2019/8/20 14:55
 * @Description
 */
public class MaterialQuestionActivity extends BaseActivity {

    RecyclerView recyclerView;

    private MaterialAdapter adapter;


    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.material_recycler);

        initData();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_material;
    }

    final byte[] sendLed = {(byte) 0xCC, (byte) 0xEE, (byte) 0x01, (byte) 0x09,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF};


    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    public void initData() {
        String str= toHexString(sendLed);
        Log.i("AAAAAAA","str" + str);
        ArrayList<HomeworkQuestionBean> arrayList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            Log.e("AAAAAAAAa","i = "+ i);
            HomeworkQuestionBean homeworkQuestionBean = new HomeworkQuestionBean();
            homeworkQuestionBean.setItemType(i);
            arrayList.add(homeworkQuestionBean);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        View view = getLayoutInflater().inflate(R.layout.item1, null);


        adapter = new MaterialAdapter(arrayList);

        adapter.addHeaderView(view);


        recyclerView.setAdapter(adapter);


    }
}
