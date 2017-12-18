package cc.pchospital.toolbox.util;


import android.support.annotation.NonNull;

public class Card {
    private String ticketID;
    private Long ticketDate;
    private String ticketLocation;
    private String ticketNote;
    private String ticketStates;

    public Card(int id, Long date, String location, String note, String states){
        ticketID = Integer.toString(id);
        ticketDate = date;
        ticketLocation = location;
        ticketNote = note;
        ticketStates = states;
    }

    String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    Long getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Long ticketDate) {
        this.ticketDate = ticketDate;
    }

    @NonNull
    String getTicketLocation() {
        if (ticketLocation == null){
            ticketLocation = "";
        }
        return ticketLocation;
    }

    public void setTicketLocation(String ticketLocation) {
        this.ticketLocation = ticketLocation;
    }

    @NonNull
    String getTicketNote() {
        if (ticketNote == null){
            ticketNote = "";
        }
        return ticketNote;
    }

    public void setTicketNote(String ticketNote) {
        this.ticketNote = ticketNote;
    }

    String getTicketStates() {
        return ticketStates;
    }

    public void setTicketStates(String ticketStates) {
        this.ticketStates = ticketStates;
    }
}
