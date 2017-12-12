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
    int gender;
    String imageURL;
    String thumbURL;

    public UserProfile(String email,String name,int age,   int userlevel, int studio, int location, int gender, String iurl, String turl ) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.userlevel = userlevel;
        this.studio = studio;
        this.location = location;
        this.gender = gender;
        this.imageURL = iurl;
        this.thumbURL = turl;
    }

    public UserProfile(){
        this.email = "default";
        this.name = "default";
        this.age = -1;
        this.userlevel = -1;
        this.location = -1;
        this.gender = -1;
        this.imageURL = "default";
        this.thumbURL = "default";

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

    //GETTER

    public int getUserlevel() {
        return userlevel;
    }

    public int getStudio() {
        return studio;
    }

    public int getLocation() {
        return location;
    }

    public int getGender() {
        return gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    //SETTER


    public void setUserlevel(int userlevel) {
        this.userlevel = userlevel;
    }

    public void setStudio(int studio) {
        this.studio = studio;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }
}


