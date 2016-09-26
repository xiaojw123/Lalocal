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

public class LiveSearchAdapter$LiveSearchViewHolder_ViewBinding<T extends LiveSearchAdapter.LiveSearchViewHolder> implements Unbinder {
  protected T target;

  public LiveSearchAdapter$LiveSearchViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.photoImg = Utils.findRequiredViewAsType(source, R.id.live_search_item_photo, "field 'photoImg'", ImageView.class);
    target.avatarImg = Utils.findRequiredViewAsType(source, R.id.live_search_item_useravatar, "field 'avatarImg'", CircleImageView.class);
    target.titleTv = Utils.findRequiredViewAsType(source, R.id.live_search_item_title, "field 'titleTv'", TextView.class);
    target.onlinUserNumTv = Utils.findRequiredViewAsType(source, R.id.live_search_onlinUser_num, "field 'onlinUserNumTv'", TextView.class);
    target.userNameTv = Utils.findRequiredViewAsType(source, R.id.live_search_item_username, "field 'userNameTv'", TextView.class);
    target.locTv = Utils.findRequiredViewAsType(source, R.id.live_search_item_loc, "field 'locTv'", TextView.class);
    target.lin1 = Utils.findRequiredView(source, R.id.live_search_lin1, "field 'lin1'");
    target.lin2 = Utils.findRequiredView(source, R.id.live_search_lin2, "field 'lin2'");
    target.meetingTagLayout = Utils.findRequiredViewAsType(source, R.id.live_search_item_meeting_tag, "field 'meetingTagLayout'", LinearLayout.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.photoImg = null;
    target.avatarImg = null;
    target.titleTv = null;
    target.onlinUserNumTv = null;
    target.userNameTv = null;
    target.locTv = null;
    target.lin1 = null;
    target.lin2 = null;
    target.meetingTagLayout = null;

    this.target = null;
  }
}
