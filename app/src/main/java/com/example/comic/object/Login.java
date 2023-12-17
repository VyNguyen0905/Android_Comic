package com.example.comic.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Login {
    private String email, password;

    public Login(JSONObject o) throws JSONException {
        email = o.getString("Email");
        password = o.getString("PassWord");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
