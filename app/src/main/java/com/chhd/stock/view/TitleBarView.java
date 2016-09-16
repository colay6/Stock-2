package com.chhd.stock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chhd.stock.R;
import com.chhd.stock.util.Consts;

/**
 * Created by CWQ on 2016/7/16.
 */
public class TitleBarView extends LinearLayout {

    private View mView;
    private TextView tv_title_bar_title;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mView = View.inflate(context, R.layout.view_title_bar, this);

        initViews();

        String title = attrs.getAttributeValue(Consts.NAMESPACE, "title_bar_title");
        tv_title_bar_title.setText(title);
    }

    private void initViews() {
        tv_title_bar_title = (TextView) mView.findViewById(R.id.tv_title_bar_title);
    }

}
