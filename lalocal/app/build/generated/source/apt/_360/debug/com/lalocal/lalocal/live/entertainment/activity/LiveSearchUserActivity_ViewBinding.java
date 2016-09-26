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
import com.lalocal.lalocal.view.xlistview.XListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LiveSearchUserActivity_ViewBinding<T extends LiveSearchUserActivity> implements Unbinder {
  protected T target;

  private View view2131624593;

  private View view2131624595;

  private View view2131624596;

  public LiveSearchUserActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.live_attention_search_et, "field 'liveAttentionSearchEt' and method 'clickButton'");
    target.liveAttentionSearchEt = Utils.castView(view, R.id.live_attention_search_et, "field 'liveAttentionSearchEt'", EditText.class);
    view2131624593 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    target.searchTextHint = Utils.findRequiredViewAsType(source, R.id.search_text_hint, "field 'searchTextHint'", TextView.class);
    view = Utils.findRequiredView(source, R.id.seach_clear_btn, "field 'seachClearBtn' and method 'clickButton'");
    target.seachClearBtn = Utils.castView(view, R.id.seach_clear_btn, "field 'seachClearBtn'", ImageView.class);
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
    target.liveSearchLayoutTo = Utils.findRequiredViewAsType(source, R.id.live_search_layout_to, "field 'liveSearchLayoutTo'", LinearLayout.class);
    target.searchResultNull = Utils.findRequiredViewAsType(source, R.id.search_result_null, "field 'searchResultNull'", TextView.class);
    target.liveAttentionListview = Utils.findRequiredViewAsType(source, R.id.live_attention_listview, "field 'liveAttentionListview'", XListView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.liveAttentionSearchEt = null;
    target.searchTextHint = null;
    target.seachClearBtn = null;
    target.liveAttentionSearchCancel = null;
    target.liveSearchLayoutTo = null;
    target.searchResultNull = null;
    target.liveAttentionListview = null;

    view2131624593.setOnClickListener(null);
    view2131624593 = null;
    view2131624595.setOnClickListener(null);
    view2131624595 = null;
    view2131624596.setOnClickListener(null);
    view2131624596 = null;

    this.target = null;
  }
}
