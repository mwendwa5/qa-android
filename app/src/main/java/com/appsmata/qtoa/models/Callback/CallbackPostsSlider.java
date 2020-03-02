package com.appsmata.qtoa.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.models.PostsSlider;

public class CallbackPostsSlider implements Serializable {
    public List<PostsSlider> data = new ArrayList<PostsSlider>();
}
