package com.lelangkita.android.resources;

/**
 * Created by Andre on 11/19/2016.
 */

public class UserProfile {
    private String userProfileMenu, userProfileMenuDesc;
    public UserProfile(){

    }
    public UserProfile(String menu, String menuDesc){
        this.userProfileMenu = menu;
        this.userProfileMenuDesc = menuDesc;
    }
    public String getUserProfileMenu(){
        return userProfileMenu;
    }
    public String getUserProfileMenuDesc(){
        return userProfileMenuDesc;
    }
    public void setUserProfileMenu(String input){
        userProfileMenu = input;
    }
    public void setUserProfileMenuDesc(String input){
        userProfileMenuDesc = input;
    }
}
