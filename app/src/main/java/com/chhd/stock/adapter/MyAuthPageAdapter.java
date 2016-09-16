package com.chhd.stock.adapter;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by CWQ on 2016/8/16.
 */
public class MyAuthPageAdapter extends AuthorizeAdapter {

    @Override
    public void onCreate() {
        super.onCreate();
        hideShareSDKLogo();
    }
}
