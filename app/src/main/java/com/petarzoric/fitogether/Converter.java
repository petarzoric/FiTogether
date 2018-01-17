package com.petarzoric.fitogether;

import android.app.Fragment;
import android.content.res.Resources;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Alex on 17.01.2018.
 */

public class Converter extends Fragment {
    public Converter() {
    }

    public static String monthConverter(int month) {
        String monthString = "";
        if (month == 1) {
            monthString = "Januar";
        }
        if (month == 2) {
            monthString = "Februar";
        }
        if (month == 3) {
            monthString = "März";
        }
        if (month == 4) {
            monthString = "April";
        }
        if (month == 5) {
            monthString = "Mai";
        }
        if (month == 6) {
            monthString = "Juni";
        }
        if (month == 7) {
            monthString = "Juli";
        }
        if (month == 8) {
            monthString = "August";
        }
        if (month == 9) {
            monthString = "September";
        }
        if (month == 10) {
            monthString = "Oktober";
        }
        if (month == 11) {
            monthString = "November";
        }
        if (month == 12) {
            monthString = "Dezember";
        }
        return monthString;
    }

    public static String studioString(int studio, int loc, Resources res) {
        String[] studios = res.getStringArray(R.array.Studio);
        String[] location;
        String studioText = "";
        if (studio == 0) {
            location = res.getStringArray(R.array.LocationFITSTAR);
            studioText = studios[studio] + " " + location[loc];

        } else if (studio == 1) {
            location = res.getStringArray(R.array.LocationFitnessFirst);
            studioText = studios[studio] + " " + location[loc];

        } else if (studio == 2) {
            location = res.getStringArray(R.array.LocationBodyandSoul);
            studioText = studios[studio] + " " + location[loc];

        } else if (studio == 3) {
            location = res.getStringArray(R.array.LocationMcFit);
            studioText = studios[studio] + " " + location[loc];

        } else if (studio == 4) {
            location = res.getStringArray(R.array.LocationCleverFit);
            studioText = studios[studio] + " " + location[loc];

        } else if (studio == 5) {
            location = res.getStringArray(R.array.LocationAndere);
            studioText = location[loc];

        }
        return studioText;

    }

    /*public static UserProfile getUserProfile(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        UserProfile userProfile = databaseReference.child("Users2").child(uid).;
        databaseReference.child("Users2").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }*/
}
