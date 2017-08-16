package com.example.sinmingu.login.Activitys;

import java.util.ArrayList;

/**
 * Created by sinmingu on 2017-08-01.
 */

public class Member {

    String user_name;
    String user_pw;
    String user_id;
    ArrayList<String> friend_list = new ArrayList<String>();

    public Member(){

    }

    public Member(String user_name, String user_id, String user_pw){
        this.user_name=user_name;
        this.user_id=user_id;
        this.user_pw=user_pw;
    }

    public String getUser_name(){
        return user_name;
    }

    public String getUser_id(){
        return user_id;
    }

    public String getUser_pw(){
        return user_pw;
    }

    public void add_friend_list(String id){
        friend_list.add(id);
    }

    public ArrayList getFriend_list(){
        return friend_list;
    }

    public void setFrined_list(ArrayList frined_list){
        this.friend_list=frined_list;
    }

    public void setUser_name(String user_name){
        this.user_name=user_name;
    }

    public void setUser_id(String user_id){
        this.user_id=user_id;
    }

    public void setUser_pw(String user_pw){
        this.user_pw=user_pw;
    }


}
