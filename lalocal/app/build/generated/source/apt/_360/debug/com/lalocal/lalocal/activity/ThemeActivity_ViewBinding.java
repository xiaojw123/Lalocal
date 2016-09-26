// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ThemeActivity_ViewBinding<T extends ThemeActivity> implements Unbinder {
  protected T target;

  public ThemeActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.mPtrFrame = Utils.findRequiredViewAsType(source, R.id.ptr_frame_layout, "field 'mPtrFrame'", PtrClassicFrameLayout.class);
    target.mRvTheme = Utils.findRequiredViewAsType(source, R.id.rv_theme, "field 'mRvTheme'", RecyclerView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mPtrFrame = null;
    target.mRvTheme = null;

    this.target = null;
  }
}
