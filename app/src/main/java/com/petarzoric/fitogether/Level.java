package com.petarzoric.fitogether;

/**
 * Created by petarzoric on 12.12.17.
 */

public enum Level {
    ANFÄNGER,
    FORTGESCHRITTEN,
    PROFI,
    ARNOLD;

    public static Level parseToEnum(String input){
        switch (input){
            case "Anfänger":
            case "ANFÄNGER":
            case "anfänger":
            case "Anfaenger":
            case "ANFAENGER":
            case "anfaenger":
                return Level.ANFÄNGER;

            case "FORTGESCHRITTEN":
            case "Fortgeschritten":
            case "fortgeschritten":
                return Level.FORTGESCHRITTEN;

            case "Profi":
            case "PROFI":
            case "profi":
                return Level.PROFI;

            case "Arnold":
            case "ARNOLD":
            case "arnold":
                return Level.ARNOLD;

            default:
                System.out.print("DEFAULT CASE IN LEVEL");
                return Level.ARNOLD;

        }


    }

    public static int parseToInt(Level level) {
        switch (level) {
            case ANFÄNGER:
                return 0;
            case FORTGESCHRITTEN:
                return 1;
            case PROFI:
                return 2;
            case ARNOLD:
                return 3;



        }
        return 0;
    }

    public static Level parseToEnum(int input){
        switch (input){
            case 0:
                return Level.ARNOLD;

            case 1:
                return Level.FORTGESCHRITTEN;

            case 2:
                return Level.PROFI;

            case 3:
                return Level.ARNOLD;
        }
        return Level.ANFÄNGER;

    }
}