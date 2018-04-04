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

package com.example.jingbin.materialdesign.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingbin.materialdesign.R;
import com.example.jingbin.materialdesign.main.adapter.MyRecyclerViewAdapter;
import com.example.jingbin.materialdesign.main.adapter.MyStaggeredViewAdapter;
import com.example.jingbin.materialdesign.main.utils.SnackbarUtil;

import java.util.ArrayList;

public class MyFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, MyRecyclerViewAdapter.OnItemClickListener,
        MyStaggeredViewAdapter.OnItemClickListener {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private MyStaggeredViewAdapter mStaggeredAdapter;

    private static final int VERTICAL_LIST = 0;
    private static final int HORIZONTAL_LIST = 1;
    private static final int VERTICAL_GRID = 2;
    private static final int HORIZONTAL_GRID = 3;
    private static final int STAGGERED_GRID = 4;

    private static final int SPAN_COUNT = 2;
    private int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);

        flag = (int) getArguments().get("flag");
        configRecyclerView();

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.fab_red, R.color.main_black);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView() {

        switch (flag) {
            case VERTICAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case STAGGERED_GRID:
                mLayoutManager =
                        new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
            default:
                break;
        }

        if (flag != STAGGERED_GRID) {
            mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity());
            mRecyclerViewAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mStaggeredAdapter = new MyStaggeredViewAdapter(getActivity());
            mStaggeredAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mStaggeredAdapter);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        initRefresh();
    }

    private void initRefresh() {
        // RecyclerView上拉刷新
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    // 当不滚动时
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的itemposition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        // 加载更多
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i <= 10; i++) {
                            list.add("加班宝  " + i);
                        }
                        mRecyclerViewAdapter.mDatas.addAll(list);
                        mRecyclerViewAdapter.notifyDataSetChanged();
//                        SnackbarUtil.show(getActivity(), "加载成功");
//                        }
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });

    }

    @Override
    public void onRefresh() {

        // 刷新时模拟数据的变化
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                int temp = (int) (Math.random() * 10);
                if (flag != STAGGERED_GRID) {
                    mRecyclerViewAdapter.mDatas.add(0, "new" + temp);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    mStaggeredAdapter.mDatas.add(0, "new" + temp);
                    mStaggeredAdapter.mHeights.add(0, (int) (Math.random() * 300) + 200);
                    mStaggeredAdapter.notifyDataSetChanged();
                }
            }
        }, 3000);
    }

    @Override
    public void onItemClick(View view, int position) {
        SnackbarUtil.show(getActivity(), getString(R.string.item_clicked), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(getActivity(), getString(R.string.item_longclicked), 0);
    }
}
