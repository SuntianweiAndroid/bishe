package com.bishe.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bishe.myapplication.R;

/**
 * 自定义对话框
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public MyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    /**
     * @param context    上下文对象
     * @param themeResId 对话框样式
     * @param content    对话框提示内容
     * @param listener   点击确定取消按钮监听
     */
    public MyDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public MyDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public MyDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        contentTxt = (TextView) findViewById(R.id.content);
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                //取消按钮
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                //确定按钮
                if (listener != null) {
                    listener.onClick(this, true);
                }
                this.dismiss();
                break;
        }
    }

    //注册监听回掉
    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}