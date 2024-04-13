package com.teeniv.whatsapp.Models;

public class Users {

    String profilepic, username, mail, password, userid, lastmessage, status;

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public Users(String profilepic, String username, String mail, String password, String userid, String lastmessage, String status)
    {
        this.password = password;
        this.lastmessage = lastmessage;
        this.profilepic = profilepic;
        this.userid = userid;
        this.username = username;
        this.mail = mail;
        this.status = status;
    }

    public Users(){}

    //signUp Constructors
    public Users(String username, String mail, String password)
    {
        this.password = password;
        this.username = username;
        this.mail = mail;
    }
}
