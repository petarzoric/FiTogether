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
    String imageURL;
    String thumbURL;
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
        this.imageURL = iurl;
        this.thumbURL = turl;
        this.status = status;
    }

 /*   public UserProfile(String uid){
        this.uid = uid;
        this.email = "default";
        this.name = "default";
        this.age = -1;
        this.level = Level.ANFÄNGER;
        this.studio = -1;
        this.location = -1;
        this.gender = Gender.NOTDEFINED;
        this.imageURL = "default";
        this.thumbURL = "default";
        this.status = "Hi, I am using FiTogether";

    }
*/
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }
}


