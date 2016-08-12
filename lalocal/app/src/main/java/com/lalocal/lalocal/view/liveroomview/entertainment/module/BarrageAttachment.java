package com.lalocal.lalocal.view.liveroomview.entertainment.module;

import com.alibaba.fastjson.JSONObject;

/**
 * 点赞附件
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
