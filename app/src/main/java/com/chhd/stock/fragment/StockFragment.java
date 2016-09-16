package com.chhd.stock.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chhd.stock.R;
import com.chhd.stock.activity.DetailActivity;
import com.chhd.stock.activity.SearchActivity;
import com.chhd.stock.adapter.StockAdapter;
import com.chhd.stock.app.MyApplication;
import com.chhd.stock.entity.News;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.entity.User;
import com.chhd.stock.entity.UserData;
import com.chhd.stock.http.RequestStockInfo;
import com.chhd.stock.util.Consts;
import com.chhd.stock.util.FormatUitl;
import com.chhd.util.LogUtil;
import com.chhd.util.ToastUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by CWQ on 2016/7/10.
 */
public class StockFragment extends Fragment {

    private View mView;
    private Button btn_title_bar_filter;
    private Button btn_title_bar_search;
    private Button btn_title_bar_cancel;
    private Button btn_title_bar_confirm;
    private RelativeLayout rl_stock_head;
    private PullToRefreshListView ptrlv_stocks;
    private LinearLayout ll_filter_list;
    private RadioGroup rg_category;
    private RadioGroup rg_sort;
    private TextView tv_bottom_info;

    private List<Stock> mStocks = new ArrayList<>();
    private List<News> mBanners = new ArrayList<>();
    private StockAdapter mStockAdapter;
    private RequestStockInfo mRequestStockInfo = new RequestStockInfo(new InnerHandler());
    private long mStartTime;
    private long mEndTime;
    private View.OnClickListener mOnClickListener;
    private List<Stock> mNewStocks = new ArrayList<>();
    private Comparator<Stock> mDefComparator = new DefComparator();
    private Comparator<Stock> mPriceComparator = new PriceComparator();
    private Comparator<Stock> mIncreaseComparator = new IncreaseComparator();
    private StringBuilder mStockIDSB = new StringBuilder();
    private boolean mIsRunning;
    private BroadcastReceiver mMyReceiver;
    private ILoadingLayout mLoadingLayoutProxy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_stock, null);

        initViews();

        initRadioGroupStatus();

        initData();

//        test();

        setAdapers();

        setListeners();

        setLoadingLayoutProxy();

        registerReceiver();

        ptrlv_stocks.setRefreshing(true);

        return mView;
    }

    private void initRadioGroupStatus() {
        rg_category.check(R.id.rb_all);
        rg_sort.check(R.id.rb_def);
    }

    private void initViews() {
        btn_title_bar_filter = (Button) mView.findViewById(R.id.btn_title_bar_filter);
        btn_title_bar_search = (Button) mView.findViewById(R.id.btn_title_bar_search);
        btn_title_bar_cancel = (Button) mView.findViewById(R.id.btn_title_bar_cancel);
        btn_title_bar_confirm = (Button) mView.findViewById(R.id.btn_title_bar_confirm);
        rl_stock_head = (RelativeLayout) mView.findViewById(R.id.rl_stock_head);
        ptrlv_stocks = (PullToRefreshListView) mView.findViewById(R.id.ptrlv_stocks);
        ll_filter_list = (LinearLayout) mView.findViewById(R.id.ll_filter_list);
        rg_category = (RadioGroup) mView.findViewById(R.id.rg_category);
        rg_sort = (RadioGroup) mView.findViewById(R.id.rg_sort);
        tv_bottom_info = (TextView) mView.findViewById(R.id.tv_bottom_info);
    }

    private void initData() {
        mBanners.add(new News());
        mBanners.add(new News());
        mBanners.add(new News());
        mBanners.add(new News());
        mBanners.add(new News());
    }

    private void test() {
        mStocks.add(new Stock());
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("苏宁云商", 0, 0));
        mStocks.add(new Stock("中国银行", 0, 0));
        mStocks.add(new Stock("中国银行", 0, 0));
        mStocks.add(new Stock("中国银行", 0, 0));
        mStocks.add(new Stock("中国银行", 0, 0));
        mStocks.add(new Stock("中国银行", 0, 0));
        mStockAdapter = new StockAdapter(getActivity(), mStocks, mBanners);
        ptrlv_stocks.setAdapter(mStockAdapter);
    }

    private void setAdapers() {
        mStockAdapter = new StockAdapter(getActivity(), mStocks, mBanners);
        ptrlv_stocks.setAdapter(mStockAdapter);
        mStockAdapter.addAll(mNewStocks);
    }

    private void setListeners() {
        mOnClickListener = new InnerOnClickListener();
        btn_title_bar_filter.setOnClickListener(mOnClickListener);
        btn_title_bar_search.setOnClickListener(mOnClickListener);
        btn_title_bar_cancel.setOnClickListener(mOnClickListener);
        btn_title_bar_confirm.setOnClickListener(mOnClickListener);
        ptrlv_stocks.setOnRefreshListener(new InnerOnRefreshListener());
        ptrlv_stocks.setOnItemClickListener(new InnerOnItemClickListener());
        ptrlv_stocks.setOnScrollListener(new MyOnScrollListener());
    }

    private void setLoadingLayoutProxy() {
        mLoadingLayoutProxy = ptrlv_stocks.getLoadingLayoutProxy();
        mLoadingLayoutProxy.setPullLabel("下拉刷新");
        mLoadingLayoutProxy.setReleaseLabel("松开可以刷新");
        mLoadingLayoutProxy.setRefreshingLabel("正在刷新...");
    }

    private void registerReceiver() {
        mMyReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Consts.ACTION_STATUS_CHANGE);
        filter.addAction(Consts.ACTION_UPDATE_USER_INFO);
        filter.addAction(Consts.ACTION_UPDATE_BANNERS);
        getActivity().registerReceiver(mMyReceiver, filter);
    }

    private void query() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mIsRunning = true;
                    while (mIsRunning) {
                        if (MyApplication.mStatus == Consts.LOGIN) {
                            User user = BmobUser.getCurrentUser(User.class);
                            BmobQuery<UserData> bmobQuery = new BmobQuery<>();
                            bmobQuery.addWhereEqualTo(Consts.KEY_USER, user);
                            bmobQuery.findObjects(new MyFindListener());
                            mIsRunning = false;
                        } else {
                            mRequestStockInfo.request(Consts.STOCKIDS);
                            mIsRunning = false;
                        }
                    }
                    sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showFilterList() {
        btn_title_bar_filter.setVisibility(View.GONE);
        btn_title_bar_search.setVisibility(View.GONE);
        btn_title_bar_cancel.setVisibility(View.VISIBLE);
        btn_title_bar_confirm.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.set_filter_list_enter);
        ll_filter_list.startAnimation(animation);
        ll_filter_list.setVisibility(View.VISIBLE);
    }

    private void hideFilterList() {
        btn_title_bar_cancel.setVisibility(View.GONE);
        btn_title_bar_confirm.setVisibility(View.GONE);
        btn_title_bar_filter.setVisibility(View.VISIBLE);
        btn_title_bar_search.setVisibility(View.VISIBLE);

        ll_filter_list.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.set_filter_list_exit);
        ll_filter_list.startAnimation(animation);
    }

    private void filter() {

        switch (rg_category.getCheckedRadioButtonId()) {
            case R.id.rb_all:
                mNewStocks.clear();
                mNewStocks.addAll(mStocks);
                break;
            case R.id.rb_sh:
                mNewStocks.clear();
                for (int i = 0; i < mStocks.size(); i++) {
                    Stock stock = mStocks.get(i);
                    String code = stock.getCode();
                    if ("sh".equals(code.substring(0, 2))) {
                        mNewStocks.add(stock);
                    }
                }
                break;
            case R.id.rb_sz:
                mNewStocks.clear();
                for (int i = 0; i < mStocks.size(); i++) {
                    Stock stock = mStocks.get(i);
                    String code = stock.getCode();
                    if ("sz".equals(code.substring(0, 2))) {
                        mNewStocks.add(stock);
                    }
                }
                break;
        }

        switch (rg_sort.getCheckedRadioButtonId()) {
            case R.id.rb_def:
                Collections.sort(mNewStocks, mDefComparator);
                break;
            case R.id.rb_current_price_asc:
                Collections.sort(mNewStocks, mPriceComparator);
                break;
            case R.id.rb_current_price_desc:
                Collections.sort(mNewStocks, mPriceComparator);
                break;
            case R.id.rb_increase_asc:
                Collections.sort(mNewStocks, mIncreaseComparator);
                break;
            case R.id.rb_increase_desc:
                Collections.sort(mNewStocks, mIncreaseComparator);
                break;
        }

        mStockAdapter.clear();
        mStockAdapter.addAll(mNewStocks);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ll_filter_list.getVisibility() == View.VISIBLE) {
                hideFilterList();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mMyReceiver);
        super.onDestroy();
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Consts.HANDLER_REQUEST_STOCK_INFO_FAIL:
                    ptrlv_stocks.onRefreshComplete();
                    ToastUtil.toast(getActivity(), "刷新失败");
                    break;
                case Consts.HANDLER_USER_NO_DATA:
                    ptrlv_stocks.onRefreshComplete();
                    tv_bottom_info.setVisibility(View.VISIBLE);
                    tv_bottom_info.setText("该用户没有收藏任何股票");
                    mStocks.clear();
                    filter();
                    break;
                case Consts.HANDLER_REQUEST_STOCK_INFO_SUCCESS:
                    mEndTime = System.currentTimeMillis();
                    mStocks = (List<Stock>) msg.obj;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                long time = mEndTime - mStartTime;
                                if (time < Consts.LOADING_TIME) {
                                    sleep(Consts.LOADING_TIME - time);
                                }
                                sendEmptyMessage(Consts.HANDLER_REFRESH_STOCK_LISTVIEW);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                case Consts.HANDLER_REFRESH_STOCK_LISTVIEW:
                    if (MyApplication.mStatus == Consts.LOGIN) {
                        tv_bottom_info.setVisibility(View.GONE);
                    } else {
                        tv_bottom_info.setVisibility(View.VISIBLE);
                        tv_bottom_info.setText("当前没有登陆, 默认显示三支股票, 请登录后收藏更多股票");
                    }
                    mLoadingLayoutProxy.setLastUpdatedLabel("更新于: " + FormatUitl.formatTime(mEndTime));
                    ptrlv_stocks.onRefreshComplete();
                    filter();
                    break;
            }
        }
    }

    private class InnerOnRefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            mStartTime = System.currentTimeMillis();

            query();
        }
    }

    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0 && position != 1) {
                Stock stock = mNewStocks.get(position - 2);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Consts.KEY_STOCK, stock);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
            }
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_title_bar_filter:
                    showFilterList();
                    LogUtil.log("showFilterList() -> " + ll_filter_list);
                    break;
                case R.id.btn_title_bar_search:
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_title_bar_cancel:
                    hideFilterList();
                    break;
                case R.id.btn_title_bar_confirm:
                    LogUtil.log("hideFilterList() -> " + ll_filter_list);
                    hideFilterList();
                    filter();
                    break;
            }
        }
    }

    private class DefComparator implements Comparator<Stock> {
        @Override
        public int compare(Stock lhs, Stock rhs) {
            return lhs.getCode().compareTo(rhs.getCode());
        }
    }

    private class PriceComparator implements Comparator<Stock> {
        @Override
        public int compare(Stock lhs, Stock rhs) {
            double price = lhs.getCurrentPrice() - rhs.getCurrentPrice();
            if (rg_sort.getCheckedRadioButtonId() == R.id.rb_current_price_asc) {
                return price < 0 ? -1 : 1;
            } else {
                return price < 0 ? 1 : -1;
            }
        }
    }

    private class IncreaseComparator implements Comparator<Stock> {
        @Override
        public int compare(Stock lhs, Stock rhs) {
            double increase = lhs.getIncrease() - rhs.getIncrease();
            if (rg_sort.getCheckedRadioButtonId() == R.id.rb_increase_asc) {
                return increase < 0 ? -1 : 1;
            } else {
                return increase < 0 ? 1 : -1;
            }
        }
    }

    private class MyFindListener extends FindListener<UserData> {
        @Override
        public void done(List<UserData> list, BmobException e) {
            mStockIDSB.delete(0, mStockIDSB.length());
            if (e == null) {
                mRequestStockInfo.request(list);
            } else {
                e.printStackTrace();
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Consts.ACTION_STATUS_CHANGE.equals(action) || Consts.ACTION_UPDATE_USER_INFO.equals(action)) {
                ptrlv_stocks.setRefreshing(true);
            }
            if (Consts.ACTION_UPDATE_BANNERS.equals(action)) {
                List<News> banners = intent.getParcelableArrayListExtra(Consts.KEY_BANNERS);
                mBanners.clear();
                mBanners.addAll(banners);
                mStockAdapter.notifyDataSetChanged();
                mStockAdapter.updateBanners();
            }
        }
    }

    private class MyOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem >= 2) {
                rl_stock_head.setVisibility(View.VISIBLE);
            } else {
                rl_stock_head.setVisibility(View.GONE);
            }
        }
    }
}
