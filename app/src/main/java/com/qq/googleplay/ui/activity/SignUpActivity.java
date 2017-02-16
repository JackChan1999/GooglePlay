package com.qq.googleplay.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.android.log.Log;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.ui.widget.CleanEditText;
import com.qq.googleplay.utils.RegexUtils;
import com.qq.googleplay.utils.ToastUtil;
import com.qq.googleplay.utils.VerifyCodeManager;
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
public class SignUpActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SignupActivity";
    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText verifyCodeEdit;
    private Button        getVerifiCodeButton;

    private VerifyCodeManager codeManager;

    @Override
    public void initView() {
        setContentView(R.layout.activity_signup);
        if (Build.VERSION.SDK_INT > 21){
            getWindow().setStatusBarColor(Color.parseColor("#757575"));
        }
        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);
        getVerifiCodeButton.setOnClickListener(this);
        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });

        codeManager = new VerifyCodeManager(this, phoneEdit, getVerifiCodeButton);
    }

    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }

    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String code = verifyCodeEdit.getText().toString().trim();

        if (checkInput(phone, password, code)) {
            // TODO:请求服务端注册账号
        }
    }

    private boolean checkInput(String phone, String password, String code) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            ToastUtil.toast(getString(R.string.tip_phone_can_not_be_empty));
        } else {
            if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
                ToastUtil.toast(getString(R.string.tip_phone_regex_not_right));
            } else if (TextUtils.isEmpty(code)) { // 验证码不正确
                ToastUtil.toast(getString(R.string.tip_please_input_code));
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                ToastUtil.toast(getString(R.string.tip_please_input_6_32_password));
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verifi_code:
                // TODO 请求接口发送验证码
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;

            default:
                break;
        }
    }
}