package paj.project5_vc.dto;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;

@XmlRootElement
public class MessageDto {

    @XmlElement
    private int id;
    @XmlElement
    private String messageText;
    @XmlElement
    private Timestamp sentTime;
    @XmlElement
    private boolean readStatus;
    @XmlElement
    private int senderId;
    @XmlElement
    private int receiverId;

    // Default constructor
    public MessageDto() {
    }

    // Constructor with parameters
    public MessageDto(int id, String messageText, Timestamp sentTime, boolean readStatus, int senderId, int receiverId) {
        this.id = id;
        this.messageText = messageText;
        this.sentTime = sentTime;
        this.readStatus = readStatus;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}


