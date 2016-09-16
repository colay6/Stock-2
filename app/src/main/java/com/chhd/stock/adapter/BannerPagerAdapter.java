package com.chhd.stock.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chhd.stock.R;
import com.chhd.stock.activity.NewsActivity;
import com.chhd.stock.entity.News;
import com.chhd.stock.util.Consts;

import java.util.List;

/**
 * Created by CWQ on 2016/7/10.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private Context context;
    private List<ImageView> imageViews;
    private List<News> banners;

    public BannerPagerAdapter(Context context, List<ImageView> imageViews, List<News> banners) {
        this.context = context;
        this.imageViews = imageViews;
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        News news = banners.get(position);
        imageView.setOnClickListener(new InnerOnClickListener(news));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    private class InnerOnClickListener implements View.OnClickListener {

        private News news;

        public InnerOnClickListener(News news) {
            this.news = news;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra(Consts.KEY_NEWS, news);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
        }
    }

}
