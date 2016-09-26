// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.live.entertainment.adapter;

import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TouristAdapter$LiveViewHodler_ViewBinding<T extends TouristAdapter.LiveViewHodler> implements Unbinder {
  protected T target;

  public TouristAdapter$LiveViewHodler_ViewBinding(T target, View source) {
    this.target = target;

    target.touristItem = Utils.findRequiredViewAsType(source, R.id.tourist_item, "field 'touristItem'", CircleImageView.class);
    target.managerMark = Utils.findRequiredViewAsType(source, R.id.live_item_manager_mark, "field 'managerMark'", ImageView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.touristItem = null;
    target.managerMark = null;

    this.target = null;
  }
}
