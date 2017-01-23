package com.qq.googleplay.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.qq.googleplay.R;
import com.qq.googleplay.interfaces.BaseViewInterface;
import com.qq.googleplay.manager.AppManager;
import com.qq.googleplay.ui.activity.MainActivity;
import com.qq.googleplay.utils.StringUtil;
import com.qq.googleplay.utils.ToastUtil;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/20 17:01
 * 描 述 ：基类Activity
 * 修订历史 ：
 * ============================================================
 **/
public class BaseActivity extends AppCompatActivity implements BaseViewInterface {

    protected ActionBar      mActionBar;
    protected LayoutInflater mInflater;
    protected Activity       mCurActivity;
    protected long           mPreTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        mActionBar = getSupportActionBar();
        mInflater = getLayoutInflater();
        if (hasActionBar()) {
            initActionBar(mActionBar);
        }
        init(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        mCurActivity = this;// 最上层的一个activity
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2s
                toast("再按一次,退出谷歌市场");
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();
    }

    /**
     * 得到最上层activity
     * @return
     */
    public Activity getCurActivity() {
        return mCurActivity;
    }

    protected void onBeforeSetContentLayout() {
    }

    protected int getLayoutId() {
        return 0;
    }

    protected boolean hasActionBar() {
        return getSupportActionBar() != null;
    }

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null)
            return;
        if (hasBackButton()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        } else {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayUseLogoEnabled(false);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }
        }
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    public void setActionBarTitle(String title) {
        if (StringUtil.isEmpty(title)) {
            title = getString(R.string.app_name);
        }
        if (hasActionBar() && mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    protected void init(Bundle savedInstanceState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String content) {
        ToastUtil.toast(content);
    }

    public void toast(Activity activity, String content, int duration) {
        ToastUtil.toast(activity, content, duration);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
