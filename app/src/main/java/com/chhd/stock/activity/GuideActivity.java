package com.chhd.stock.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chhd.adapter.MyFragmentPagerAdapter;
import com.chhd.stock.R;
import com.chhd.stock.fragment.Guid1Fragment;
import com.chhd.stock.fragment.Guid2Fragment;
import com.chhd.stock.fragment.Guid3Fragment;
import com.chhd.stock.fragment.Guid4Fragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vpGuides;
    private List<Fragment> mFragments;
    private CirclePageIndicator cpi_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();

        initData();

        setAdapter(mFragments);

        cpi_indicator.setViewPager(vpGuides);
    }

    private void initViews() {
        vpGuides = (ViewPager) findViewById(R.id.vp_guids);
        cpi_indicator = (CirclePageIndicator) findViewById(R.id.cpi_indicator);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new Guid1Fragment());
        mFragments.add(new Guid2Fragment());
        mFragments.add(new Guid3Fragment());
        mFragments.add(new Guid4Fragment());
    }

    private void setAdapter(List<Fragment> fragments) {
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        vpGuides.setAdapter(myFragmentPagerAdapter);
    }
}
