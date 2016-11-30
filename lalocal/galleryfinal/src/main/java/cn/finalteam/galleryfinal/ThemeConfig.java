/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/16 下午2:49
 */
public class ThemeConfig implements Serializable{

    //默认主题
    public static ThemeConfig DEFAULT = new ThemeConfig.Builder().build();
    //黑色主题
    public static ThemeConfig DARK = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.rgb(0x38, 0x42, 0x48))
            .setFabNormalColor(Color.rgb(0x38, 0x42, 0x48))
            .setFabPressedColor(Color.rgb(0x20, 0x25, 0x28))
            .setCheckSelectedColor(Color.rgb(0x38, 0x42, 0x48))
            .setCropControlColor(Color.rgb(0x38, 0x42, 0x48))
            .build();
    //蓝绿主题
    public static ThemeConfig CYAN = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.rgb(0x01, 0x83, 0x93))
            .setFabNormalColor(Color.rgb(0x00, 0xac, 0xc1))
            .setFabPressedColor(Color.rgb(0x01, 0x83, 0x93))
            .setCheckSelectedColor(Color.rgb(0x00, 0xac, 0xc1))
            .setCropControlColor(Color.rgb(0x00, 0xac, 0xc1))
            .build();
    //橙色主题
    public static ThemeConfig ORANGE = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.rgb(0xFF, 0x57, 0x22))
            .setFabNormalColor(Color.rgb(0xFF, 0x57, 0x22))
            .setFabPressedColor(Color.rgb(0xE6, 0x4A, 0x19))
            .setCheckSelectedColor(Color.rgb(0xFF, 0x57, 0x22))
            .setCropControlColor(Color.rgb(0xFF, 0x57, 0x22))
            .build();
    //绿色主题
    public static ThemeConfig GREEN = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.rgb(0x4C, 0xAF, 0x50))
            .setFabNormalColor(Color.rgb(0x4C, 0xAF, 0x50))
            .setFabPressedColor(Color.rgb(0x38, 0x8E, 0x3C))
            .setCheckSelectedColor(Color.rgb(0x4C, 0xAF, 0x50))
            .setCropControlColor(Color.rgb(0x4C, 0xAF, 0x50))
            .build();
    //青绿色主题
    public static ThemeConfig TEAL = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.rgb(0x00, 0x96, 0x88))
            .setFabNormalColor(Color.rgb(0x00, 0x96, 0x88))
            .setFabPressedColor(Color.rgb(0x00, 0x79, 0x6B))
            .setCheckSelectedColor(Color.rgb(0x00, 0x96, 0x88))
            .setCropControlColor(Color.rgb(0x00, 0x96, 0x88))
            .build();

    private int titleBarTextColor;
    private int titleBarBgColor;
    private int titleBarIconColor;
    private int checkNormalColor;
    private int checkNormalResource;
    private Drawable checkNormalDrawable;
    private int checkSelectedColor;
    private int checkSelectedResource;
    private Drawable checkSelectedDrawable;
    private int fabNormalColor;
    private int fabPressedColor;
    private int cropControlColor;

    private int iconBack;
    private int iconCamera;
    private int iconCrop;
    private int iconRotate;
    private int iconClear;
    private int iconFolderArrow;
    private int iconDelete;
    private int iconCheck;
    private int iconFab;
    private int iconPreview;

    private Drawable bgEditTexture;
    private Drawable bgPreveiw;

    private ThemeConfig(Builder builder) {
        this.titleBarTextColor = builder.titleBarTextColor;
        this.titleBarBgColor = builder.titleBarBgColor;
        this.titleBarIconColor = builder.titleBarIconColor;
        this.checkNormalColor = builder.checkNormalColor;
        this.checkSelectedColor = builder.checkSelectedColor;
        this.fabNormalColor = builder.fabNormalColor;
        this.fabPressedColor = builder.fabPressedColor;
        this.cropControlColor = builder.cropControlColor;
        this.iconBack = builder.iconBack;
        this.iconCamera = builder.iconCamera;
        this.iconCrop = builder.iconCrop;
        this.iconRotate = builder.iconRotate;
        this.iconClear = builder.iconClear;
        this.iconDelete = builder.iconDelete;
        this.iconFolderArrow = builder.iconFolderArrow;
        this.iconCheck = builder.iconCheck;
        this.iconFab = builder.iconFab;
        this.bgEditTexture = builder.bgEditTexture;
        this.iconPreview = builder.iconPreview;
        this.bgPreveiw = builder.bgPreveiw;
        this.checkNormalResource = builder.checkNormalResource;
        this.checkSelectedResource = builder.checkSelectedResource;
        this.checkNormalDrawable = builder.checkNormalDrawable;
        this.checkSelectedDrawable = builder.checkSelectedDrawable;
    }

    public static class Builder {
        private int titleBarTextColor = Color.WHITE;
        private int titleBarBgColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int titleBarIconColor = Color.WHITE;
        private int checkNormalColor = Color.rgb(0xd2, 0xd2, 0xd7);
        private int checkSelectedColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int fabNormalColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int fabPressedColor = Color.rgb(0x30, 0x3f, 0x9f);
        private int cropControlColor = Color.rgb(0x3F, 0x51, 0xB5);

        private int iconBack = R.drawable.ic_arrow_left;
        private int iconCamera = R.drawable.ic_gf_camera;
        private int iconCrop = R.drawable.ic_gf_crop;
        private int iconRotate = R.drawable.ic_gf_rotate;
        private int iconClear = R.drawable.ic_gf_clear;
        private int iconFolderArrow = R.drawable.ic_arrow_drop_down;
        private int iconDelete = R.drawable.ic_delete_photo;
        private int iconCheck = R.drawable.ic_folder_check;
        private int iconFab = R.drawable.ic_folder_check;
        private int iconPreview = R.drawable.ic_gf_preview;

        private Drawable bgEditTexture;
        private Drawable bgPreveiw;

        private int checkNormalResource;
        private int checkSelectedResource;

        private Drawable checkNormalDrawable;
        private Drawable checkSelectedDrawable;

        public Builder setTitleBarTextColor(int titleBarTextColor) {
            this.titleBarTextColor = titleBarTextColor;
            return this;
        }

        public Builder setTitleBarBgColor(int titleBarBgColor) {
            this.titleBarBgColor = titleBarBgColor;
            return this;
        }

        public Builder setTitleBarIconColor(int iconColor) {
            this.titleBarIconColor = iconColor;
            return this;
        }

        public Builder setCheckNormalColor(int checkNormalColor) {
            this.checkNormalColor = checkNormalColor;
            return this;
        }

        public Builder setCheckSelectedColor(int checkSelectedColor) {
            this.checkSelectedColor = checkSelectedColor;
            return this;
        }

        public Builder setCropControlColor(int cropControlColor) {
            this.cropControlColor = cropControlColor;
            return this;
        }

        public Builder setFabNormalColor(int fabNormalColor) {
            this.fabNormalColor = fabNormalColor;
            return this;
        }

        public Builder setFabPressedColor(int fabPressedColor) {
            this.fabPressedColor = fabPressedColor;
            return this;
        }

        public Builder setIconBack(int iconBack) {
            this.iconBack = iconBack;
            return this;
        }

        public Builder setIconCamera(int iconCamera) {
            this.iconCamera = iconCamera;
            return this;
        }

        public Builder setIconCrop(int iconCrop) {
            this.iconCrop = iconCrop;
            return this;
        }

        public Builder setIconRotate(int iconRotate) {
            this.iconRotate = iconRotate;
            return this;
        }

        public Builder setIconClear(int iconClear) {
            this.iconClear = iconClear;
            return this;
        }

        public Builder setIconFolderArrow(int iconFolderArrow) {
            this.iconFolderArrow = iconFolderArrow;
            return this;
        }

        public Builder setIconDelete(int iconDelete) {
            this.iconDelete = iconDelete;
            return this;
        }

        public Builder setIconCheck(int iconCheck) {
            this.iconCheck = iconCheck;
            return this;
        }

        public Builder setIconFab(int iconFab) {
            this.iconFab = iconFab;
            return this;
        }

        public Builder setEditPhotoBgTexture(Drawable bgEditTexture) {
            this.bgEditTexture = bgEditTexture;
            return this;
        }

        public Builder setIconPreview(int iconPreview) {
            this.iconPreview = iconPreview;
            return this;
        }

        public Builder setPreviewBg(Drawable bgPreveiw) {
            this.bgPreveiw = bgPreveiw;
            return this;
        }

        public int getCheckNormalResource() {
            return checkNormalResource;
        }

        public void setCheckNormalResource(int checkNormalResource) {
            this.checkNormalResource = checkNormalResource;
        }

        public int getCheckSelectedResource() {
            return checkSelectedResource;
        }

        public void setCheckSelectedResource(int checkSelectedResource) {
            this.checkSelectedResource = checkSelectedResource;
        }

        public Drawable getCheckNormalDrawable() {
            return checkNormalDrawable;
        }

        public Builder setCheckNormalDrawable(Drawable checkNormalDrawable) {
            this.checkNormalDrawable = checkNormalDrawable;
            return this;
        }

        public Builder setCheckSelectedDrawable(Drawable checkSelectedDrawable) {
            this.checkSelectedDrawable = checkSelectedDrawable;
            return this;
        }

        public Drawable getCheckSelectedDrawable() {
            return checkSelectedDrawable;
        }

        public ThemeConfig build() {
            return new ThemeConfig(this);
        }
    }

    public int getTitleBarTextColor() {
        return titleBarTextColor;
    }

    public int getTitleBarBgColor() {
        return titleBarBgColor;
    }

    public int getCheckNormalColor() {
        return checkNormalColor;
    }

    public int getCheckSelectedColor() {
        return checkSelectedColor;
    }

    public int getTitleBarIconColor() {
        return titleBarIconColor;
    }

    public int getFabNormalColor() {
        return fabNormalColor;
    }

    public int getFabPressedColor() {
        return fabPressedColor;
    }

    public int getCropControlColor() {
        return cropControlColor;
    }

    public int getIconBack() {
        return iconBack;
    }

    public int getIconCamera() {
        return iconCamera;
    }

    public int getIconCrop() {
        return iconCrop;
    }

    public int getIconRotate() {
        return iconRotate;
    }

    public int getIconClear() {
        return iconClear;
    }

    public int getIconFolderArrow() {
        return iconFolderArrow;
    }

    public int getIconDelete() {
        return iconDelete;
    }

    public int getIconCheck() {
        return iconCheck;
    }

    public int getIconFab() {
        return iconFab;
    }

    public int getIconPreview() {
        return iconPreview;
    }

    public Drawable getPreviewBg() {
        return bgPreveiw;
    }

    public Drawable getEditPhotoBgTexture() {
        return bgEditTexture;
    }

    public int getCheckNormalResource() {
        return checkNormalResource;
    }

    public void setCheckNormalResource(int checkNormalResource) {
        this.checkNormalResource = checkNormalResource;
    }

    public int getCheckSelectedResource() {
        return checkSelectedResource;
    }

    public void setCheckSelectedResource(int checkSelectedResource) {
        this.checkSelectedResource = checkSelectedResource;
    }

    public Drawable getCheckNormalDrawable() {
        return checkNormalDrawable;
    }

    public void setCheckNormalDrawable(Drawable checkNormalDrawable) {
        this.checkNormalDrawable = checkNormalDrawable;
    }

    public Drawable getCheckSelectedDrawable() {
        return checkSelectedDrawable;
    }

    public void setCheckSelectedDrawable(Drawable checkSelectedDrawable) {
        this.checkSelectedDrawable = checkSelectedDrawable;
    }
}
