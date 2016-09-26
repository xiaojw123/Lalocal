// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LiveMainAdapter$LiveViewHodler_ViewBinding<T extends LiveMainAdapter.LiveViewHodler> implements Unbinder {
  protected T target;

  public LiveMainAdapter$LiveViewHodler_ViewBinding(T target, View source) {
    this.target = target;

    target.liveStatus = Utils.findRequiredViewAsType(source, R.id.live_status, "field 'liveStatus'", TextView.class);
    target.liveTheme = Utils.findRequiredViewAsType(source, R.id.live_theme, "field 'liveTheme'", TextView.class);
    target.liveOnlineCountTv = Utils.findRequiredViewAsType(source, R.id.live_online_count_tv, "field 'liveOnlineCountTv'", TextView.class);
    target.liveCompereLocation = Utils.findRequiredViewAsType(source, R.id.live_compere_location, "field 'liveCompereLocation'", TextView.class);
    target.livePeopleLayout = Utils.findRequiredViewAsType(source, R.id.live_people_layout, "field 'livePeopleLayout'", LinearLayout.class);
    target.liveCompereHeadPortrait = Utils.findRequiredViewAsType(source, R.id.live_compere_head_portrait, "field 'liveCompereHeadPortrait'", CircleImageView.class);
    target.liveCompereHeadTv = Utils.findRequiredViewAsType(source, R.id.live_compere_head_tv, "field 'liveCompereHeadTv'", TextView.class);
    target.liveCoverIv = Utils.findRequiredViewAsType(source, R.id.live_cover_iv, "field 'liveCoverIv'", ImageView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.liveStatus = null;
    target.liveTheme = null;
    target.liveOnlineCountTv = null;
    target.liveCompereLocation = null;
    target.livePeopleLayout = null;
    target.liveCompereHeadPortrait = null;
    target.liveCompereHeadTv = null;
    target.liveCoverIv = null;

    this.target = null;
  }
}
