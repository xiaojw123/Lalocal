package com.lalocal.lalocal.easemob.domain;

import com.easemob.easeui.domain.EaseEmojicon;
import com.easemob.easeui.domain.EaseEmojiconGroupEntity;
import com.lalocal.lalocal.R;

import java.util.Arrays;

/**
 * Created by easemob
 */
public class EmojiconExampleGroupData {
    private static int[] icons = new int[]{
            R.drawable.em_icon_002_cover,
            R.drawable.em_icon_007_cover,
            R.drawable.em_icon_010_cover,
            R.drawable.em_icon_012_cover,
            R.drawable.em_icon_013_cover,
            R.drawable.em_icon_018_cover,
            R.drawable.em_icon_019_cover,
            R.drawable.em_icon_020_cover,
            R.drawable.em_icon_021_cover,
            R.drawable.em_icon_022_cover,
            R.drawable.em_icon_024_cover,
            R.drawable.em_icon_027_cover,
            R.drawable.em_icon_029_cover,
            R.drawable.em_icon_030_cover,
            R.drawable.em_icon_035_cover,
            R.drawable.em_icon_040_cover,
    };
    private static int[] bigIcons = new int[]{
            R.drawable.em_icon_002,
            R.drawable.em_icon_007,
            R.drawable.em_icon_010,
            R.drawable.em_icon_012,
            R.drawable.em_icon_013,
            R.drawable.em_icon_018,
            R.drawable.em_icon_019,
            R.drawable.em_icon_020,
            R.drawable.em_icon_021,
            R.drawable.em_icon_022,
            R.drawable.em_icon_024,
            R.drawable.em_icon_027,
            R.drawable.em_icon_029,
            R.drawable.em_icon_030,
            R.drawable.em_icon_035,
            R.drawable.em_icon_040,
    };

    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData() {
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for (int i = 0; i < icons.length; i++) {
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
            datas[i].setName("示例" + (i + 1));
            datas[i].setIdentityCode("em" + (1000 + i + 1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(R.drawable.ee_2);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }

    public static EaseEmojiconGroupEntity getData() {
        return DATA;
    }

}
