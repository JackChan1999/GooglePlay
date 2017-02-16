package com.qq.googleplay.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseActivity;
import com.qq.googleplay.base.BaseFragment;
import com.qq.googleplay.factory.FragmentFactory;
import com.qq.googleplay.ui.animation.transformation.DepthPageTransformer;
import com.qq.googleplay.ui.widget.CircleImageView;
import com.qq.googleplay.utils.StringUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;
import com.qq.googleplay.utils.bitmap.BitmapUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.nv_menu)
    NavigationView mNavigationView;

    private String[] mTitle;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mToolbar.setLogo(R.mipmap.icon);
        setSupportActionBar(mToolbar);
        initDrawerLayout();
        initListener();
    }

    public void initListener() {

        final ViewPagerChangeListener listener = new ViewPagerChangeListener();
        mViewPager.addOnPageChangeListener(listener);
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                listener.onPageSelected(0);
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    @Override
    public void initData() {
        mTitle = StringUtil.getStringArr(R.array.main_titles);
        mViewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }

        CircleImageView img = (CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.icon);
        BitmapHelper.instance.init(this).display(img,R.mipmap.allen);

        LinearLayout ll_header = (LinearLayout)navigationView.getHeaderView(0).findViewById(R.id.ll_header);
        ll_header.measure(0,0);
        int width = ll_header.getMeasuredWidth();
        int height = ll_header.getMeasuredHeight();
        Bitmap bitmap = BitmapUtils.decodeResource(getResources(), R.mipmap.wallpaper, width, height);
        ll_header.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmap));
    }

    public void initDrawerLayout() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R
                .string.open, R.string.close);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);
        setupDrawerContent(mNavigationView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main, menu);
        //  SupportMenuItem smi = (SupportMenuItem) menu;
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // SearchView searchView = (SearchView) smi.getActionView();
        searchView.setOnQueryTextListener(this);//搜索监听
        /*SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) searchView
                .findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);*/
        initSearchView(searchView);

        return true;
    }

    private void initSearchView(SearchView searchView) {

       /* ImageView search_mag_icon = (ImageView) searchView.findViewById(android.support.v7
                .appcompat.R.id.search_mag_icon);
       *//* int search_mag_icon_id = searchView.getContext().getResources().getIdentifier
                ("android:id/search_mag_icon", null, null);
        ImageView search_mag_icon = (ImageView) searchView.findViewById(search_mag_icon_id);
        //获取搜索图标*//*

        if (search_mag_icon != null) {
            search_mag_icon.setImageResource(R.drawable.ic_action_search);//图标都是用src的
        }*/

        /*int id = searchView.getContext().getResources().getIdentifier
        ("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);*/
        /*Class<?> argClass = searchView.getClass();
        //指定某个私有属性
        try {
            Field mCollapsedIcon = argClass.getDeclaredField("mCollapsedIcon");
            mCollapsedIcon.setAccessible(true);
            ImageView icon = (ImageView) mCollapsedIcon.get(searchView);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap
                    .ic_action_search);

            icon.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) searchView
                .findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);

        LinearLayout linearLayout = (LinearLayout) searchView.findViewById(R.id.search_plate);
       /* View view = new View(UIUtils.getContext());
        view.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, UIUtils.dip2Px(1));
        linearLayout.addView(view,params);*/

        linearLayout.setBackgroundResource(R.drawable.textfield_search_activated_mtrl_alpha);
        ImageView btnSearch = (ImageView) searchView.findViewById(R.id.search_button);
        btnSearch.setImageResource(R.mipmap.ic_action_search);

       /* ImageView searchIcon = (ImageView) searchView.findViewById(R.id.search_mag_icon);
        searchIcon.setVisibility(View.VISIBLE);
        searchIcon.setImageResource(R.mipmap.ic_action_search);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            toast("搜索");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        toast(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        toast(newText);
        return true;
    }

    class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private RecyclerView.RecycledViewPool mPool = new RecyclerView.RecycledViewPool();

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.getFragment(position);
            fragment.mPool = mPool;
            return fragment;
        }

        @Override
        public int getCount() {
            if (mTitle != null) {
                return mTitle.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }

    private class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //完成触发加载
            BaseFragment fragment = FragmentFactory.getFragment(position);
            if (fragment != null) {
                fragment.getLoadingPager().loadData();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
