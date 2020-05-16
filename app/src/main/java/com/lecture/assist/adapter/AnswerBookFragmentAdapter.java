package com.lecture.assist.adapter;//package com.lecture.assist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecture.assist.R;
import com.lecture.assist.listener.OnClickerListener;

import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 */

public class AnswerBookFragmentAdapter extends RecyclerView.Adapter<AnswerBookFragmentAdapter.ViewHolder> {
    private Context context;
    private List<String> bookNameList;
    private OnClickerListener onClickerListener;

    public AnswerBookFragmentAdapter(Context context, OnClickerListener onClickerListener, List<String>bookNameList) {
        this.context = context;
        this.onClickerListener = onClickerListener;
        this.bookNameList = bookNameList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.answerbook_recycler_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {
        holder.bookName.setText(bookNameList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickerListener.click(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookNameList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookName;

        public ViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.answerbook_avator_item);
            bookName = itemView.findViewById(R.id.book_name);
        }

    }

}
