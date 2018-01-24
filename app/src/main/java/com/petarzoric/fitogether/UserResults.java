package com.petarzoric.fitogether;

/**
 * Created by Alex on 24.01.2018.
 */



    public class UserResults {
        String uid;
        String name;
        int age;
        Level level;
        int studio;
        int location;
        Gender gender;
        String thumbnail;
    String time;


        public UserResults(String uid,String name,int age, Level userlevel, int studio, int location, Gender gender, String turl, String time) {
            this.uid = uid;
            this.name = name;
            this.age = age;
            this.level = userlevel;
            this.studio = studio;
            this.location = location;
            this.gender = gender;
            this.thumbnail = turl;
            this.time = time;
        }

        public UserResults(){

        }

        //GETTER UND SETTER



        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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


        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbURL) {
            this.thumbnail = thumbURL;
        }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}




