package com.petarzoric.fitogether;

/**
 * Created by petarzoric on 16.01.18.
 */
/*
    Model Klasse für den ViewHolder in der FriendsView
    Siehe FriendsActivity.
    Macht nicht besonders viel, speichert lediglich, seit wann man
    miteinander befreundet ist.
 */

public class Friends {

    public String date;

    public Friends(){


    }

    public Friends(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setData(String data) {
        this.date = data;
    }
}
