package com.chhd.stock.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chhd.stock.activity.GraphActivity;
import com.chhd.stock.util.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/7/14.
 */
public class GraphPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ImageView> mImageViews;
    private List<String> mUrls;

    public GraphPagerAdapter(Context context, List<ImageView> imageViews, List<String> urls) {
        mContext = context;
        mImageViews = imageViews;
        mUrls = urls;
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImageViews.get(position);

        imageView.setOnClickListener(new MyOnClickListener(position));

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }

    private class MyOnClickListener implements View.OnClickListener {

        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, GraphActivity.class);
            intent.putExtra(Consts.KEY_URLS, (ArrayList) mUrls);
            intent.putExtra(Consts.KEY_CURRENT_INDEX, position);
            mContext.startActivity(intent);
        }
    }
}
