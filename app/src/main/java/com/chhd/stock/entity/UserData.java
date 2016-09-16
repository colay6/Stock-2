package com.chhd.stock.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by CWQ on 2016/7/23.
 */
public class UserData extends BmobObject {

    private User user;
    private String stockID;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }
}
