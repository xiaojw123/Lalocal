// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.ClearEditText;
import com.lalocal.lalocal.view.xlistview.XListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchActivity_ViewBinding<T extends SearchActivity> implements Unbinder {
  protected T target;

  private View view2131624991;

  private View view2131624992;

  public SearchActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.searchKeyCet = Utils.findRequiredViewAsType(source, R.id.search_key_cet, "field 'searchKeyCet'", ClearEditText.class);
    target.seachKeyHint = Utils.findRequiredViewAsType(source, R.id.seach_key_hint, "field 'seachKeyHint'", TextView.class);
    target.searchKeyContainer = Utils.findRequiredViewAsType(source, R.id.search_key_container, "field 'searchKeyContainer'", FrameLayout.class);
    target.searchHintRlv = Utils.findRequiredViewAsType(source, R.id.search_hint_rlv, "field 'searchHintRlv'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.search_key_cancel, "field 'searchKeyCancel' and method 'cancel'");
    target.searchKeyCancel = Utils.castView(view, R.id.search_key_cancel, "field 'searchKeyCancel'", TextView.class);
    view2131624991 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.cancel();
      }
    });
    target.searchTagRlv = Utils.findRequiredViewAsType(source, R.id.search_tag_rlv, "field 'searchTagRlv'", RecyclerView.class);
    target.searchResultRlv = Utils.findRequiredViewAsType(source, R.id.search_result_rlv, "field 'searchResultRlv'", RecyclerView.class);
    target.searchResultMoreXlv = Utils.findRequiredViewAsType(source, R.id.search_result_xlv, "field 'searchResultMoreXlv'", XListView.class);
    view = Utils.findRequiredView(source, R.id.search_back_img, "field 'searchBackImg' and method 'back'");
    target.searchBackImg = Utils.castView(view, R.id.search_back_img, "field 'searchBackImg'", ImageView.class);
    view2131624992 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.searchKeyCet = null;
    target.seachKeyHint = null;
    target.searchKeyContainer = null;
    target.searchHintRlv = null;
    target.searchKeyCancel = null;
    target.searchTagRlv = null;
    target.searchResultRlv = null;
    target.searchResultMoreXlv = null;
    target.searchBackImg = null;

    view2131624991.setOnClickListener(null);
    view2131624991 = null;
    view2131624992.setOnClickListener(null);
    view2131624992 = null;

    this.target = null;
  }
}
