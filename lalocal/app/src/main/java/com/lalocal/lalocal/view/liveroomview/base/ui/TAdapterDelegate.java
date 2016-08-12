package com.lalocal.lalocal.view.liveroomview.base.ui;

public interface TAdapterDelegate {

	public int getViewTypeCount();

	public Class<? extends TViewHolder> viewHolderAtPosition(int position);

	public boolean enabled(int position);
}