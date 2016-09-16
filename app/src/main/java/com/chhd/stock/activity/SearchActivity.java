package com.chhd.stock.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;

import com.chhd.stock.R;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.http.RequestStockInfo;
import com.chhd.stock.util.Consts;
import com.chhd.stock.view.InputView;
import com.chhd.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private InputView iv_stock;
    private Button btn_confirm;
    private Button btn_back;

    private Handler mHandler = new MyHandler();
    private List<Stock> mStocks = new ArrayList<>();
    private AlertDialog mAlertDialog;
    private long mStartTime;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

        createDialog();

        setListeners();
    }

    private void initViews() {
        iv_stock = (InputView) findViewById(R.id.iv_stock);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_back = (Button) findViewById(R.id.btn_back);
    }

    private void setListeners() {
        View.OnClickListener onClickListener = new MyOnClickListener();
        btn_confirm.setOnClickListener(onClickListener);
        btn_back.setOnClickListener(onClickListener);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_progress, null);
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(false);
    }

    private void jumpDetailActivity() {
        mAlertDialog.dismiss();
        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
        intent.putExtra(Consts.KEY_STOCK, mStocks.get(0));
        startActivity(intent);
        overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
        finish();
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    String stockID = iv_stock.getText();
                    if (!stockID.matches("s[hz]\\d{6}")) {
                        Animation animation = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.set_shake);
//                CycleInterpolator
                        animation.setInterpolator(new MyInterpolator());
                        iv_stock.startAnimation(animation);

                        ToastUtil.toast(SearchActivity.this, "格式不正确");
                        return;
                    }
                    mStartTime = System.currentTimeMillis();
                    mAlertDialog.show();
                    RequestStockInfo requestStockInfo = new RequestStockInfo(mHandler);
                    requestStockInfo.request(stockID);
                    break;
                case R.id.btn_back:
                    finish();
                    break;
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Consts.HANDLER_REQUEST_STOCK_INFO_FAIL:
                    mAlertDialog.dismiss();
                    ToastUtil.toast(SearchActivity.this, "搜索失败");
                    break;
                case Consts.HANDLER_REQUEST_STOCK_INFO_SUCCESS:
                    mStocks = (List<Stock>) msg.obj;
                    mEndTime = System.currentTimeMillis();
                    long time = mEndTime - mStartTime;
                    if (time < Consts.LOADING_TIME) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                jumpDetailActivity();
                            }
                        }, Consts.LOADING_TIME - time);
                    } else {
                        jumpDetailActivity();
                    }
                    break;
            }
        }
    }

    private class MyInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(2 * 7 * Math.PI * input));
        }
    }
}
