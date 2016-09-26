// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.live.entertainment.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.xlistview.XListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LiveAttentionOrFansActivity_ViewBinding<T extends LiveAttentionOrFansActivity> implements Unbinder {
  protected T target;

  private View view2131624593;

  private View view2131624595;

  private View view2131624596;

  private View view2131624597;

  public LiveAttentionOrFansActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.userAttentionTitle = Utils.findRequiredViewAsType(source, R.id.user_attention_title, "field 'userAttentionTitle'", CustomTitleView.class);
    view = Utils.findRequiredView(source, R.id.live_attention_search_et, "field 'liveAttentionSearchEt' and method 'clickButton'");
    target.liveAttentionSearchEt = Utils.castView(view, R.id.live_attention_search_et, "field 'liveAttentionSearchEt'", EditText.class);
    view2131624593 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.seach_clear_btn, "field 'searchClearBtn' and method 'clickButton'");
    target.searchClearBtn = Utils.castView(view, R.id.seach_clear_btn, "field 'searchClearBtn'", ImageView.class);
    view2131624595 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.live_attention_search_cancel, "field 'liveAttentionSearchCancel' and method 'clickButton'");
    target.liveAttentionSearchCancel = Utils.castView(view, R.id.live_attention_search_cancel, "field 'liveAttentionSearchCancel'", TextView.class);
    view2131624596 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    target.searchTextHint = Utils.findRequiredViewAsType(source, R.id.search_text_hint, "field 'searchTextHint'", TextView.class);
    target.liveAttentionListview = Utils.findRequiredViewAsType(source, R.id.live_attention_listview, "field 'liveAttentionListview'", XListView.class);
    target.liveSearchLayout = Utils.findRequiredViewAsType(source, R.id.live_search_layout_to, "field 'liveSearchLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.live_search_layout_font, "field 'liveSearchLayoutFont' and method 'clickButton'");
    target.liveSearchLayoutFont = Utils.castView(view, R.id.live_search_layout_font, "field 'liveSearchLayoutFont'", LinearLayout.class);
    view2131624597 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    target.searchResultNull = Utils.findRequiredViewAsType(source, R.id.search_result_null, "field 'searchResultNull'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.userAttentionTitle = null;
    target.liveAttentionSearchEt = null;
    target.searchClearBtn = null;
    target.liveAttentionSearchCancel = null;
    target.searchTextHint = null;
    target.liveAttentionListview = null;
    target.liveSearchLayout = null;
    target.liveSearchLayoutFont = null;
    target.searchResultNull = null;

    view2131624593.setOnClickListener(null);
    view2131624593 = null;
    view2131624595.setOnClickListener(null);
    view2131624595 = null;
    view2131624596.setOnClickListener(null);
    view2131624596 = null;
    view2131624597.setOnClickListener(null);
    view2131624597 = null;

    this.target = null;
  }
}
