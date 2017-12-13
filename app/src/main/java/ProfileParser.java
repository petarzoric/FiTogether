import com.petarzoric.fitogether.UserProfile;

import java.util.HashMap;

/**
 * Created by petarzoric on 13.12.17.
 */

public class ProfileParser {

    public static HashMap<String, Object> parse(UserProfile profile){
        HashMap<String, Object> dataBaseObject= new HashMap<>();
        dataBaseObject.put("UID", profile.getUid());
        dataBaseObject.put("eMail", profile.getEmail());
        dataBaseObject.put("name", profile.getEmail());
        dataBaseObject.put("age", profile.getAge());
        dataBaseObject.put("level", profile.getLevel().toString());
        dataBaseObject.put("studio", profile.getStudio());
        dataBaseObject.put("studio location", profile.getLocation());
        dataBaseObject.put("gender", profile.getGender().toString());
        dataBaseObject.put("image", profile.getImageURL());
        dataBaseObject.put("thumbnail", profile.getThumbURL());

        return dataBaseObject;
    }
}
