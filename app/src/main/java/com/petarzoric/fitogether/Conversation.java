package com.petarzoric.fitogether;

/**
 * Created by petarzoric on 18.01.18.
 */
    /*
        Conversation Klasse, wird im entsprechenden ViewHolder benötigt.
        Speichert Zeit der Nachricht und ob sie bereits gesehen wurde, oder nicht.
    */
public class Conversation {

    public boolean seen;
    public long timestamp;

    public Conversation(){

    }

    public Conversation(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
