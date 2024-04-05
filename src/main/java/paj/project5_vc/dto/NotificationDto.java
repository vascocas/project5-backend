package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;

@XmlRootElement
public class NotificationDto {

    @XmlElement
    private int id;
    @XmlElement
    private Timestamp creationTime;
    @XmlElement
    private String contentText;
    @XmlElement
    private boolean readStatus;
    @XmlElement
    private int recipientId;

    // Default constructor
    public NotificationDto() {
    }

    // Constructor with parameters

    public NotificationDto(int id, Timestamp creationTime, String contentText, boolean readStatus, int recipientId) {
        this.id = id;
        this.creationTime = creationTime;
        this.contentText = contentText;
        this.readStatus = readStatus;
        this.recipientId = recipientId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }
}
