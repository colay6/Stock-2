package com.chhd.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.chhd.stock.R;
import com.chhd.stock.adapter.DetailAdapter;
import com.chhd.stock.app.MyApplication;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.entity.User;
import com.chhd.stock.entity.UserData;
import com.chhd.stock.util.Consts;
import com.chhd.util.ToastUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends BaseActivity {


    private ImageButton ib_detail_title_bar_back;
    private Button btn_detail_title_bar_collection;
    private Button btn_detail_title_bar_cancel;
    private PullToRefreshListView ptrlv_stock_detail;

    private List<Stock> mStocks = new ArrayList<>();
    private Stock mStock;
    private View.OnClickListener mOnClickListener;
    private UserData mUserData = new UserData();
    private Handler mHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();

        initData();

        setAdapter();

        setListeners();

        setLoadingLayoutProxy();

        query();

    }

    private void initViews() {
        ib_detail_title_bar_back = (ImageButton) findViewById(R.id.ib_detail_title_bar_back);
        btn_detail_title_bar_collection = (Button) findViewById(R.id.btn_detail_title_bar_collection);
        btn_detail_title_bar_cancel = (Button) findViewById(R.id.btn_detail_title_bar_cancel);
        ptrlv_stock_detail = (PullToRefreshListView) findViewById(R.id.ptrlv_stock_detail);
    }

    private void initData() {
        Intent intent = getIntent();
        mStock = intent.getParcelableExtra(Consts.KEY_STOCK);
    }

    private void setAdapter() {
        mStocks.add(mStock);
        DetailAdapter detailAdapter = new DetailAdapter(this, mStocks);
        ptrlv_stock_detail.setAdapter(detailAdapter);
    }

    private void setListeners() {
        mOnClickListener = new InnerOnClickListener();
        ib_detail_title_bar_back.setOnClickListener(mOnClickListener);
        btn_detail_title_bar_collection.setOnClickListener(mOnClickListener);
        btn_detail_title_bar_cancel.setOnClickListener(mOnClickListener);
        ptrlv_stock_detail.setOnRefreshListener(new InnerOnRefreshListener());
    }


    private void setLoadingLayoutProxy() {
        ILoadingLayout loadingLayoutProxy = ptrlv_stock_detail.getLoadingLayoutProxy();
        loadingLayoutProxy.setPullLabel("");
        loadingLayoutProxy.setReleaseLabel("");
        loadingLayoutProxy.setRefreshingLabel("");
    }

    private void query() {
        if (MyApplication.mStatus == Consts.LOGIN) {
            User User = BmobUser.getCurrentUser(User.class);
            BmobQuery<UserData> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo(Consts.KEY_USER, User);
            bmobQuery.addWhereEqualTo(Consts.KEY_STOCK_ID, mStock.getCode());
            bmobQuery.findObjects(new MyFindListener());
        } else {
            btn_detail_title_bar_collection.setVisibility(View.VISIBLE);
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_detail_title_bar_back:
                    finish();
                    overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
                    break;
                case R.id.btn_detail_title_bar_collection:
                    if (!TextUtils.isEmpty(MyApplication.mUsername)) {
                        User User = BmobUser.getCurrentUser(User.class);
                        UserData userDate = new UserData();
                        userDate.setUser(User);
                        userDate.setStockID(mStock.getCode());
                        userDate.save(new MySaveListener());
                    } else {
                        ToastUtil.toast(DetailActivity.this, "请登录");
                    }
                    break;
                case R.id.btn_detail_title_bar_cancel:
                    UserData userData = new UserData();
                    userData.delete(mUserData.getObjectId(), new MyUpdateListener());
                    break;
            }
        }
    }

    private class InnerOnRefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            mHandler.obtainMessage().sendToTarget();
        }
    }

    private class MySaveListener extends SaveListener<String> {

        @Override
        public void done(String s, BmobException e) {
            if (e == null) {
                ToastUtil.toast(DetailActivity.this, "收藏成功");
                mUserData.setObjectId(s);
                btn_detail_title_bar_collection.setVisibility(View.GONE);
                btn_detail_title_bar_cancel.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Consts.ACTION_UPDATE_USER_INFO);
                sendBroadcast(intent);
            } else {
                ToastUtil.toast(DetailActivity.this, "收藏失败");
            }
        }
    }

    private class MyFindListener extends FindListener<UserData> {
        @Override
        public void done(List<UserData> list, BmobException e) {

            if (e == null) {
                mUserData = list.get(0);
                btn_detail_title_bar_cancel.setVisibility(View.VISIBLE);
            } else {
                btn_detail_title_bar_collection.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }

    private class MyUpdateListener extends UpdateListener {
        @Override
        public void done(BmobException e) {
            if (e == null) {
                ToastUtil.toast(DetailActivity.this, "取消成功");
                btn_detail_title_bar_cancel.setVisibility(View.GONE);
                btn_detail_title_bar_collection.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Consts.ACTION_UPDATE_USER_INFO);
                sendBroadcast(intent);
            } else {
                ToastUtil.toast(DetailActivity.this, "取消失败");
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ptrlv_stock_detail.onRefreshComplete();
        }
    }
}
