package com.chhd.stock.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.chhd.stock.R;
import com.chhd.util.ToastUtil;

import java.util.ArrayList;

public class FeedbackActivity extends BaseActivity {

    private ImageButton ib_back;
    private Button btn_submit;
    private RelativeLayout rl_content;
    private EditText et_content;
    private EditText et_contact;
    private ImageButton ib_content_clear;
    private ImageButton ib_contact_clear;

    private String mContent;
    private String mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();

        setListeners();

    }

    private void initViews() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        et_content = (EditText) findViewById(R.id.et_content);
        et_contact = (EditText) findViewById(R.id.et_contact);
        ib_content_clear = (ImageButton) findViewById(R.id.ib_content_clear);
        ib_contact_clear = (ImageButton) findViewById(R.id.ib_contact_clear);
    }

    private void setListeners() {
        View.OnClickListener onClickListener = new MyOnClickListener();
        ib_back.setOnClickListener(onClickListener);
        btn_submit.setOnClickListener(onClickListener);
        ib_content_clear.setOnClickListener(onClickListener);
        ib_contact_clear.setOnClickListener(onClickListener);
        TextWatcher textWatcher = new MyTextWatcher();
        et_content.addTextChangedListener(textWatcher);
        et_contact.addTextChangedListener(textWatcher);
    }

    public String getAppName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            return appName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_back:
                    finish();
                    overridePendingTransition(R.anim.set_left2right_enter, R.anim.set_left2right_exit);
                    break;
                case R.id.btn_submit:
                    mContent = et_content.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        Animation animation = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.set_shake);
                        animation.setInterpolator(new MyInterpolator());
                        rl_content.startAnimation(animation);
                        return;
                    }
                    mContact = et_contact.getText().toString();
                    if (!TextUtils.isEmpty(mContact)) {
                        String emailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
                        String mobliePhoneRegex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
                        if (mContact.matches(emailRegex) || mContact.matches(mobliePhoneRegex)) {

                        } else {
                            ToastUtil.toast(FeedbackActivity.this, "手机号码 或 邮箱格式不正确");
                            Animation animation = AnimationUtils.loadAnimation(FeedbackActivity.this, R.anim.set_shake);
                            animation.setInterpolator(new MyInterpolator());
                            et_contact.startAnimation(animation);
                        }
                    } else {
                        mContact = "none";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(FeedbackActivity.this, R.style.AlertDialog));
                    builder.setMessage("每条短信按运营商正常收费喔!");
                    builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SmsManager smsManager = SmsManager.getDefault();
                            String text = "app: " + getAppName() + "\n\n" + "content: " + mContent + "\n\n" + "contact: " + mContact;
                            ArrayList<String> messages = smsManager.divideMessage(text);
                            for (String message : messages) {
                                smsManager.sendTextMessage("13751847729", null, message, null, null);
                            }
                            ToastUtil.toast(FeedbackActivity.this, "发送成功");
                            ib_contact_clear.setVisibility(View.INVISIBLE);
                            et_content.setText("");
                            et_contact.setText("");
                        }
                    });
                    builder.setNegativeButton("还是不要了", null);
                    builder.show();

                    break;
                case R.id.ib_content_clear:
                    et_content.setText("");
                    break;
                case R.id.ib_contact_clear:
                    et_contact.setText("");
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

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_content.hasFocus()) {
                if (!TextUtils.isEmpty(s)) {
                    ib_content_clear.setVisibility(View.VISIBLE);
                } else {
                    ib_content_clear.setVisibility(View.INVISIBLE);
                }
            } else if (et_contact.hasFocus()) {
                if (!TextUtils.isEmpty(s)) {
                    ib_contact_clear.setVisibility(View.VISIBLE);
                } else {
                    ib_contact_clear.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
