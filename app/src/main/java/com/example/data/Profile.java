package com.example.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Profile {

    public String email;
    public String url;

public  Profile(){

}
    /*public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }*/

    public  Profile(String email, String url){
        this.email = email;
        this.url = url;
    }
}

