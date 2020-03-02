package com.appsmata.qtoa.core;

public interface SearchResultListener<T> {
	void onSelected(BaseSearchDialogCompat dialog, T item, int position);
}
