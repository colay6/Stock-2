package com.chhd.stock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chhd.stock.R;
import com.chhd.stock.activity.NewsActivity;
import com.chhd.stock.adapter.NewsAdapter;
import com.chhd.stock.entity.News;
import com.chhd.stock.http.RequestNewsInfo;
import com.chhd.stock.util.Consts;
import com.chhd.stock.util.Tools;
import com.chhd.util.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/7/27.
 */
public class NewsFragment extends Fragment {

    private View mView;
    private PullToRefreshListView ptrlv_newss;

    private Handler mHandler = new MyHandler();
    private List<News> mNewss = new ArrayList<>();
    private NewsAdapter mNewsAdapter;
    private int mPage;
    private long mStartTime;
    private long mEndTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_news, null);

        initViews();

        initILoadingLayout();

//        test();

        setAdapter();

        setListeners();

        ptrlv_newss.setRefreshing(true);

        return mView;
    }

    private void initViews() {
        ptrlv_newss = (PullToRefreshListView) mView.findViewById(R.id.ptrlv_newss);
    }

    private void initILoadingLayout() {
        ptrlv_newss.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv_newss.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_newss.getLoadingLayoutProxy(true, false).setReleaseLabel("松手刷新");
        ptrlv_newss.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_newss.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_newss.getLoadingLayoutProxy(false, true).setReleaseLabel("上拉开始加载");
        ptrlv_newss.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载更多...");
    }

    private void test() {
        mNewss.add(new News());
        mNewss.add(new News());
        mNewss.add(new News());
        mNewss.add(new News());
        mNewss.add(new News());
    }

    private void setAdapter() {
        mNewsAdapter = new NewsAdapter(getActivity(), mNewss);
        ptrlv_newss.setAdapter(mNewsAdapter);
    }

    private void setListeners() {
        ptrlv_newss.setOnRefreshListener(new MyOnRefreshListener());
        ptrlv_newss.setOnItemClickListener(new MyOnItemClickListener());
    }

    private class MyOnRefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            mStartTime = System.currentTimeMillis();
            if (refreshView.getFooterLayout().isShown()) {
                mPage++;
                if (mPage > 10) {
                    mHandler.sendEmptyMessage(Consts.HANDLER_ALREADY_LOADING_FINISH);
                    ToastUtil.toast(getActivity(), "已经加载完毕了");
                    return;
                }
                RequestNewsInfo requestNewsInfo = new RequestNewsInfo(mHandler);
                requestNewsInfo.requestMore(Consts.SHARES, "" + mPage);
            } else {
                mPage = 1;
                RequestNewsInfo requestNewsInfo = new RequestNewsInfo(mHandler);
                requestNewsInfo.request(Consts.SHARES);
            }
        }
    }

    private class MyHandler extends Handler {

        private List<News> newss = new ArrayList<>();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Consts.HANDLER_ALREADY_LOADING_FINISH:
                    ptrlv_newss.onRefreshComplete();
                    break;
                case Consts.HANDLER_REQUEST_NEWS_FAIL:
                    ptrlv_newss.onRefreshComplete();
                    break;
                case Consts.HANDLER_REQUEST_NEWS_INFO_SUCCESS:
                    mEndTime = System.currentTimeMillis();
                    newss = (List<News>) msg.obj;
                    mNewss.clear();
                    mNewss.addAll(newss);

                    List<News> banners = Tools.getRandomNoRepeatList(newss);
                    Intent intent = new Intent(Consts.ACTION_UPDATE_BANNERS);
                    intent.putParcelableArrayListExtra(Consts.KEY_BANNERS, (ArrayList<? extends Parcelable>) banners);
                    getActivity().sendBroadcast(intent);

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                long time = mEndTime - mStartTime;
                                if (time < Consts.LOADING_TIME) {
                                    sleep(Consts.LOADING_TIME - time);
                                }
                                sendEmptyMessage(Consts.HANDLER_REFRESH_NEWS_LISTVIEW);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                case Consts.HANDLER_REQUEST_MORE_NEWS_INFO_SUCCESS:
                    mEndTime = System.currentTimeMillis();
                    newss = (List<News>) msg.obj;
                    mNewss.addAll(newss);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                long time = mEndTime - mStartTime;
                                if (time < Consts.LOADING_TIME) {
                                    sleep(Consts.LOADING_TIME - time);
                                }
                                sendEmptyMessage(Consts.HANDLER_REFRESH_NEWS_LISTVIEW);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                case Consts.HANDLER_REFRESH_NEWS_LISTVIEW:
                    ptrlv_newss.onRefreshComplete();
                    mNewsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra(Consts.KEY_NEWS, mNewss.get(position - 1));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
        }
    }
}
