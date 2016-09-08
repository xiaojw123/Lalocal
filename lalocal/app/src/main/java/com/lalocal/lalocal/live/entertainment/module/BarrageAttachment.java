package com.lalocal.lalocal.live.entertainment.module;

import com.alibaba.fastjson.JSONObject;

/**
 * 弹幕
 * Created by hzxuwen on 2016/3/30.
 */
public class BarrageAttachment extends CustomAttachment {
    public BarrageAttachment() {
        super(CustomAttachmentType.barrage);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
