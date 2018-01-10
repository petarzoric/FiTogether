package com.petarzoric.fitogether;

/**
 * Created by Alex on 19.12.2017.
 */

public class UserTraining {
    String date;
    int trainingstype;
    String user;
    int level;
    int studio;
    int location;
    String time;


    public UserTraining(String date, int trainingstype, String user, int level, int studio, int location, String time) {
        this.date = date;
        this.trainingstype = trainingstype;
        this.user = user;
        this.level = level;
        this.studio = studio;
        this.location = location;
        this.time = time;

    }

    public UserTraining() {
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTrainingstype() {
        return trainingstype;
    }

    public void setTrainingstype(int trainingstype) {
        this.trainingstype = trainingstype;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStudio() {
        return studio;
    }

    public void setStudio(int studio) {
        this.studio = studio;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
