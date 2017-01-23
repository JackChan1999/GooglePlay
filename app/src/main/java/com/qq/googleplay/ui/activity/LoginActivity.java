package com.qq.googleplay.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.android.log.Log;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.constants.AppConstants;
import com.qq.googleplay.ui.widget.CleanEditText;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.RegexUtils;
import com.qq.googleplay.utils.SPUtils;
import com.qq.googleplay.utils.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @desc 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG                      = "loginActivity";
    private static final int    REQUEST_CODE_TO_REGISTER = 0x001;

    // 界面控件
    private CleanEditText accountEdit;
    private CleanEditText passwordEdit;

    // 第三方平台获取的访问token，有效时间，uid
    private String accessToken;
    private String expires_in;
    private String uid;
    private String sns;

    private UMShareAPI mShareAPI;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT > 21){
            getWindow().setStatusBarColor(Color.parseColor("#757575"));
        }
        accountEdit = (CleanEditText) this.findViewById(R.id.et_email_phone);
        accountEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        accountEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        passwordEdit = (CleanEditText) this.findViewById(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    clickLogin();
                }
                return false;
            }
        });

        mShareAPI = UMShareAPI.get(this);
    }

    private void clickLogin() {
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (checkInput(account, password)) {
            // TODO: 请求服务器登录账号
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            ToastUtil.toast(getString(R.string.tip_account_empty));
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if (!RegexUtils.checkMobile(account)) {
                ToastUtil.toast(getString(R.string.tip_account_regex_not_right));
            } else if (password == null || password.trim().equals("")) {
                ToastUtil.toast(getString(R.string.tip_password_can_not_be_empty));
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
           /* case R.id.iv_cancel:
                finish();
                break;*/
            case R.id.btn_login:
                clickLogin();
                break;
            case R.id.iv_wechat:
                clickLoginWexin();
                break;
            case R.id.iv_qq:
                clickLoginQQ();
                break;
            case R.id.iv_sina:
                loginThirdPlatform(SHARE_MEDIA.SINA);
                break;
            case R.id.tv_create_account:
                enterRegister();
                break;
            case R.id.tv_forget_password:
                enterForgetPwd();
                break;
            default:
                break;
        }
    }

    /**
     * 点击使用QQ快速登录
     */
    private void clickLoginQQ() {
        if (!CommonUtil.isQQClientAvailable(this)) {
            ToastUtil.toast(getString(R.string.no_install_qq));
        } else {
            loginThirdPlatform(SHARE_MEDIA.QQ);
        }
    }

    /**
     * 点击使用微信登录
     */
    private void clickLoginWexin() {
        if (!CommonUtil.isWeixinAvilible(this)) {
            ToastUtil.toast(getString(R.string.no_install_wechat));
        } else {
            loginThirdPlatform(SHARE_MEDIA.WEIXIN);
        }
    }

    /**
     * 跳转到忘记密码
     */
    private void enterForgetPwd() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }


    /**
     * 授权。如果授权成功，则获取用户信息
     *
     * @param platform
     */
    private void loginThirdPlatform(final SHARE_MEDIA platform) {
        mShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> data) {
            for (String key : data.keySet()) {
                Log.i("MainActivity", key + "=" + data.get(key));
            }

            //Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            accessToken = data.get("access_token");
            expires_in = data.get("expires_in");
            // 获取uid
            uid = data.get(AppConstants.UID);
            if (data != null && !TextUtils.isEmpty(uid)) {
                // uid不为空，获取用户信息
                getUserInfo(platform);
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            } else {
                ToastUtil.toast(getString(R.string.oauth_fail));
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i(TAG, "onError------" + Thread.currentThread().getId());
            ToastUtil.toast(getString(R.string.oauth_fail));
            //ProgressDialogUtils.getInstance().dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i(TAG, "onCancel------" + Thread.currentThread().getId());
            ToastUtil.toast(getString(R.string.oauth_cancle));
            //ProgressDialogUtils.getInstance().dismiss();
        }
    };

    /**
     * 获取用户信息
     *
     * @param platform
     */
    private void getUserInfo(final SHARE_MEDIA platform) {
        mShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> info) {
                try {
                    String sns_id = "";
                    String sns_avatar = "";
                    String sns_loginname = "";
                    if (info != null && info.size() != 0) {
                        Log.i("third login", info.toString());

                        if (platform == SHARE_MEDIA.SINA) { // 新浪微博
                            sns = AppConstants.SINA;
                            if (info.get(AppConstants.UID) != null) {
                                sns_id = info.get(AppConstants.UID);
                            }
                            if (info.get(AppConstants.PROFILE_IMAGE_URL) != null) {
                                sns_avatar = info.get(AppConstants.PROFILE_IMAGE_URL);
                            }
                            if (info.get(AppConstants.SCREEN_NAME) != null) {
                                sns_loginname = info.get(AppConstants.SCREEN_NAME);
                            }
                        } else if (platform == SHARE_MEDIA.QZONE) { // QQ
                            sns = AppConstants.QQ;
                            if (info.get(AppConstants.UID) == null) {
                                ToastUtil.toast(getString(R.string.oauth_fail));
                                return;
                            }
                            sns_id = info.get(AppConstants.UID);
                            sns_avatar = info.get(
                                    AppConstants.PROFILE_IMAGE_URL);
                            sns_loginname = info.get(
                                    AppConstants.SCREEN_NAME);
                        } else if (platform == SHARE_MEDIA.WEIXIN) { // 微信
                            sns = AppConstants.WECHAT;
                            sns_id = info.get(AppConstants.OPENID);
                            sns_avatar = info.get(
                                    AppConstants.HEADIMG_URL);
                            sns_loginname = info.get(AppConstants.NICKNAME);
                        }

                        // 这里直接保存第三方返回来的用户信息
                        SPUtils.saveBoolean(LoginActivity.this, AppConstants.THIRD_LOGIN, true);

                        Log.e("info", sns + "," + sns_id + "," + sns_loginname);

                        // TODO: 这里执行第三方连接(绑定服务器账号）
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}