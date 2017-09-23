package com.agenthun.readingroutine.utils;

import android.support.annotation.DrawableRes;

import com.agenthun.readingroutine.R;

public enum Avatar {

    ONE(R.drawable.avatar_1_raster),
    TWO(R.drawable.avatar_2_raster),
    THREE(R.drawable.avatar_3_raster),
    FOUR(R.drawable.avatar_4_raster),
    FIVE(R.drawable.avatar_5_raster),
    SIX(R.drawable.avatar_6_raster),
    SEVEN(R.drawable.avatar_7_raster),
    EIGHT(R.drawable.avatar_8_raster),
    NINE(R.drawable.avatar_9_raster),
    TEN(R.drawable.avatar_10_raster),
    ELEVEN(R.drawable.avatar_11_raster),
    TWELVE(R.drawable.avatar_12_raster),
    THIRTEEN(R.drawable.avatar_13_raster),
    FOURTEEN(R.drawable.avatar_14_raster),
    FIFTEEN(R.drawable.avatar_15_raster),
    SIXTEEN(R.drawable.avatar_16_raster),
    SEVENTEEN(R.drawable.avatar_17_raster),
    EIGHTEEN(R.drawable.avatar_18_raster),
    NINETEEN(R.drawable.avatar_19_raster),
    TWENTY(R.drawable.avatar_20_raster),
    TWENTY_1(R.drawable.avatar_20_1_raster);

    private int mResId;

    Avatar(@DrawableRes int resId) {
        mResId = resId;
    }

    @DrawableRes
    public int getDrawableId() {
        return mResId;
    }

    public String getNameForAccessibility() {
        return ordinal() + 1 + "";
    }
}
