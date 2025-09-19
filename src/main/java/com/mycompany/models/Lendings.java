
package com.mycompany.models;

public class Lendings {

    private int id;
    private int user_id;
    private int dsfr_id;
    private String date_out;
    private String date_return;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDsfr_id(int dsfr_id) {
        this.dsfr_id = dsfr_id;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public void setDate_return(String date_return) {
        this.date_return = date_return;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDsfr_id() {
        return dsfr_id;
    }

    public String getDate_out() {
        return date_out;
    }

    public String getDate_return() {
        return date_return;
    }
}
