package com.chhd.stock.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.polites.android.GestureImageView;

import java.util.List;

/**
 * Created by CWQ on 2016/7/30.
 */
public class GraphLargePagerAdapter extends PagerAdapter {

    private List<GestureImageView> mGestureImageViews;

    public GraphLargePagerAdapter(List<GestureImageView> GestureImageViews) {
        mGestureImageViews = GestureImageViews;
    }

    @Override
    public int getCount() {
        return mGestureImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GestureImageView gestureImageView = mGestureImageViews.get(position);
        container.addView(gestureImageView);
        return gestureImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mGestureImageViews.get(position));
    }
}
