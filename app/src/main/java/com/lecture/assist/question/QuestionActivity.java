package com.lecture.assist.question;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lecture.assist.R;
import com.lecture.assist.base.BaseActivity;
import com.lecture.assist.question.adapter.QuestionPagerAdapter;
import com.lecture.assist.question.event.MessageEvent;
import com.lecture.assist.question.module.HomeworkQuestionBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;



/**
 * @Author yinzh
 * @Date 2019/8/12 21:10
 * @Description
 */
public class QuestionActivity extends BaseActivity {

    ViewPager viewPager;

    TextView tv_title;

    LinearLayout ll_card;

    LinearLayout ll_finish;

    LinearLayout ll_back;


    //当前题目下标
    protected int mCurrentIndex;


    private QuestionPagerAdapter mAdapter;
    public ArrayList<HomeworkQuestionBean> mQuestionList = new ArrayList<>();


    @Override
    protected void logicActivity(Bundle savedInstanceState) {

        viewPager = findViewById(R.id.view_pager);
        tv_title = findViewById(R.id.tv_title);
        ll_card = findViewById(R.id.ll_card);
        ll_finish = findViewById(R.id.ll_finish);
        ll_back = findViewById(R.id.ll_back);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        initData();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_question;
    }

    public void initData() {
        tv_title.setText("第一章（练习二）");
        for (int i = 0; i < 4; i++) {
            ArrayList<String> metas = new ArrayList<>();
            metas.add("宪法");
            metas.add("法律");
            metas.add("行政法规");
            metas.add("行政规章");
            HomeworkQuestionBean homeworkQuestionBean1 = new HomeworkQuestionBean();
            homeworkQuestionBean1.type = HomeworkQuestionBean.HomeworkQuestionTypeBean.single_choice;
            homeworkQuestionBean1.setMetas(metas);
            homeworkQuestionBean1.setStem("1、下列发的形式中，由全国人民代表大会及其常务委员会经一定立法程序制定颁布，" +
                    "调整国家、社会和公民生活中基本社会关系的是（）。");
            mQuestionList.add(homeworkQuestionBean1);
        }


        for (int i = 0; i < 10; i++) {
            ArrayList<String> metas1 = new ArrayList<>();
            metas1.add("应当按照多数仲裁员的意见作出裁决");
            metas1.add("应当有仲裁庭达成一致意见作出在裁决");
            metas1.add("按照首席仲裁员的意见作出裁决");
            metas1.add("提请仲裁委员会作出裁决");
            HomeworkQuestionBean homeworkQuestionBean2 = new HomeworkQuestionBean();
            homeworkQuestionBean2.type = HomeworkQuestionBean.HomeworkQuestionTypeBean.fill;
            homeworkQuestionBean2.setMetas(metas1);
            homeworkQuestionBean2.setStem("2、甲、乙因合同纠纷申请仲裁，仲裁庭对案件裁决时两位仲裁员支持甲方的请求，但首席仲裁员支持乙的请求，关于该案件仲裁" +
                    "裁决的下列表述中，符合法律规定的是（）。");

            mQuestionList.add(homeworkQuestionBean2);
        }




        setStartExamData(mQuestionList);

        initListener();

        EventBus.getDefault().register(this);
    }


    private void setStartExamData(ArrayList<HomeworkQuestionBean> results) {
        mAdapter = new QuestionPagerAdapter(getBaseContext(), results);
        viewPager.setAdapter(mAdapter);

    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean flag;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //判断是不是最后一页，同是是不是拖的状态
                        if (viewPager.getCurrentItem() == mAdapter.getCount() - 1 && !flag) {
                            Toast.makeText(QuestionActivity.this,"已经是最后一题",Toast.LENGTH_LONG).show();
                        }
                        flag = true;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getType()) {
            case MessageEvent.EXAM_CHANGE_ANSWER:

                break;
            case MessageEvent.EXAM_NEXT_QUESTION:
                //自动下一题
                if (viewPager.getCurrentItem() == mQuestionList.size() - 1) {
                    Toast.makeText(QuestionActivity.this,"已经是最后一题",Toast.LENGTH_LONG).show();
                    return;
                }

                if (viewPager.getCurrentItem() < mQuestionList.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    EventBus.getDefault().cancelEventDelivery(messageEvent);
                }
                break;
        }
    }


}
