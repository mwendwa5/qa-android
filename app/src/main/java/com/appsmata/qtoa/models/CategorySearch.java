package com.appsmata.qtoa.models;

import com.appsmata.qtoa.core.Searchable;

public class CategorySearch implements Searchable {
    private String Title, CatID;

    public CategorySearch(String title, String catid) {
        Title = title;
        CatID = catid;
    }

    public String getTitle() {
        return Title;
    }

    public CategorySearch setName(String title) {
        Title = title;
        return this;
    }

    public String getCatID() {
        return CatID;
    }

    public CategorySearch setCatID(String catid) {
        CatID = catid;
        return this;
    }
}
