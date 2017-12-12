package com.petarzoric.fitogether;

/**
 * Created by petarzoric on 12.12.17.
 */

public enum Gender {
    MÄNNLICH,
    WEIBLICH,
    NOTDEFINED;

    public static Gender parseToEnum(String input){
        switch (input){
            case "maennlich":
            case "männlich":
            case "Männlich":
            case "Maennlich":
            case "MÄNNLICH":
            case "MAENNLICH":
                return Gender.MÄNNLICH;

            case "weiblich":
            case "WEIBLICH":
            case "Weiblich":
                return Gender.WEIBLICH;

            case "anderes":
            case "Anderes":
            case "ANDERES":
                return Gender.NOTDEFINED;

            default:
                return Gender.NOTDEFINED;
        }
    }
}
