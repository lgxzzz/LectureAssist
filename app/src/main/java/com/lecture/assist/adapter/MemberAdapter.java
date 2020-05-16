package com.lecture.assist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.lecture.assist.R;
import com.lecture.assist.listener.OnClickerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/30.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private Context context;
    private OnClickerListener onClickerListener;
    private List<Map.Entry<String, Integer>> map = new ArrayList<>();
    private static final String TAG = "MemberAdapter";

    public MemberAdapter(Context context, OnClickerListener onClickerListener, List<Map.Entry<String, Integer>> map) {
        this.context = context;
        this.onClickerListener = onClickerListener;
        this.map = map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_recycler_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (onClickerListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickerListener.click(holder.getLayoutPosition(),holder.itemView);
                }
            });
        }
        final AVQuery<AVUser> avQuery = new AVQuery<>("_User");
        avQuery.getInBackground(map.get(position).getKey(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                  holder.memberItemRank.setText(position + 1 +"");
                  holder.memberItemAllScore.setText(map.get(position).getValue()+" 经验值");
                  holder.memberItemName.setText(avUser.getUsername());
                  holder.memberItemNumber.setText(avUser.get("st_number").toString());
                }else {
                    Log.i(TAG, "done: 哼 人家没找到");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberItemRank;
        ImageView memberItemAvator;
        TextView memberItemAllScore;
        TextView memberItemName;
        TextView memberItemNumber;
        ViewHolder(View view) {
            super(view);
            memberItemRank = view.findViewById(R.id.member_item_rank);
            memberItemAvator = view.findViewById(R.id.class_avator_item);
            memberItemAllScore = view.findViewById(R.id.class_random_number_item);
            memberItemName = view.findViewById(R.id.class_class_item);
            memberItemNumber = view.findViewById(R.id.class_name_item);
        }
    }
}
