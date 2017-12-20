package cc.pchospital.toolbox.util;

public class Ticket {
    private String ticketId;
    private String userId;
    private String ticketName;
    private String ticketPhone;
    private Long ticketDate;
    private String ticketNote;
    private String ticketStates;
    private String ticketLocation;
    private Double ticketLocationLo;
    private Double ticketLocationLa;
    private String staffId;
    private String[] pictures;

    public Ticket(Card card) {
        ticketId = card.getTicketID();
        ticketLocation = card.getTicketLocation();
        ticketDate = card.getTicketDate();
        ticketStates = card.getTicketStates();
        ticketNote = card.getTicketNote();
    }

    public Ticket(){}

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketPhone() {
        return ticketPhone;
    }

    public void setTicketPhone(String ticketPhone) {
        this.ticketPhone = ticketPhone;
    }

    public Long getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Long ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getTicketNote() {
        return ticketNote;
    }

    public void setTicketNote(String ticketNote) {
        this.ticketNote = ticketNote;
    }

    public String getTicketStates() {
        return ticketStates;
    }

    public void setTicketStates(String ticketStates) {
        this.ticketStates = ticketStates;
    }

    public String getTicketLocation() {
        return ticketLocation;
    }

    public void setTicketLocation(String ticketLocation) {
        this.ticketLocation = ticketLocation;
    }

    public Double getTicketLocationLo() {
        if (ticketLocationLo == null) {
            ticketLocationLo = 0.0;
        }
        return ticketLocationLo;
    }

    public void setTicketLocationLo(Double ticketLocationLo) {
        this.ticketLocationLo = ticketLocationLo;
    }

    public Double getTicketLocationLa() {
        if (ticketLocationLa == null) {
            ticketLocationLa = 0.0;
        }
        return ticketLocationLa;
    }

    public void setTicketLocationLa(Double ticketLocationLa) {
        this.ticketLocationLa = ticketLocationLa;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }
}