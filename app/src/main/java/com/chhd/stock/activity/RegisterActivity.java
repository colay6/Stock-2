package com.chhd.stock.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chhd.stock.R;
import com.chhd.stock.entity.User;
import com.chhd.stock.util.Consts;
import com.chhd.stock.view.InputView;
import com.chhd.util.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private InputView iv_username;
    private InputView iv_password;
    private InputView iv_confirm_password;
    private Button btn_confirm;
    private Button btn_cancel;
    private ProgressBar pb_progress;
    private View.OnClickListener mOnClickListener;
    private AlertDialog mAlertDialog;
    private String mUsername;
    private String mPassword;
    private long mStartTime;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        setListeners();

        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        mAlertDialog = builder.create();
        View view = View.inflate(RegisterActivity.this, R.layout.dialog_progress, null);
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(false);
    }

    private void initViews() {
        iv_username = (InputView) findViewById(R.id.iv_username);
        iv_password = (InputView) findViewById(R.id.iv_password);
        iv_confirm_password = (InputView) findViewById(R.id.iv_confirm_password);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
    }

    private void setListeners() {
        mOnClickListener = new InnerOnClickListener();
        btn_confirm.setOnClickListener(mOnClickListener);
        btn_cancel.setOnClickListener(mOnClickListener);
    }

    private void register() {
        mAlertDialog.show();
        mStartTime = System.currentTimeMillis();

        User user = new User();
        user.setUsername(mUsername);
        user.setPassword(mPassword);
        user.signUp(new InnerSaveListener());
    }

    private boolean validateForm() {
        mUsername = iv_username.getText();
        mPassword = iv_password.getText();
        String confirmPassword = iv_confirm_password.getText();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.set_shake);
        animation.setInterpolator(new MyInterpolator());
        if (mUsername.length() < 6 || mUsername.length() > 20) {
            ToastUtil.toast(RegisterActivity.this, "用户名格式不正确, 请输入6—20位字母/数字");
            iv_username.clear();
            iv_username.startAnimation(animation);
            return false;
        }
        if (mPassword.length() < 6 || mPassword.length() > 20) {
            ToastUtil.toast(RegisterActivity.this, "密码格式不正确, 请输入6—20位字母/数字");
            iv_password.clear();
            iv_password.startAnimation(animation);
            return false;
        }
        if (confirmPassword.length() < 6 || confirmPassword.length() > 20) {
            ToastUtil.toast(RegisterActivity.this, "确认密码格式不正确, 请输入6—20位字母/数字");
            iv_confirm_password.clear();
            iv_confirm_password.startAnimation(animation);
            return false;
        }
        if (!mPassword.equals(confirmPassword)) {
            ToastUtil.toast(RegisterActivity.this, "二次密码不一样");
            iv_password.clear();
            iv_confirm_password.clear();
            iv_password.startAnimation(animation);
            iv_confirm_password.startAnimation(animation);
            iv_password.setFocusable(true);
            return false;
        }
        return true;
    }

    private void done(BmobException e) {
        mAlertDialog.dismiss();
        if (e == null) {
            Intent intent = new Intent();
            intent.putExtra(Consts.KEY_USERNAME, mUsername);
            intent.putExtra(Consts.KEY_PASSWORD, mPassword);
            RegisterActivity.this.setResult(0, intent);
            finish();
        } else {
            switch (e.getErrorCode()) {
                case Consts.ERROR_USERNAME_ALREADY_EXISTS:
                    ToastUtil.toast(RegisterActivity.this,"用户已存在");
                    break;
                case Consts.ERROR_NETWORK_IS_NOT_AVAILABLE:
                    ToastUtil.toast(RegisterActivity.this,"网络不可用");
                    break;
                default:
                    ToastUtil.toast(RegisterActivity.this, "注册失败: " + e.getMessage());
                    break;
            }
            e.printStackTrace();

        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    if (!validateForm()) return;
                    register();
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;
            }
        }
    }

    private class InnerSaveListener extends SaveListener<User> {
        @Override
        public void done(User user, final BmobException e) {
            mEndTime = System.currentTimeMillis();
            long time = mEndTime - mStartTime;
            if (time < Consts.LOADING_TIME) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RegisterActivity.this.done(e);
                    }
                }, Consts.LOADING_TIME - time);
            } else {
                RegisterActivity.this.done(e);
            }
        }
    }

    private class MyInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(2 * 7 * Math.PI * input));
        }
    }
}
