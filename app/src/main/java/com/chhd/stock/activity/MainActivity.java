package com.chhd.stock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.chhd.adapter.MyFragmentPagerAdapter;
import com.chhd.stock.R;
import com.chhd.stock.app.MyApplication;
import com.chhd.stock.fragment.HelpFragment;
import com.chhd.stock.fragment.MeFragment;
import com.chhd.stock.fragment.NewsFragment;
import com.chhd.stock.fragment.StockFragment;
import com.chhd.stock.util.Consts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager vpContainer;
    private RadioGroup rgMainBottom;
    private ImageView iv_tab;

    private List<Fragment> fragments;
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private StockFragment mStockFragment;
    private int mCurrentPosition;
    private int mTabWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initData();

        setAdapter();

        rgMainBottom.check(R.id.rb_stock);

        setListeners();

        initTab();
    }

    private void initViews() {
        vpContainer = (ViewPager) findViewById(R.id.vp_container);
        rgMainBottom = (RadioGroup) findViewById(R.id.rg_main_bottom);
        iv_tab = (ImageView) findViewById(R.id.iv_tab);
    }

    private void initData() {
        mStockFragment = new StockFragment();

        fragments = new ArrayList<>();
        fragments.add(mStockFragment);
        fragments.add(new NewsFragment());
        fragments.add(new MeFragment());
        fragments.add(new HelpFragment());
    }

    private void setAdapter() {
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        vpContainer.setAdapter(mFragmentPagerAdapter);
        vpContainer.setOffscreenPageLimit(3);
    }

    private void setListeners() {
        vpContainer.addOnPageChangeListener(new InnerOnPageChangeListener());
        rgMainBottom.setOnCheckedChangeListener(new InnerOnCheckedChangeListener());
    }

    private void initTab() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        mTabWidth = widthPixels / 4;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_tab.getLayoutParams();
        layoutParams.width = mTabWidth;
        iv_tab.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mStockFragment instanceof StockFragment) {
                if (mStockFragment.onKeyDown(keyCode, event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.mStatus = Consts.LOGOUT;
    }

    private class InnerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_tab.getLayoutParams();
            layoutParams.leftMargin = (int) (mTabWidth * positionOffset + position * mTabWidth);
            iv_tab.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position;
            switch (position) {
                case 0:
                    rgMainBottom.check(R.id.rb_stock);
                    break;
                case 1:
                    rgMainBottom.check(R.id.rb_news);
                    break;
                case 2:
                    rgMainBottom.check(R.id.rb_me);
                    break;
                case 3:
                    rgMainBottom.check(R.id.rb_help);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class InnerOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_stock:
                    vpContainer.setCurrentItem(0
                    );
                    break;
                case R.id.rb_news:
                    vpContainer.setCurrentItem(1);
                    break;
                case R.id.rb_me:
                    vpContainer.setCurrentItem(2);
                    break;
                case R.id.rb_help:
                    vpContainer.setCurrentItem(3);
                    break;
            }

        }
    }
}
