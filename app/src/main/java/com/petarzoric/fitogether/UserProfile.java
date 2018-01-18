package com.petarzoric.fitogether;

/**
 * Created by Alex on 20.11.2017.
 */

public class UserProfile {
    String uid;
    String email;
    String name;
    int age;
    Level level;
    int studio;
    int location;
    Gender gender;
    String image;
    String thumbnail;
    String status;


    public UserProfile(String uid,String email,String name,int age,   Level userlevel, int studio, int location, Gender gender, String iurl, String turl, String status ) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.age = age;
        this.level = userlevel;
        this.studio = studio;
        this.location = location;
        this.gender = gender;
        this.image = iurl;
        this.thumbnail = turl;
        this.status = status;
    }

    public UserProfile(){

    }

    //GETTER UND SETTER

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String imageURL) {
        this.image = imageURL;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbURL) {
        this.thumbnail = thumbURL;
    }
}


