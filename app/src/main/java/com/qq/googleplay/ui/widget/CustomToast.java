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
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/7/18 17:03
 * 描 述 ：
 * 修订历史 ：
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
