package com.petarzoric.fitogether;

/**
 * Created by petarzoric on 31.01.18.
 */

public class Request {

    public String requestMessage;
    public String date;
    public String from;
    String thumb;

    public Request(){

    }

    public Request(String requestMessage, String date, String from, String thumb) {
        this.requestMessage = requestMessage;
        this.date = date;
        this.from = from;
        this.thumb = thumb;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
