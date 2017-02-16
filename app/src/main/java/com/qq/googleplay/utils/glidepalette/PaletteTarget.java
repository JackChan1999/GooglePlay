package com.qq.googleplay.utils.glidepalette;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
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
public class PaletteTarget {

    @BitmapPalette.Profile
    protected int paletteProfile = GlidePalette.Profile.VIBRANT;

    protected ArrayList<Pair<View, Integer>> targetsBackground = new ArrayList<>();
    protected ArrayList<Pair<TextView, Integer>> targetsText = new ArrayList<>();

    protected boolean targetCrossfade = false;
    protected int targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED;
    protected static final int DEFAULT_CROSSFADE_SPEED = 300;

    public PaletteTarget(@BitmapPalette.Profile int paletteProfile) {
        this.paletteProfile = paletteProfile;
    }

    public void clear() {
        targetsBackground.clear();
        targetsText.clear();

        targetsBackground = null;
        targetsText = null;

        targetCrossfade = false;
        targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED;
    }
}
