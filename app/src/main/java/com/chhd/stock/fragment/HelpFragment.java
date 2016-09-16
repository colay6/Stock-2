package com.chhd.stock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chhd.stock.R;
import com.chhd.stock.activity.AboutActivity;
import com.chhd.stock.activity.FeedbackActivity;

/**
 * Created by CWQ on 2016/7/10.
 */
public class HelpFragment extends Fragment {

    private View mView;
    private Button btn_feedback;
    private Button btn_about;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_help, null);

        initViews();

        setListeners();

        return mView;
    }

    private void initViews() {
        btn_feedback = (Button) mView.findViewById(R.id.btn_feedback);
        btn_about = (Button) mView.findViewById(R.id.btn_about);
    }

    private void setListeners() {
        View.OnClickListener onClickListener = new MyOnClickListener();
        btn_feedback.setOnClickListener(onClickListener);
        btn_about.setOnClickListener(onClickListener);
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_feedback: {
                    Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
                }
                break;
                case R.id.btn_about: {
                    Intent intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.set_right2left_enter, R.anim.set_right2left_exit);
                }
                break;
            }
        }
    }
}
