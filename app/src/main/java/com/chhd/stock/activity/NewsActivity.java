package com.chhd.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.chhd.stock.R;
import com.chhd.stock.entity.News;
import com.chhd.stock.util.Consts;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsActivity extends BaseActivity {

    private ImageButton ib_back;
    private Button btn_share;
    private WebView wv_news;

    private News mNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initViews();

        initData();

        initWebView();

        setListeners();
    }

    private void initViews() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        btn_share = (Button) findViewById(R.id.btn_share);
        wv_news = (WebView) findViewById(R.id.wv_news);
    }

    private void initData() {
        Intent intent = getIntent();
        mNews = intent.getParcelableExtra(Consts.KEY_NEWS);
    }

    private void initWebView() {
        wv_news.loadUrl(mNews.getUrl());
        wv_news.setWebViewClient(new MyWebViewClient());
        WebSettings settings = wv_news.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void setListeners() {
        View.OnClickListener onClickListener = new MyOnClickListener();
        ib_back.setOnClickListener(onClickListener);
        btn_share.setOnClickListener(onClickListener);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setText(mNews.getTitle() + "\n" + mNews.getUrl());
        oks.show(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv_news.canGoBack()) {
                wv_news.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_back:
                    finish();
                    overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
                    break;
                case R.id.btn_share:
//                    ToastUtil.toast(NewsActivity.this, "分享功能尚未开通");
                    showShare();
                    break;
            }
        }
    }
}
