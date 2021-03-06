package com.lecture.assist.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;


public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected AVUser final_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(getResourcesLayout(), container, false);
        final_user = AVUser.getCurrentUser();
        //Log.i(TAG, "onCreateView: "+final_user.getUsername());
        init(view,savedInstanceState);
        logic();
        return view;
    }

    protected abstract void logic();

    protected abstract void init(View mView, Bundle mSavedInstanceState);
    protected abstract int getResourcesLayout();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: "+getResourcesLayout());

    }
    protected void startActivityTo(Class  activityClass){
        Intent intent = new Intent(getContext(),activityClass);
        startActivity(intent);
    }
    protected void startActivityTo(Class  activityClass,String s){
        Intent intent = new Intent(getContext(),activityClass);
        Bundle bundle = new Bundle();
        bundle.putString("random_number",s);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

}
