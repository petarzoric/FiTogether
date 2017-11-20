package com.petarzoric.fitogether;

/**
 * Created by Alex on 20.11.2017.
 */

public class UserProfile {
    String email;
    String name;
    int age;
    int userlevel;
    int studio;
    int location;

    public UserProfile(String email,String name,int age,   int userlevel, int studio, int location) {
        this.age = age;
        this.name = name;
        this.email = email;
        this.userlevel = userlevel;
        this.studio = studio;
        this.location = location;
    }

    public UserProfile() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public int getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(int userlevel) {
        this.userlevel = userlevel;
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
