package com.appsmata.qtoa.core;

import android.widget.Filter;

import java.util.ArrayList;

public abstract class BaseFilter<T> extends Filter {
	private OnPerformFilterListener mOnPerformFilterListener;
	private ArrayList<T> mItems;
	private FilterResultListener mFilterResultListener;
	
	public OnPerformFilterListener getOnPerformFilterListener() {
		return mOnPerformFilterListener;
	}
	
	public BaseFilter setOnPerformFilterListener(OnPerformFilterListener onPerformFilterListener) {
		mOnPerformFilterListener = onPerformFilterListener;
		return this;
	}
	
	public void doAfterFiltering() {
		if (mOnPerformFilterListener != null) {
			mOnPerformFilterListener.doAfterFiltering();
		}
	}
	
	public void doBeforeFiltering() {
		if (mOnPerformFilterListener != null) {
			mOnPerformFilterListener.doBeforeFiltering();
		}
	}
	
	public ArrayList<T> getItems() {
		return mItems;
	}
	
	public BaseFilter setItems(ArrayList<T> items) {
		mItems = items;
		return this;
	}
	
	public FilterResultListener getFilterResultListener() {
		return mFilterResultListener;
	}
	
	public BaseFilter setFilterResultListener(FilterResultListener filterResultListener) {
		mFilterResultListener = filterResultListener;
		return this;
	}
}
