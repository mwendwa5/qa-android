package com.appsmata.qtoa.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appsmata.qtoa.models.Comment;

public class CallbackShowComment implements Serializable {
    public List<Comment> data = new ArrayList<Comment>();
}
