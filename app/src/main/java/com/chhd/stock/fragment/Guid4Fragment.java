package com.chhd.stock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.chhd.stock.R;
import com.chhd.stock.activity.MainActivity;
import com.chhd.stock.util.SharedPreferencesUtil;

/**
 * Created by CWQ on 2016/7/9.
 */
public class Guid4Fragment extends Fragment {

    private View view;
    private Button btnWelcome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_4, null);

        initViews();

        btnWelcome.setOnClickListener(new InnerOnClickListener());

        return view;
    }

    private void initViews() {
        btnWelcome = (Button) view.findViewById(R.id.btn_welcome);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.set_btn_welcome);
            btnWelcome.startAnimation(animation);
            btnWelcome.setVisibility(View.VISIBLE);
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_welcome:

                    SharedPreferencesUtil.cacheGuideInfo(getActivity(), false);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

            }
        }
    }
}
