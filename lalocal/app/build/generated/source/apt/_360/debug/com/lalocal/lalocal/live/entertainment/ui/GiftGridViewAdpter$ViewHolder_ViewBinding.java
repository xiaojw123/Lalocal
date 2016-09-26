// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.live.entertainment.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GiftGridViewAdpter$ViewHolder_ViewBinding<T extends GiftGridViewAdpter.ViewHolder> implements Unbinder {
  protected T target;

  public GiftGridViewAdpter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.giftSendCount = Utils.findRequiredViewAsType(source, R.id.gift_send_count, "field 'giftSendCount'", TextView.class);
    target.giftPhotoIv = Utils.findRequiredViewAsType(source, R.id.gift_photo_iv, "field 'giftPhotoIv'", ImageView.class);
    target.giftPriceTv = Utils.findRequiredViewAsType(source, R.id.gift_price_tv, "field 'giftPriceTv'", TextView.class);
    target.giftItemBg = Utils.findRequiredViewAsType(source, R.id.gift_item_bg, "field 'giftItemBg'", LinearLayout.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.giftSendCount = null;
    target.giftPhotoIv = null;
    target.giftPriceTv = null;
    target.giftItemBg = null;

    this.target = null;
  }
}
