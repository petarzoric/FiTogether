package com.petarzoric.fitogether;

import java.util.HashMap;

/**
 * Created by petarzoric on 13.12.17.
 */

class ProfileParser {

    public static HashMap<String, Object> parseToHashmap(UserProfile profile){
        HashMap<String, Object> dataBaseObject= new HashMap<>();
        dataBaseObject.put("UID", profile.getUid());
        dataBaseObject.put("email", profile.getEmail());
        dataBaseObject.put("name", profile.getName());
        dataBaseObject.put("age", profile.getAge());
        dataBaseObject.put("level", profile.getLevel().toString());
        dataBaseObject.put("studio", profile.getStudio());
        dataBaseObject.put("location", profile.getLocation());
        dataBaseObject.put("gender", profile.getGender().toString());
        dataBaseObject.put("image", profile.getImage());
        dataBaseObject.put("thumbnail", profile.getThumbnail());
        dataBaseObject.put("status", profile.getStatus());

        return dataBaseObject;
    }

    public static UserProfile parseToProfile(HashMap<String, Object> hashMap){
        UserProfile profile = new UserProfile();
        profile.setEmail((String) hashMap.get("email"));
        profile.setUid((String) hashMap.get("UID"));
        profile.setName((String) hashMap.get("name"));
        profile.setAge((int) hashMap.get("age"));
        profile.setLevel(Level.parseToEnum((String) hashMap.get("level")));
        profile.setStudio((int) hashMap.get("studio"));
        profile.setLocation((int) hashMap.get("location"));
        profile.setGender(Gender.parseToEnum((String) hashMap.get("gender")));
        profile.setImage((String) hashMap.get("image"));
        profile.setThumbnail((String) hashMap.get("thumbnail"));
        profile.setStatus((String) hashMap.get("status"));

        return profile;

    }


}