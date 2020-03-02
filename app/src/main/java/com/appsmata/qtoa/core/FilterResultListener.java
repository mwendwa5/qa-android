package com.appsmata.qtoa.core;

import java.util.ArrayList;

public interface FilterResultListener<T> {
	void onFilter(ArrayList<T> items);
}
