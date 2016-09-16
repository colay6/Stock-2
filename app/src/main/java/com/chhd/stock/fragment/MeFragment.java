package com.chhd.stock.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chhd.stock.R;
import com.chhd.stock.activity.PasswordActivity;
import com.chhd.stock.activity.RegisterActivity;
import com.chhd.stock.app.MyApplication;
import com.chhd.stock.entity.User;
import com.chhd.stock.util.Consts;
import com.chhd.stock.util.SharedPreferencesUtil;
import com.chhd.stock.view.InputView;
import com.chhd.util.ToastUtil;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CWQ on 2016/7/10.
 */
public class MeFragment extends Fragment {

    private View mView;
    private LinearLayout ll_logout;
    private InputView iv_username;
    private InputView iv_password;
    private CheckBox cb_remember;
    private CheckBox cb_auto_login;
    private Button btn_login;
    private LinearLayout ll_register;
    private LinearLayout ll_login;
    private CircleImageView civ_icon;
    private TextView tv_username;
    private Button btn_icon;
    private Button btn_password;
    private Button btn_logout;

    private BroadcastReceiver mReceiver;
    private View.OnClickListener mOnClickListener;
    private AlertDialog mAlertDialog;
    private String mUsername;
    private String mPassword;
    private long mStartTime;
    private long mEndTime;
    private BmobFile mBmobFile;
    private Bitmap mBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_me, null);

        initViews();

        setListeners();

        readSharedPreferences();

        createDialog();

        registerReceiver();

        return mView;
    }

    private void initViews() {
        ll_logout = (LinearLayout) mView.findViewById(R.id.ll_logout);
        iv_username = (InputView) mView.findViewById(R.id.iv_username);
        iv_password = (InputView) mView.findViewById(R.id.iv_password);
        cb_remember = (CheckBox) mView.findViewById(R.id.cb_remember);
        cb_auto_login = (CheckBox) mView.findViewById(R.id.cb_auto_login);
        btn_login = (Button) mView.findViewById(R.id.btn_login);
        ll_register = (LinearLayout) mView.findViewById(R.id.ll_register);
        ll_login = (LinearLayout) mView.findViewById(R.id.ll_login);
        civ_icon = (CircleImageView) mView.findViewById(R.id.civ_icon);
        tv_username = (TextView) mView.findViewById(R.id.tv_username);
        btn_icon = (Button) mView.findViewById(R.id.btn_icon);
        btn_password = (Button) mView.findViewById(R.id.btn_password);
        btn_logout = (Button) mView.findViewById(R.id.btn_logout);
    }

    private void readSharedPreferences() {
        boolean autoLogin = SharedPreferencesUtil.getBoolean(getActivity(), Consts.KEY_AUTO_LOGIN);
        if (autoLogin) {
            Intent intent = new Intent(Consts.ACTION_MEFRAGMENT_ONCREATEVIEW);
            getActivity().sendBroadcast(intent);
        }

        boolean checked = SharedPreferencesUtil.getBoolean(getActivity(), Consts.KEY_REMEMBER);
        cb_remember.setChecked(checked);
        if (checked) {
            String username = SharedPreferencesUtil.getString(getActivity(), Consts.KEY_USERNAME);
            String password = SharedPreferencesUtil.getString(getActivity(), Consts.KEY_PASSWORD);
            iv_username.setText(username);
            iv_password.setText(password);
        }
        checked = SharedPreferencesUtil.getBoolean(getActivity(), Consts.KEY_AUTO_LOGIN);
        cb_auto_login.setChecked(checked);
    }

    private void setListeners() {
        mOnClickListener = new InnerOnClickListener();
        cb_remember.setOnClickListener(mOnClickListener);
        cb_auto_login.setOnClickListener(mOnClickListener);
        btn_login.setOnClickListener(mOnClickListener);
        ll_register.setOnClickListener(new InnerOnClickListener());
        btn_icon.setOnClickListener(mOnClickListener);
        btn_password.setOnClickListener(mOnClickListener);
        btn_logout.setOnClickListener(mOnClickListener);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mAlertDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_progress, null);
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(false);
    }

    private void registerReceiver() {
        mReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.ACTION_STATUS_CHANGE);
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    private void initIcon() {
        BmobUser bmobUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(bmobUser.getObjectId(), new MyQueryListener());
    }

    private void login() {
        mUsername = iv_username.getText();
        mPassword = iv_password.getText();

        mAlertDialog.show();
        mStartTime = System.currentTimeMillis();

        User user = new User();
        user.setUsername(mUsername);
        user.setPassword(mPassword);
        user.login(new InnerSaveListener());
    }

    private void saveSharedPreferences() {
        boolean checked = cb_remember.isChecked();
        SharedPreferencesUtil.putBoolean(getActivity(), Consts.KEY_REMEMBER, checked);
        if (checked) {
            SharedPreferencesUtil.putString(getActivity(), Consts.KEY_USERNAME, mUsername);
            SharedPreferencesUtil.putString(getActivity(), Consts.KEY_PASSWORD, mPassword);
        } else {
            SharedPreferencesUtil.putString(getActivity(), Consts.KEY_USERNAME, "");
            SharedPreferencesUtil.putString(getActivity(), Consts.KEY_PASSWORD, "");
        }
        checked = cb_auto_login.isChecked();
        SharedPreferencesUtil.putBoolean(getActivity(), Consts.KEY_AUTO_LOGIN, checked);
    }

    private void changeStatus() {
        MyApplication.mStatus = Consts.LOGIN;
        MyApplication.mUsername = mUsername;
    }

    private void showLoginInLayout() {
        tv_username.setText(MyApplication.mUsername);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.set_login_in);
        ll_login.startAnimation(anim);
        ll_logout.setVisibility(View.INVISIBLE);
        ll_login.setVisibility(View.VISIBLE);
    }


    private void hideLoginLayout() {
        ll_logout.setVisibility(View.VISIBLE);
        ll_login.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.set_login_out);
        ll_login.startAnimation(anim);
    }

    private void done(BmobException e) {
        mAlertDialog.dismiss();
        if (e == null) {
            saveSharedPreferences();
            changeStatus();

            Intent intent = new Intent(Consts.ACTION_STATUS_CHANGE);
            getActivity().sendBroadcast(intent);
        } else {
            switch (e.getErrorCode()) {
                case Consts.ERROR_USERNAME_OR_PASSWORD_INCORRECT:
                    ToastUtil.toast(getActivity(), "用户名 或 密码错误");
                    break;
                default:
                    ToastUtil.toast(getActivity(), "登录失败: " + e.getMessage());
                    break;
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Consts.REQUEST_FORM_REGISTERACTIVITY:
                if (data == null) {
                    return;
                }
                mUsername = data.getStringExtra(Consts.KEY_USERNAME);
                mPassword = data.getStringExtra(Consts.KEY_PASSWORD);
                iv_username.setText(mUsername);
                iv_password.setText(mPassword);
                break;
            case Consts.REQUEST_FORM_GALLERY: {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 128);
                intent.putExtra("outputY", 128);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, Consts.REQUEST_FORM_CROP);
            }
            break;
            case Consts.REQUEST_FORM_CROP:
                try {
                    if (data == null) {
                        return;
                    }
                    mBitmap = data.getParcelableExtra("data");

                    String absolutePath = getActivity().getFilesDir().getAbsolutePath();
                    File file = new File(absolutePath, "icon");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    mAlertDialog.show();

                    mBmobFile = new BmobFile(file);
                    mBmobFile.upload(new MyUploadFileListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Consts.REQUEST_FORM_CAMERA: {
                if (data == null) {
                    return;
                }
                Bitmap bitmap = data.getParcelableExtra("data");

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setType("image/*");
                intent.putExtra("data", bitmap);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 128);
                intent.putExtra("outputY", 128);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, Consts.REQUEST_FORM_CROP);
            }
            break;
            case Consts.REQUEST_FORM_PASSWRODACTIVITY:
                if (resultCode == Consts.REQUEST_SUCCESS) {
                    MyApplication.mStatus = Consts.LOGOUT;

                    Intent intent = new Intent(Consts.ACTION_STATUS_CHANGE);
                    getActivity().sendBroadcast(intent);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }


    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            boolean checked = false;
            switch (v.getId()) {
                case R.id.cb_remember:
                    checked = cb_remember.isChecked();
                    if (!checked) {
                        cb_auto_login.setChecked(checked);
                    }
                    break;
                case R.id.cb_auto_login:
                    checked = cb_auto_login.isChecked();
                    if (checked) {
                        cb_remember.setChecked(checked);
                    }
                    break;
                case R.id.btn_login:
                    login();
                    break;
                case R.id.ll_register: {
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivityForResult(intent, Consts.REQUEST_FORM_REGISTERACTIVITY);
                }
                break;
                case R.id.btn_logout: {
                    MyApplication.mStatus = Consts.LOGOUT;

                    Intent intent = new Intent(Consts.ACTION_STATUS_CHANGE);
                    getActivity().sendBroadcast(intent);
                }
                break;
                case R.id.btn_icon: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    DialogInterfaceOnClickListener dialogInterfaceOnClickListener = new DialogInterfaceOnClickListener();
                    builder.setPositiveButton("图库", dialogInterfaceOnClickListener);
                    builder.setNegativeButton("拍照", dialogInterfaceOnClickListener);
                    builder.show();
                }
                break;
                case R.id.btn_password:
                    Intent intent = new Intent(getActivity(), PasswordActivity.class);
                    startActivityForResult(intent, Consts.REQUEST_FORM_PASSWRODACTIVITY);
                    break;
            }
        }
    }

    private class DialogInterfaceOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Consts.REQUEST_FORM_GALLERY);
                }
                break;
                case DialogInterface.BUTTON_NEGATIVE: {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Consts.REQUEST_FORM_CAMERA);
                }
                break;
            }
        }
    }

    private class InnerSaveListener extends SaveListener<User> {
        @Override
        public void done(User User, final BmobException e) {
            mEndTime = System.currentTimeMillis();
            long time = mEndTime - mStartTime;
            if (time < Consts.LOADING_TIME) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MeFragment.this.done(e);
                    }
                }, Consts.LOADING_TIME - time);
            } else {
                MeFragment.this.done(e);
            }
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Consts.ACTION_STATUS_CHANGE.equals(action)) {
                if (MyApplication.mStatus == Consts.LOGIN) {
                    initIcon();
                    showLoginInLayout();
                } else {
                    hideLoginLayout();
                }
            }
        }
    }

    private class MyUploadFileListener extends UploadFileListener {
        @Override
        public void done(BmobException e) {
            if (e == null) {
                BmobUser bmobUser = BmobUser.getCurrentUser(User.class);

                User user = new User();
                user.setIcon(mBmobFile);
                user.update(bmobUser.getObjectId(), new MyUpdateListener());
            } else {
                e.printStackTrace();
            }
        }
    }

    private class MyUpdateListener extends UpdateListener {
        @Override
        public void done(BmobException e) {
            if (e == null) {
                mAlertDialog.dismiss();
                civ_icon.setImageBitmap(mBitmap);
                ToastUtil.toast(getActivity(), "头像保存成功");
            } else {
                ToastUtil.toast(getActivity(), "头像保存失败");
                e.printStackTrace();
            }
        }
    }

    private class MyQueryListener extends QueryListener<User> {
        @Override
        public void done(User user, BmobException e) {
            if (e == null) {
                String url = user.getIcon().getUrl();
                if (!TextUtils.isEmpty(url)) {
                    BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                    bitmapUtils.configDefaultLoadingImage(R.mipmap.ic_1820_128p);
                    bitmapUtils.configDefaultLoadFailedImage(R.mipmap.ic_1820_128p);
                    bitmapUtils.display(civ_icon, url);
                }
            } else {
                civ_icon.setImageResource(R.mipmap.ic_1820_128p);
                e.printStackTrace();
            }
        }
    }
}
