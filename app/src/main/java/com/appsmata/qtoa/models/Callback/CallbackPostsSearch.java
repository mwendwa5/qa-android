package com.appsmata.qtoa.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.models.PostsSearch;

public class CallbackPostsSearch implements Serializable {
    public List<PostsSearch> data = new ArrayList<PostsSearch>();
}
