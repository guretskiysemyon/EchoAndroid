package com.echoexp4.Repositories;

import android.content.Context;

import com.echoexp4.Activities.ConnectionActivity;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.Requests.LogInRequest;
import com.echoexp4.Requests.SignUpRequest;
import com.echoexp4.api.UserAPI;

public class ConnectionRepository {
    private AppDB db;
    private UserAPI api;
    private ConnectionActivity activity;


    public ConnectionRepository(Context context, ConnectionActivity activity){
        this.activity = activity;
        this.db = AppDB.getDbInstance(context);
        this.api = new UserAPI(this);
    }

    public void LogIn(LogInRequest logInRequest){
        api.logIn(logInRequest);
    }

    public void SignUp(SignUpRequest request){
        api.SignUp(request);
    }

    //TODO: Change to User data from service
    public  void setUser(User user, String token){
        //TODO: Change here too
        db.clearAllTables();
        db.allDao().insertUser(user);
        this.activity.nextActivity();
    }

}
