package com.chhd.stock.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.chhd.stock.entity.User;
import com.chhd.stock.util.Consts;
import com.chhd.stock.util.SharedPreferencesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by CWQ on 2016/7/10.
 */
public class MyApplication extends Application {

    public static String mUsername = "";
    public static int mStatus = Consts.LOGOUT;
    private BroadcastReceiver mReceiver;
    private boolean mIsRunning;

    @Override
    public void onCreate() {
        super.onCreate();

        initBmob();

        initJPush();

        autoLogin();

        initImageLoaderConfiguration();

        registerReceiver();

    }

    private void initBmob() {
        BmobConfig.Builder builder = new BmobConfig.Builder(this);
        builder.setApplicationId(Consts.APPLICATION_ID);
        builder.setConnectTimeout(6000);
        BmobConfig config = builder.build();
        Bmob.initialize(config);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void autoLogin() {
        boolean autoLogin = SharedPreferencesUtil.getBoolean(this, Consts.KEY_AUTO_LOGIN);
        if (autoLogin) {
            String username = SharedPreferencesUtil.getString(this, Consts.KEY_USERNAME);
            String password = SharedPreferencesUtil.getString(this, Consts.KEY_PASSWORD);
            User User = new User();
            User.setUsername(username);
            User.setPassword(password);
            User.login(new InnerSaveListener());
        }
    }

    private void initImageLoaderConfiguration() {
        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

    private void registerReceiver() {
        mReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.ACTION_MEFRAGMENT_ONCREATEVIEW);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        unregisterReceiver(mReceiver);
    }

    private class InnerSaveListener extends SaveListener<User> {
        @Override
        public void done(User User, BmobException e) {
            if (e == null) {
                mStatus = Consts.LOGIN;
                mUsername = SharedPreferencesUtil.getString(MyApplication.this, Consts.KEY_USERNAME);
            } else {
                e.printStackTrace();
            }
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Consts.ACTION_MEFRAGMENT_ONCREATEVIEW.equals(action)) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            mIsRunning = true;
                            while (mIsRunning) {
                                if (mStatus == Consts.LOGIN) {
                                    Intent intent = new Intent(Consts.ACTION_STATUS_CHANGE);
                                    sendBroadcast(intent);
                                    mIsRunning = false;
                                }
                                sleep(500);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }
}
