package com.lecture.assist.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import com.lecture.assist.MemberActivity;
import com.lecture.assist.R;
import com.lecture.assist.adapter.MemberAdapter;
import com.lecture.assist.base.BaseFragment;
import com.lecture.assist.listener.OnClickerListener;
import com.lecture.assist.utils.MapSortUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends BaseFragment {

    private static final String TAG = "MemberFragment";
    TextView appbarTitleText;
    TextView rank;
    TextView scoreText;
    RecyclerView classMemberRecycler;
    TextView sumMemberText;
    FloatingActionButton qiandaoBtn;
    ImageView backFromMemberImage;
    private MemberAdapter classFragmentAdapter;
    private static String random_number;
    private Map<String, Integer> map;
    private List<Map.Entry<String, Integer>> entries;

    public static MemberFragment getInstance(String class_random_number) {
        random_number = class_random_number;
        return new MemberFragment();

    }

    @Override
    protected void logic() {

    }

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {
        Log.i(TAG, "init: " + random_number);
        appbarTitleText = mView.findViewById(R.id.appbar_title_text);
        rank = mView.findViewById(R.id.rank);
        scoreText = mView.findViewById(R.id.score_text);
        classMemberRecycler = mView.findViewById(R.id.class_member_recycler);
        sumMemberText = mView.findViewById(R.id.sum_member_text);
        qiandaoBtn = mView.findViewById(R.id.qiandao_btn);
        backFromMemberImage = mView.findViewById(R.id.back_from_member_image);

        qiandaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClicked();
            }
        });

        backFromMemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_member_layout;
    }

    public void onViewClicked() {
        AVQuery<AVObject> query = new AVQuery<>("ClassBean");
        query.whereEqualTo("class_random_number", random_number);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    Log.i(TAG, "done: 签到已经开始");
                    if (avObject != null) {
                        if (avObject.get("class_signin_number")!=null){
                            String key = avObject.get("class_signin_number").toString();
                            if (key != null && !key.equals("") ){
                                startActivityTo(MemberActivity.class,random_number);
                            }else {

                                Toast.makeText(getActivity(), "签到未开始", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "签到码为空", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        AVQuery<AVObject> query = new AVQuery<>("ClassBean");
        query.whereEqualTo("class_random_number", random_number);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    Log.i(TAG, "done: 获取房间信息成功");
                    if (avObject != null) {
                        //获取课堂的管理者
                        final AVQuery<AVUser> avQuery = new AVQuery<>("_User");
                        avQuery.getInBackground(avObject.getString("class_owner_user"), new GetCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                //判断管理者和当前用户是否为同一人
                                if (avUser.getUsername().equals(AVUser.getCurrentUser().getUsername())) {
                                    qiandaoBtn.setVisibility(View.GONE);
                                    rank.setText("经验排行榜");
                                    scoreText.setText("");
                                } else {
                                    qiandaoBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        map = avObject.getMap("class_user_rank");
                        if (map == null) {
                            map = new HashMap<>();
                        }
                        entries = MapSortUtils.sort(map);
                        Log.i(TAG, "done: " + entries.size());

                        classFragmentAdapter = new MemberAdapter(getContext(), new OnClickerListener() {
                            @Override
                            public void click(int position, View view) {

                            }
                        }, entries);
                        classMemberRecycler.setAdapter(classFragmentAdapter);
                        classMemberRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        //绘制界面
                        appbarTitleText.setText(avObject.getString("class_name"));
                        if (avObject.getList("user_list") != null){
                            sumMemberText.setText(avObject.getList("user_list").size()+" 人");
                        }else {
                            sumMemberText.setText("0 人");
                        }
                        int index = 0;
                        for (Map.Entry<String, Integer> entry : entries) {
                            index ++;
                            if (entry.getKey().equals(AVUser.getCurrentUser().getObjectId())){
                                Log.i(TAG, "done: hi i find you");
                                int rank_index = index;//获取排名
                                int score_index = entry.getValue();//获取分数
                                rank.setText("第 "+rank_index+" 名");
                                scoreText.setText("获得 "+score_index+" 经验值");
                            }
                        }
                        index = 0;
                    } else {
                        Toast.makeText(getActivity(), "房间不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
