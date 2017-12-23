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
    String key;

    public UserTraining(String date, int trainingstype, String user, int level, int studio, int location, String time, String key) {
        this.date = date;
        this.trainingstype = trainingstype;
        this.user = user;
        this.level = level;
        this.studio = studio;
        this.location = location;
        this.time = time;
        this.key = key;
    }

    public UserTraining() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
