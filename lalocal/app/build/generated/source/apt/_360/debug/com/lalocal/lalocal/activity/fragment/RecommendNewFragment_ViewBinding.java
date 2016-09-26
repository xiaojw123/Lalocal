// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity.fragment;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.MyRecyclerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecommendNewFragment_ViewBinding<T extends RecommendNewFragment> implements Unbinder {
  protected T target;

  public RecommendNewFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycleview_home_recomend, "field 'mRecyclerView'", MyRecyclerView.class);
    target.mPtrLayout = Utils.findRequiredViewAsType(source, R.id.ptr_layout, "field 'mPtrLayout'", MyPtrClassicFrameLayout.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mRecyclerView = null;
    target.mPtrLayout = null;

    this.target = null;
  }
}
