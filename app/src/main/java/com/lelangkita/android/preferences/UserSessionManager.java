package com.lelangkita.android.preferences;

/**
 * Created by Andre on 11/12/2016.
 */

public class UserSessionManager {
    private String username;
    private String name;
    private String email;
    public UserSessionManager(){}
    public UserSessionManager (String username, String name, String email){
        this.username = username;
        this.name = name;
        this.email = email;
    }
    public void setUsername(){
        this.username = username;
    }
    public void setName(){
        this.name = name;
    }
    public void setEmail(){
        this.email = email;
    }
    public String getUsername(){
        return username;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
}
