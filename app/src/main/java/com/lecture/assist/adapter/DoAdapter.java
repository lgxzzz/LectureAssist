package com.lecture.assist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.lecture.assist.R;
import com.lecture.assist.listener.OnClickerListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/4/30.
 */

public class DoAdapter extends RecyclerView.Adapter<DoAdapter.ViewHolder> {
    private Context context;
    private OnClickerListener onClickerListener;
    private List<String>discuss_list = new ArrayList<>();
    public DoAdapter(Context context, OnClickerListener onClickerListener, List<String>list) {
        this.context = context;
        this.onClickerListener = onClickerListener;
        discuss_list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.do_recycler_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (onClickerListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickerListener.click(holder.getLayoutPosition(),holder.itemView);
                }
            });
        }
            final AVQuery<AVObject> discuss_query = new AVQuery<>("Discuss");
            discuss_query.whereEqualTo("objectId",discuss_list.get(position));
            discuss_query.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if ( e == null){
                        if (avObject  != null){
                            holder.doItemTitle.setText(avObject.getString("type")+"/"+avObject.getString("title"));
                            holder.doItemJoinnum.setText(discuss_list.size()+" 人参与");
                            holder.doItemType.setText(avObject.getString("type"));
                            holder.doItemScore.setText(avObject.getString("score")+" 经验");

                        }
                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return discuss_list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doItemImage;
        TextView doItemJoinnum;
        TextView doItemTitle;
        TextView doItemType;
        TextView doItemScore;

        ViewHolder(View view) {
            super(view);
            doItemImage = view.findViewById(R.id.do_item_image);
            doItemJoinnum = view.findViewById(R.id.do_item_joinnum);
            doItemTitle = view.findViewById(R.id.do_item_title);
            doItemType = view.findViewById(R.id.do_item_type);
            doItemScore = view.findViewById(R.id.do_item_score);
        }
    }
}
