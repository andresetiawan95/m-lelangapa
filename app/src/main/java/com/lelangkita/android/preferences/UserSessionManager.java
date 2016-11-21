package com.lelangkita.android.preferences;

/**
 * Created by Andre on 11/12/2016.
 */

public class UserSessionManager {
    private String id;
    private String username;
    private String name;
    private String email;
    public UserSessionManager(){}
    public UserSessionManager (String id, String username, String name, String email){
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }
    public void setId(String id){ this.id = id; }
    public void setUsername(String username){
        this.username = username;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getId() { return id; }
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
