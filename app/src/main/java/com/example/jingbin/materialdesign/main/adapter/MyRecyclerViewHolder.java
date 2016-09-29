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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingbin.materialdesign.R;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

  public TextView mTextView;
  public ImageView image_id;

  public MyRecyclerViewHolder(View itemView) {
    super(itemView);
    mTextView = (TextView) itemView.findViewById(R.id.content);
    image_id = (ImageView) itemView.findViewById(R.id.image_id);
  }
}
