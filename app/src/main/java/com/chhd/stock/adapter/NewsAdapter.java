package com.chhd.stock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.adapter.MyBaseAdapter;
import com.chhd.stock.R;
import com.chhd.stock.entity.News;
import com.chhd.stock.util.FormatUitl;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by CWQ on 2016/7/28.
 */
public class NewsAdapter extends MyBaseAdapter<News> {

    private Context mContext;
    private Animation mAnimation;

    public NewsAdapter(Context context, List<News> data) {
        super(context, data);
        mContext = context;
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.set_img);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.listview_item_news, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
            viewHolder.tv_item_author = (TextView) convertView.findViewById(R.id.tv_item_author);
            viewHolder.tv_item_time = (TextView) convertView.findViewById(R.id.tv_item_time);
            viewHolder.iv_item_img = (ImageView) convertView.findViewById(R.id.iv_item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        News news = getItem(position);
        viewHolder.tv_item_title.setText(news.getTitle());
        viewHolder.tv_item_author.setText(news.getAuthor());
        viewHolder.tv_item_time.setText(FormatUitl.formatTime(news.getTime() * 1000L));
        String img = news.getImg();
        BitmapUtils bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.news_loading_pic);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.news_default_pic);
        bitmapUtils.configDefaultImageLoadAnimation(mAnimation);
        bitmapUtils.display(viewHolder.iv_item_img, img);
        return convertView;
    }

    class ViewHolder {
        TextView tv_item_title;
        TextView tv_item_author;
        TextView tv_item_time;
        ImageView iv_item_img;
    }
}
