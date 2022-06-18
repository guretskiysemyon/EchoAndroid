package com.echoexp4.Repositories;

import android.content.Context;

import com.echoexp4.Database.AppDB;
import com.echoexp4.api.UserAPI;

public class AppRepository {
    private AppDB db;
    private UserAPI api;

    public AppRepository(Context context, UserAPI api) {
        this.db = AppDB.getDbInstance(context);
        this.api = api;
    }





}
