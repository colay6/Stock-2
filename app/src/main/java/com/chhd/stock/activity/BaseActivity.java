package com.chhd.stock.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.chhd.stock.R;

/**
 * Created by CWQ on 2016/7/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
