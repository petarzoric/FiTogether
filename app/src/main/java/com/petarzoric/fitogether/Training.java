package com.petarzoric.fitogether;



public class Training {
    String trainingstype;
    int level;
    int studio;
    int location;

    public Training(String trainingstype, int level, int studio, int location) {
        this.trainingstype = trainingstype;
        this.level = level;
        this.studio = studio;
        this.location = location;
    }

    public Training() {
    }

    public String getTrainingstype() {
        return trainingstype;
    }

    public void setTrainingstype(String trainingstype) {
        this.trainingstype = trainingstype;
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
}
