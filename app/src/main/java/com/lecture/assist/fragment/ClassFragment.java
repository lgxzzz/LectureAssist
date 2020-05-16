package com.lecture.assist.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

//import com.lecture.assist.ClassActivity;
//import com.lecture.assist.CreateClassActivity;
//import com.lecture.assist.JoinClassActivity;
import com.lecture.assist.ClassActivity;
import com.lecture.assist.CreateClassActivity;
import com.lecture.assist.JoinClassActivity;
import com.lecture.assist.R;
//import com.lecture.assist.adapter.ClassFragmentAdapter;
import com.lecture.assist.adapter.ClassFragmentAdapter;
import com.lecture.assist.base.BaseFragment;
import com.lecture.assist.listener.OnClickerListener;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private ClassFragmentAdapter classFragmentAdapter;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView classFragmentRecycler;
    RapidFloatingActionButton activityMainRfab;
    RapidFloatingActionLayout activityMainRfal;
    private RapidFloatingActionHelper rfabHelper;
    private List list_create = new ArrayList<>();
    private List list_join = new ArrayList<>();
    private List<String>list_all = new ArrayList<>();
    private static final String TAG = "ClassFragment";

    public static ClassFragment getInstance() {
        // Required empty public constructor
        return new ClassFragment();
    }

    @Override
    protected void logic() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("创建课程")
                .setResId(R.drawable.ic_add_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("使用课程号加入")
                .setResId(R.drawable.ic_add_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                activityMainRfal,
                activityMainRfab,
                rfaContent
        ).build();
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
    }

    //从网络上获取数据更新数据
    private void updateData() {
        if (list_all.size()!= 0){
            list_all.clear();
        }
        if (final_user != null){
            AVQuery<AVUser>avUserAVQuery = new AVQuery<>("_User");
            avUserAVQuery.getInBackground(final_user.getObjectId(), new GetCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null){
                        list_all = getAllClass(avUser);
                        linearLayoutManager = new LinearLayoutManager(getActivity());
                        classFragmentRecycler.setLayoutManager(linearLayoutManager);
                        classFragmentAdapter = new ClassFragmentAdapter(getContext(),
                                new OnClickerListener() {
                                    @Override
                                    public void click(int position, View view) {
                                        Intent intent = new Intent(getContext(),ClassActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("class_random_number",list_all.get(position));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                },list_all);
                        Log.i(TAG, "updateData: "+list_all.size());
                        classFragmentRecycler.setAdapter(classFragmentAdapter);
                        classFragmentAdapter.notifyDataSetChanged();

                    }
                }
            });

        }
    }

    public List<String> getAllClass(AVUser avUser){
        List<String> temp_all_list = new ArrayList<>();
        Log.i(TAG, "done: "+avUser.getUsername()+avUser.get("create_wclass"));
        //获取该用户创建的课程
        list_create = avUser.getList("create_wclass");
        //获取该用户加入的课程
        list_join = avUser.getList("join_wclass");

        //判断课程是否已经结束，结束移除队列
        if (list_join != null){
            for (int i = list_join.size() - 1; i >= 0; i--) {
                AVQuery<AVObject> query = new AVQuery<>("ClassBean");
                query.whereEqualTo("class_random_number",list_join.get(i));
                final int finalI = i;
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(final AVObject avObject, AVException e) {
                        if (e == null){
                            if (avObject != null){
                                if (avObject.getBoolean("isend") == true){//已经结束的
                                    list_join.remove(finalI);
                                    Log.i(TAG, "done: this class has end ");
                                }
                            }
                        }
                    }
                });

            }
            //更新数据库正在加入的课程
            avUser.put("join_wclass",list_join);
            avUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null){
                        Log.e(TAG, "save join_class");
                    }else {
                        Log.e(TAG, "done: "+e.getMessage() );
                    }
                }
            });
        }

        if (list_create!=null){
            for (int i=0;i<list_create.size();i++){
                temp_all_list.add((String)list_create.get(i));
            }
        }

        if (list_join!=null){
            for (int i=0;i<list_join.size();i++){
                temp_all_list.add((String)list_join.get(i));
            }
        }
        return temp_all_list;
    }

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

        classFragmentRecycler = mView.findViewById(R.id.class_fragment_recycler);
        activityMainRfab = mView.findViewById(R.id.activity_main_rfab);
        activityMainRfal = mView.findViewById(R.id.activity_main_rfal);

    }
    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_class;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        startActivityByPosition(position);
    }
    private void startActivityByPosition(int position) {
        if (position == 0){
            Intent intent = new Intent(getContext(), CreateClassActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getContext(), JoinClassActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        startActivityByPosition(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}
