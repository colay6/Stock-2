package com.chhd.stock.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.chhd.stock.R;
import com.chhd.stock.util.Consts;

/**
 * Created by CWQ on 2016/7/16.
 */
public class InputView extends LinearLayout {

    private View mView;
    private EditText et_input;
    private ImageButton ib_clear;

    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mView = View.inflate(context, R.layout.view_input, this);

        initViews();

        setListeners();

        setData(context, attrs);

    }

    private void initViews() {
        et_input = (EditText) mView.findViewById(R.id.et_input);
        ib_clear = (ImageButton) mView.findViewById(R.id.ib_clear);
    }

    private void setListeners() {
        et_input.addTextChangedListener(new InnerTextWatcher());
        ib_clear.setOnClickListener(new InnerOnClickListener());
    }

    private void setData(Context context, AttributeSet attrs) {
        int resourceID = attrs.getAttributeResourceValue(Consts.NAMESPACE, "drawableLeft", 0);
        Drawable drawableLeft = ContextCompat.getDrawable(context, resourceID);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int density = (int) metrics.density;
        drawableLeft.setBounds(0, 0, 16 * density, 16 * density);
        et_input.setCompoundDrawables(drawableLeft, null, null, null);

        String hint = attrs.getAttributeValue(Consts.NAMESPACE, "hint");
        et_input.setHint(hint);

        int inputType = 0;
        String type = attrs.getAttributeValue(Consts.NAMESPACE, "inputType");
        if ("textPassword".equals(type)) {
            inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        } else {
            inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
        }
        et_input.setInputType(inputType);
    }

    public String getText() {
        return et_input.getText().toString();
    }

    public void setText(CharSequence text) {
        et_input.setText(text);
    }

    public void clear() {
        et_input.setText("");
    }

    public void setFocusable(boolean focusable) {
        et_input.setFocusable(true);
        et_input.setFocusableInTouchMode(true);
        et_input.requestFocus();
    }

    private class InnerTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                ib_clear.setVisibility(View.VISIBLE);
            } else {
                ib_clear.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class InnerOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_clear:
                    et_input.setText("");
                    break;
            }
        }
    }

}
