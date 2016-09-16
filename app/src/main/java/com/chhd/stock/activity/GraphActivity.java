package com.chhd.stock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chhd.stock.R;
import com.chhd.stock.adapter.GraphLargePagerAdapter;
import com.chhd.stock.util.Consts;
import com.chhd.stock.view.ZoomOutPageTransformer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private ViewPager vp_graphs;
    private TextView tv_current_index;

    private List<String> mUrls;
    private int mCurrentIndex;
    private List<GestureImageView> mGestureImageViews = new ArrayList<>();
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        initViews();

        initDisplayImageOptions();

        initData();

        setAdapter();

        setListeners();
    }

    private void initViews() {
        vp_graphs = (ViewPager) findViewById(R.id.vp_graphs);
        tv_current_index = (TextView) findViewById(R.id.tv_current_index);
    }

    private void initDisplayImageOptions() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(R.mipmap.graph_default_pic);
        builder.showImageOnLoading(R.mipmap.graph_loading_pic);
        builder.showImageOnFail(R.mipmap.graph_default_pic);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        mOptions = builder.build();
    }

    private void initData() {
        Intent intent = getIntent();
        mUrls = (List<String>) intent.getSerializableExtra(Consts.KEY_URLS);
        mCurrentIndex = intent.getIntExtra(Consts.KEY_CURRENT_INDEX, -1);

        for (int i = 0; i < mUrls.size(); i++) {
            GestureImageView gestureImageView = new GestureImageView(this);

            mImageLoader.displayImage(mUrls.get(i), gestureImageView, mOptions);

            mGestureImageViews.add(gestureImageView);
        }
    }

    private void setAdapter() {
        GraphLargePagerAdapter adapter = new GraphLargePagerAdapter(mGestureImageViews);
        vp_graphs.setAdapter(adapter);
        vp_graphs.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setListeners() {
        vp_graphs.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tv_current_index.setText("1 / 4");
                    break;
                case 1:
                    tv_current_index.setText("2 / 4");
                    break;
                case 2:
                    tv_current_index.setText("3 / 4");
                    break;
                case 3:
                    tv_current_index.setText("4 / 4");
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
