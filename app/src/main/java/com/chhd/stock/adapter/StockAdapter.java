package com.chhd.stock.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.adapter.MyBaseAdapter;
import com.chhd.stock.R;
import com.chhd.stock.entity.News;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.util.Consts;
import com.chhd.stock.view.ZoomOutPageTransformer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by CWQ on 2016/7/10.
 */
public class StockAdapter extends MyBaseAdapter<Stock> {

    private ViewPager vp_banner_container;
    private TextView tv_banner_title;
    private View shap_indication_point_1;
    private View shap_indication_point_2;
    private View shap_indication_point_3;
    private View shap_indication_point_4;
    private View shap_indication_point_5;
    private View mView;

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions.Builder mBuilder = new DisplayImageOptions.Builder();
    private DisplayImageOptions mDisplayImageOptions;
    private List<News> mBanners = new ArrayList<>();
    private List<ImageView> mImageViews = new ArrayList<>();
    private List<View> mPoints = new ArrayList<>();
    private int mCurrentBannerIndex;
    private int mLastBannerIndex;
    private Handler mHandler = new InnerHandler();
    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private AutoScrollTask mAutoScrollTask = new AutoScrollTask();

    private Context mContext;
    private List<Stock> mStocks;
    private int mType;

    public StockAdapter(Context context, List<Stock> stocks, List<News> banners) {
        super(context, stocks);
        this.mContext = context;
        this.mStocks = stocks;
        this.mBanners = banners;
        initHead();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        mType = position == 0 ? Consts.LISTVIEW_ITEM_HEAD : Consts.LISTVIEW_ITEM_STOCK;
        if (convertView == null || ((ViewHolder) convertView.getTag()).type != mType) {
            if (position == 0) {
                convertView = mView;
                viewHolder = new ViewHolder();
                viewHolder.type = Consts.LISTVIEW_ITEM_HEAD;
                convertView.setTag(viewHolder);
            } else {
                convertView = getLayoutInflater().inflate(R.layout.listview_item_stock, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_stock_name = (TextView) convertView.findViewById(R.id.tv_item_stock_name);
                viewHolder.tv_item_stock_current_price = (TextView) convertView.findViewById(R.id.tv_item_stock_current_price);
                viewHolder.tv_item_stock_increase = (TextView) convertView.findViewById(R.id.tv_item_stock_increase);
                viewHolder.type = Consts.LISTVIEW_ITEM_STOCK;
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position != 0) {
            Stock stock = getItem(position);
            double increase = stock.getIncrease();
            BigDecimal bigDecimal = new BigDecimal(increase);
            String increaseStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

            viewHolder.tv_item_stock_name.setText(stock.getName());
            viewHolder.tv_item_stock_current_price.setText("" + stock.getCurrentPrice());
            if (Double.parseDouble(increaseStr) < 0) {
                viewHolder.tv_item_stock_increase.setBackgroundResource(R.mipmap.btn_stock_fall);
            }
            viewHolder.tv_item_stock_increase.setText(increaseStr);
        }

        return convertView;
    }

    private void initHead() {
        mView = getLayoutInflater().inflate(R.layout.listview_item_head, null);

        initViews();

        setDisplayImageOptions();

        initData();

        setAdapter();

        setListeners();

        autoScrollBaner();
    }

    private void initViews() {
        vp_banner_container = (ViewPager) mView.findViewById(R.id.vp_banner_container);
        tv_banner_title = (TextView) mView.findViewById(R.id.tv_banner_title);
        shap_indication_point_1 = (View) mView.findViewById(R.id.shap_indication_point_1);
        shap_indication_point_2 = (View) mView.findViewById(R.id.shap_indication_point_2);
        shap_indication_point_3 = (View) mView.findViewById(R.id.shap_indication_point_3);
        shap_indication_point_4 = (View) mView.findViewById(R.id.shap_indication_point_4);
        shap_indication_point_5 = (View) mView.findViewById(R.id.shap_indication_point_5);
    }

    private void setDisplayImageOptions() {
        mBuilder.showImageOnLoading(R.mipmap.banner_loading_pic);
        mBuilder.showImageForEmptyUri(R.mipmap.banner_default_pic);
        mBuilder.showImageOnFail(R.mipmap.banner_default_pic);
        mBuilder.cacheInMemory(true);
        mBuilder.cacheOnDisk(true);
        mBuilder.imageScaleType(ImageScaleType.EXACTLY);
        mDisplayImageOptions = mBuilder.build();
    }

    private void initData() {
        mImageViews.clear();
        mPoints.clear();
        mCurrentBannerIndex = 0;
        mLastBannerIndex = 0;


        for (int i = 0; i < mBanners.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.banner_default_pic);
            mImageViews.add(imageView);

            mPoints.add(shap_indication_point_1);
            mPoints.add(shap_indication_point_2);
            mPoints.add(shap_indication_point_3);
            mPoints.add(shap_indication_point_4);
            mPoints.add(shap_indication_point_5);
        }

        tv_banner_title.setText("banner 1");
    }

    private void setAdapter() {
        vp_banner_container.setAdapter(new BannerPagerAdapter(mContext, mImageViews, mBanners));
        vp_banner_container.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setListeners() {
        vp_banner_container.addOnPageChangeListener(new InnerOnPageChangeListener());
    }

    private void autoScrollBaner() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleAtFixedRate(mAutoScrollTask, 0, 5, TimeUnit.SECONDS);
    }

    public void addAll(List<Stock> stocks) {
        mStocks.add(new Stock());
        mStocks.addAll(stocks);
        notifyDataSetChanged();
    }

    public void clear() {
        mStocks.clear();
        notifyDataSetChanged();
    }

    public void updateBanners() {
        for (int i = 0; i < mBanners.size(); i++) {
            mImageLoader.displayImage(mBanners.get(i).getImg(), mImageViews.get(i), mDisplayImageOptions);
            tv_banner_title.setText(mBanners.get(mCurrentBannerIndex).getTitle());
        }

    }

    private class AutoScrollTask implements Runnable {

        @Override
        public void run() {
            mCurrentBannerIndex = (mCurrentBannerIndex + 1) % mImageViews.size();
            mHandler.sendEmptyMessage(Consts.HANDLER_UPDATE_CURRENT_BANNER_INDEX);
        }
    }

    private class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Consts.HANDLER_UPDATE_CURRENT_BANNER_INDEX:
                    vp_banner_container.setCurrentItem(mCurrentBannerIndex);
                    break;
            }
        }
    }

    private class InnerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentBannerIndex = position;
            String title = mBanners.get(position).getTitle();
            if (TextUtils.isEmpty(title)) {
                title = "banner " + position;
            }
            tv_banner_title.setText(title);
            mPoints.get(mLastBannerIndex).setBackgroundResource(R.drawable.shap_indication_point_normal);
            mPoints.get(mCurrentBannerIndex).setBackgroundResource(R.drawable.shap_indication_point_checked);
            mLastBannerIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class ViewHolder {
        TextView tv_item_stock_name;
        TextView tv_item_stock_current_price;
        TextView tv_item_stock_increase;
        int type;
    }
}
