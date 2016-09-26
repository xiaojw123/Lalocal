// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.live.entertainment.ui;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GiftRankViewAdapter$ViewHolder_ViewBinding<T extends GiftRankViewAdapter.ViewHolder> implements Unbinder {
  protected T target;

  public GiftRankViewAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.liveGiftsRankingHead = Utils.findRequiredViewAsType(source, R.id.live_gifts_ranking_head, "field 'liveGiftsRankingHead'", CircleImageView.class);
    target.liveGiftsRankingName = Utils.findRequiredViewAsType(source, R.id.live_gifts_ranking_name, "field 'liveGiftsRankingName'", TextView.class);
    target.liveGiftsRankingCount = Utils.findRequiredViewAsType(source, R.id.live_gifts_ranking_count, "field 'liveGiftsRankingCount'", TextView.class);
    target.liveGiftsRankingNum = Utils.findRequiredViewAsType(source, R.id.live_gifts_ranking_num, "field 'liveGiftsRankingNum'", TextView.class);
    target.liveGiftRankItemLayout = Utils.findRequiredViewAsType(source, R.id.live_gift_rank_item_layout, "field 'liveGiftRankItemLayout'", RelativeLayout.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.liveGiftsRankingHead = null;
    target.liveGiftsRankingName = null;
    target.liveGiftsRankingCount = null;
    target.liveGiftsRankingNum = null;
    target.liveGiftRankItemLayout = null;

    this.target = null;
  }
}
