package com.qq.googleplay.ui.holder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.googleplay.R;
import com.qq.googleplay.base.BaseHolder;
import com.qq.googleplay.net.RequestConstants;
import com.qq.googleplay.ui.animation.transformation.StackTransformer;
import com.qq.googleplay.ui.convenientbanner.ConvenientBanner;
import com.qq.googleplay.ui.convenientbanner.holder.CBViewHolderCreator;
import com.qq.googleplay.ui.convenientbanner.holder.Holder;
import com.qq.googleplay.utils.CommonUtil;
import com.qq.googleplay.utils.bitmap.BitmapHelper;
import com.qq.googleplay.utils.glidepalette.GlidePalette;

import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/3/26 15:49
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class PictureHolder extends BaseHolder<List<String>> {

    private List<String> mDesc;

    private ConvenientBanner mConvenientBanner;

    public PictureHolder(Context context) {
        super(context);
    }

    public void setDescData(List<String> data){
        mDesc = data;
    }

    public void notifyDataSetChanged(){
        if (mConvenientBanner != null){
            mConvenientBanner.notifyDataSetChanged();
        }
    }

    @Override
    public View initHolderView() {

       /* View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.layout_cb, null);
        mConvenientBanner = (ConvenientBanner) view.findViewById(R.id.convenient_banner);
        mConvenientBanner.setMinimumHeight(UIUtils.dip2Px(180));*/
        mConvenientBanner = new ConvenientBanner(mContext);
        mConvenientBanner.setMinimumHeight(CommonUtil.dip2Px(180));
        return mConvenientBanner;
    }

    @Override
    public void refreshHolderView(List<String> data) {
        mConvenientBanner.setPages(new ViewHolderCreator(), data)
                .setPageIndicator(new int[]{R.drawable.point_normal, R.drawable.point_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(5000)
                .setCanLoop(true);

        mConvenientBanner.getViewPager().setPageTransformer(true, new StackTransformer());
        mConvenientBanner.setScrollDuration(1200);
    }


    private class ViewHolder implements Holder<String> {

        private ImageView imageView;
        private TextView tvTitle;

        @Override
        public View createView(Context context) {
            RelativeLayout relativeLayout = new RelativeLayout(context);

            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            RelativeLayout.LayoutParams iv_lp = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(iv_lp);


            tvTitle = new TextView(context);
            tvTitle.setTextSize(16);
            tvTitle.setHeight(CommonUtil.dip2Px(30));
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);
            tvTitle.setPadding(CommonUtil.dip2Px(10), 0, 0, 0);
            RelativeLayout.LayoutParams tv_lp = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            tvTitle.setLayoutParams(tv_lp);

            relativeLayout.addView(imageView);
            relativeLayout.addView(tvTitle);
            return relativeLayout;
        }

        @Override
        public void UpdateUI(Context context, final int position, String url) {

            url = RequestConstants.URLS.IMAGEBASEURL + url;
            BitmapHelper.instance.init(mContext).manager.load(url).listener(
                    GlidePalette.with(url).use(GlidePalette.Profile.VIBRANT)
                            .intoBackground(tvTitle, GlidePalette.Swatch.RGB)
                            .intoTextColor(tvTitle, GlidePalette.Swatch.BODY_TEXT_COLOR))
                            .into(imageView);

            tvTitle.setText(mDesc.get(position));
        }

    }

    private class ViewHolderCreator implements CBViewHolderCreator<ViewHolder> {

        @Override
        public ViewHolder createHolder() {
            return new ViewHolder();
        }
    }
}
