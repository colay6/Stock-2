package com.chhd.stock.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.adapter.MyBaseAdapter;
import com.chhd.stock.R;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.view.ZoomOutPageTransformer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/7/14.
 */
public class DetailAdapter extends MyBaseAdapter<Stock> {

    private Context mContext;
    private Stock mStock;
    private List<String> mGraphTitle;
    private List<String> mUrls;
    private List<ImageView> mImageViews;
    private List<View> mPoints;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions.Builder mBuilder = new DisplayImageOptions.Builder();
    private DisplayImageOptions mDisplayImageOptions;
    private int mCurrentGraphIndex;
    private int mLastGraphIndex;
    private ViewHolder mViewHolder;

    public DetailAdapter(Context context, List<Stock> data) {
        super(context, data);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mStock = getItem(position);
        mViewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.listview_item_detail, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_item_stock_name = (TextView) convertView.findViewById(R.id.tv_item_stock_name);
            mViewHolder.tv_item_stock_code = (TextView) convertView.findViewById(R.id.tv_item_stock_code);
            mViewHolder.tv_item_stock_openning_price = (TextView) convertView.findViewById(R.id.tv_item_stock_openning_price);
            mViewHolder.tv_item_stock_closeing_price = (TextView) convertView.findViewById(R.id.tv_item_stock_closeing_price);
            mViewHolder.tv_item_stock_current_price = (TextView) convertView.findViewById(R.id.tv_item_stock_current_price);
            mViewHolder.tv_item_stock_increase = (TextView) convertView.findViewById(R.id.tv_item_stock_increase);
            mViewHolder.tv_item_stock_toal_number = (TextView) convertView.findViewById(R.id.tv_item_stock_toal_number);
            mViewHolder.tv_item_stock_toal_over = (TextView) convertView.findViewById(R.id.tv_item_stock_toal_over);
            mViewHolder.tv_item_stock_date = (TextView) convertView.findViewById(R.id.tv_item_stock_date);
            mViewHolder.tv_item_stock_time = (TextView) convertView.findViewById(R.id.tv_item_stock_time);
            mViewHolder.tv_item_graph_title = (TextView) convertView.findViewById(R.id.tv_item_graph_title);
            mViewHolder.vp_graphs = (ViewPager) convertView.findViewById(R.id.vp_graphs);
            mViewHolder.shap_indication_point_1 = (View) convertView.findViewById(R.id.shap_indication_point_1);
            mViewHolder.shap_indication_point_2 = (View) convertView.findViewById(R.id.shap_indication_point_2);
            mViewHolder.shap_indication_point_3 = (View) convertView.findViewById(R.id.shap_indication_point_3);
            mViewHolder.shap_indication_point_4 = (View) convertView.findViewById(R.id.shap_indication_point_4);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        double increase = mStock.getIncrease();
        BigDecimal bigDecimal = new BigDecimal(increase);
        String increaseStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        double totalNumber = mStock.getTotalNumber();
        bigDecimal = new BigDecimal(totalNumber / 100000000);
        String totalNumberStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        double turnover = mStock.getTurnover();
        bigDecimal = new BigDecimal(turnover / 100000000);
        String turnoverStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        mViewHolder.tv_item_stock_name.setText(mStock.getName());
        mViewHolder.tv_item_stock_code.setText(mStock.getCode());
        mViewHolder.tv_item_stock_openning_price.setText("" + mStock.getOpenningPrice());
        mViewHolder.tv_item_stock_closeing_price.setText("" + mStock.getClosingPrice());
        mViewHolder.tv_item_stock_current_price.setText("" + mStock.getCurrentPrice());
        mViewHolder.tv_item_stock_increase.setText(increaseStr);
        mViewHolder.tv_item_stock_toal_number.setText(totalNumberStr);
        mViewHolder.tv_item_stock_toal_over.setText(turnoverStr);
        mViewHolder.tv_item_stock_date.setText(mStock.getDate());
        mViewHolder.tv_item_stock_time.setText(mStock.getTime());

        initData();

        setDisplayImageOptions();

        displayImages();

        setAdapter();

        setListeners();

        return convertView;
    }

    private void initData() {
        mGraphTitle = new ArrayList<>();
        mGraphTitle.add("分K线图");
        mGraphTitle.add("日K线图");
        mGraphTitle.add("周K线图");
        mGraphTitle.add("月K线图");

        mUrls = new ArrayList<>();
        mUrls.add(mStock.getMinurl());
        mUrls.add(mStock.getDayurl());
        mUrls.add(mStock.getWeekurl());
        mUrls.add(mStock.getMonthurl());

        mPoints = new ArrayList<>();
        mPoints.add(mViewHolder.shap_indication_point_1);
        mPoints.add(mViewHolder.shap_indication_point_2);
        mPoints.add(mViewHolder.shap_indication_point_3);
        mPoints.add(mViewHolder.shap_indication_point_4);
    }


    private void setDisplayImageOptions() {
        mBuilder.showImageOnLoading(R.mipmap.graph_loading_pic);
        mBuilder.showImageForEmptyUri(R.mipmap.graph_default_pic);
        mBuilder.showImageOnFail(R.mipmap.graph_default_pic);
        mBuilder.cacheInMemory(true);
        mBuilder.cacheOnDisk(true);
        mDisplayImageOptions = mBuilder.build();
    }

    private void displayImages() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(mContext);

            mImageLoader.displayImage(mUrls.get(i), imageView, mDisplayImageOptions);

            mImageViews.add(imageView);
        }
    }

    private void setAdapter() {
        GraphPagerAdapter graphPagerAdapter = new GraphPagerAdapter(mContext, mImageViews, mUrls);
        mViewHolder.vp_graphs.setAdapter(graphPagerAdapter);
        mViewHolder.vp_graphs.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setListeners() {
        mViewHolder.vp_graphs.addOnPageChangeListener(new InnerOnPageChangeListener());
    }

    class ViewHolder {
        TextView tv_item_stock_name;
        TextView tv_item_stock_code;
        TextView tv_item_stock_openning_price;
        TextView tv_item_stock_closeing_price;
        TextView tv_item_stock_current_price;
        TextView tv_item_stock_increase;
        TextView tv_item_stock_toal_number;
        TextView tv_item_stock_toal_number_unit;
        TextView tv_item_stock_toal_over;
        TextView tv_item_stock_toal_over_unit;
        TextView tv_item_stock_date;
        TextView tv_item_stock_time;
        TextView tv_item_graph_title;
        ViewPager vp_graphs;
        View shap_indication_point_1;
        View shap_indication_point_2;
        View shap_indication_point_3;
        View shap_indication_point_4;
    }

    private class InnerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mViewHolder.tv_item_graph_title.setText(mGraphTitle.get(position));
            mCurrentGraphIndex = position;
            mPoints.get(mLastGraphIndex).setBackgroundResource(R.drawable.shap_indication_point_normal);
            mPoints.get(mCurrentGraphIndex).setBackgroundResource(R.drawable.shap_indication_point_checked);
            mLastGraphIndex = mCurrentGraphIndex;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
