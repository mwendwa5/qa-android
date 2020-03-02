package com.appsmata.qtoa.models.Callback;

import com.appsmata.qtoa.models.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CallbackUsersLists implements Serializable{
    public int total = -1;
    public List<UserModel> data = new ArrayList<UserModel>();
}
