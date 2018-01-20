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

    public static int parseToInt(Gender gender){
        switch (gender){
            case MÄNNLICH:
                return 0;

            case WEIBLICH:
                return 1;

            case NOTDEFINED:
                return 2;

        }
        return 2;

    }
    public static String parseToString(Gender gender){
        switch (gender){
            case MÄNNLICH:
                return "MÄNNLICH";

            case WEIBLICH:
                return "WEIBLICH";

            case NOTDEFINED:
                return "ANDERES";
        }
        return "ANDERES";
    }

    public static Gender parseToEnum(int input){
        switch (input){
            case 0:
                return Gender.MÄNNLICH;

            case 1:
                return Gender.WEIBLICH;

            case 2:
                return Gender.NOTDEFINED;





        }
        return Gender.NOTDEFINED;

    }


}
