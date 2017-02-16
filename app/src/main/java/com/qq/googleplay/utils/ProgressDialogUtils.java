package com.qq.googleplay.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
public class ProgressDialogUtils {
    private static ProgressDialog mProgressDialog = null;
    private static Activity mContext;
    private static ProgressDialogUtils mInstance = null;

    public static ProgressDialogUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressDialogUtils();

        }
        return mInstance;
    }

    public void show(Activity activity, int mMessage) {


        try {
            if (activity == null) {
                return;
            }
            // 开始请求是，显示请求对话框
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(activity, 0);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage(activity.getResources().getString(mMessage));
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                });
            }
            if (!activity.isFinishing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Activity activity, String mMessage) {

        try {
            if (activity == null) {
                return;
            }
            // 开始请求是，显示请求对话框
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(activity, 0);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage(mMessage);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                });
            }

            if (!activity.isFinishing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void dismiss() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
