package com.lecture.assist.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lecture.assist.R;
import com.lecture.assist.adapter.AnswerBookFragmentAdapter;
import com.lecture.assist.base.BaseFragment;
import com.lecture.assist.constant.DataSourse;
import com.lecture.assist.listener.OnClickerListener;
import com.lecture.assist.question.QuestionActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerBookFragment extends BaseFragment {

    private static final String TAG = "AnswerBookFragment";

    private LinearLayoutManager mLinearLayoutManager;

    RecyclerView mAnswerBookFragmentRecycler;
    AnswerBookFragmentAdapter mAdapter;

    List<String> mBookNames = new ArrayList<>();

    public static AnswerBookFragment getInstance() {
        // Required empty public constructor
        return new AnswerBookFragment();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        Log.i(TAG, "onStart: success");
        super.onStart();
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

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

        initData();

        mAnswerBookFragmentRecycler = mView.findViewById(R.id.answerbook_fragment_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAnswerBookFragmentRecycler.setLayoutManager(gridLayoutManager);
        mAdapter = new AnswerBookFragmentAdapter(getContext(), new OnClickerListener() {
            @Override
            public void click(int position, View view) {
                Intent intent = new Intent(getContext(), QuestionActivity.class);
                startActivity(intent);
            }
        },mBookNames);
        mAnswerBookFragmentRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_answerbook;
    }

    public void initData(){
        for (int i=0;i< DataSourse.COURSE.length;i++){
            mBookNames.add(DataSourse.COURSE[i]);
        }
    }
}
