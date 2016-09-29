/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.example.jingbin.materialdesign.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingbin.materialdesign.R;
import com.example.jingbin.materialdesign.detail.Book;
import com.example.jingbin.materialdesign.detail.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

  public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
  }

  public OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public Context mContext;
  public List<String> mDatas;
  public LayoutInflater mLayoutInflater;

  public MyRecyclerViewAdapter(Context mContext) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
    mDatas = new ArrayList<>();
    for (int i = 'A'; i <= 'z'; i++) {
      mDatas.add("加班宝  "+(char) i + "");
    }
  }

  /**
   * 创建ViewHolder
   */
  @Override public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
    MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
    if (mOnItemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
//          mOnItemClickListener.onItemClick(holder.itemView, position);
          Book book = new Book();
          book.setTitle("中医");
          book.setSummary("内容简介");
          book.setAuthor_intro("作者简介");
          book.setCatalog("目录");
          Intent intent = new Intent(mContext, BookDetailActivity.class);
          intent.putExtra("book", book);

          ActivityOptionsCompat options =
                  ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                          holder.image_id, mContext.getString(R.string.transition_book_img));

          ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());

        }
      });

      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          mOnItemClickListener.onItemLongClick(holder.itemView, position);
          return true;
        }
      });
    }

    holder.mTextView.setText(mDatas.get(position));
  }

  @Override public int getItemCount() {
    return mDatas.size();
  }
}
