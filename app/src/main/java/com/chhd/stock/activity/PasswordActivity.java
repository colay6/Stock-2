package com.chhd.stock.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;

import com.chhd.stock.R;
import com.chhd.stock.util.Consts;
import com.chhd.stock.view.InputView;
import com.chhd.util.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordActivity extends AppCompatActivity {

    private InputView iv_old_password;
    private InputView iv_new_password;
    private InputView iv_confirm_password;
    private Button btn_confirm;
    private Button btn_cancel;

    private long mStartTime;
    private long mEndTime;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initViews();

        setListeners();

        createDialog();
    }

    private void initViews() {
        iv_old_password = (InputView) findViewById(R.id.iv_old_password);
        iv_new_password = (InputView) findViewById(R.id.iv_new_password);
        iv_confirm_password = (InputView) findViewById(R.id.iv_confirm_password);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void setListeners() {
        View.OnClickListener onClickListener = new MyOnClickListener();
        btn_confirm.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_progress, null);
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(false);
    }

    private void done(BmobException e) {
        mAlertDialog.dismiss();
        if (e == null) {
            ToastUtil.toast(PasswordActivity.this, "修改密码成功");
            setResult(Consts.REQUEST_SUCCESS);
            finish();
        } else {
            switch (e.getErrorCode()) {
                case Consts.ERROR_OLD_PASSWORD_INCORRECT:
                    ToastUtil.toast(PasswordActivity.this, "密码错误");
                    Animation animation = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.set_shake);
                    animation.setInterpolator(new MyInterpolator());
                    iv_old_password.clear();
                    iv_old_password.startAnimation(animation);
                    break;
                default:
                    ToastUtil.toast(PasswordActivity.this, "修改密码失败: "+e.getMessage());
                    break;
            }
            e.printStackTrace();
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    String oldPwd = iv_old_password.getText();
                    String newPwd = iv_new_password.getText();
                    String confirmPwd = iv_confirm_password.getText();
                    Animation animation = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.set_shake);
                    animation.setInterpolator(new MyInterpolator());
                    if (TextUtils.isEmpty(oldPwd)) {
                        ToastUtil.toast(PasswordActivity.this, "密码不能为空");
                        iv_old_password.setFocusable(true);
                        iv_old_password.startAnimation(animation);
                        return;
                    }
                    if (newPwd.length() < 6 || newPwd.length() > 20) {
                        ToastUtil.toast(PasswordActivity.this, "密码格式不正确, 请输入6—20位字母/数字");
                        iv_old_password.clear();
                        iv_old_password.startAnimation(animation);
                    }
                    if (confirmPwd.length() < 6 || confirmPwd.length() > 20) {
                        ToastUtil.toast(PasswordActivity.this, "密码格式不正确, 请输入6—20位字母/数字");
                        iv_confirm_password.clear();
                        iv_confirm_password.startAnimation(animation);
                    }
                    if (!newPwd.equals(confirmPwd)) {
                        ToastUtil.toast(PasswordActivity.this, "二次密码不一样");
                        iv_old_password.clear();
                        iv_confirm_password.clear();
                        iv_old_password.startAnimation(animation);
                        iv_confirm_password.startAnimation(animation);
                        iv_old_password.setFocusable(true);
                    }

                    mStartTime = System.currentTimeMillis();
                    mAlertDialog.show();
                    BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new MyUpdateListener());
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;
            }
        }
    }

    private class MyInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(2 * 7 * Math.PI * input));
        }
    }

    private class MyUpdateListener extends UpdateListener {
        @Override
        public void done(final BmobException e) {
            mEndTime = System.currentTimeMillis();
            long time = mEndTime - mStartTime;
            if (time < Consts.LOADING_TIME) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PasswordActivity.this.done(e);
                    }
                }, Consts.LOADING_TIME - time);
            } else {
                PasswordActivity.this.done(e);
            }
        }
    }
}
