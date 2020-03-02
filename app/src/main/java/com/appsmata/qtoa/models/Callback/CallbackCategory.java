package com.appsmata.qtoa.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.models.CategoryModel;

public class CallbackCategory implements Serializable {
    public List<CategoryModel> data = new ArrayList<CategoryModel>();
}
