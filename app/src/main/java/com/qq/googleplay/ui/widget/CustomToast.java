package com.qq.googleplay.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.googleplay.R;
import com.qq.googleplay.utils.UIUtils;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class CustomToast extends Toast {
    private ImageView iconView;
    private TextView msgView;
    private View view;

    public CustomToast(Context context) {
        super(context);
        view = UIUtils.inflate(context, R.layout.toast_layout, null, false);
        iconView = ((ImageView) this.view.findViewById(R.id.tosat_icon));
        msgView = ((TextView) this.view.findViewById(R.id.tosat_msg));
        setGravity(16, 0, 0);
        setView(view);
    }

    public void setMessage(String msg) {
        msgView.setText(msg);
    }

    public void setMessageById(int msgId) {
        msgView.setText(msgId);
    }

    public void setErr(boolean isErr) {
        if (isErr) {
            iconView.setImageResource(R.mipmap.err_toast_icon);
        } else {
            iconView.setImageResource(R.mipmap.toast_icon);
        }
    }

    public void show() {
        super.show();
    }
}
