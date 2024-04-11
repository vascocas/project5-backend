package paj.project5_vc.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;


@Entity
@Table(name = "message")
@NamedQuery(name = "Message.findById", query = "SELECT m FROM MessageEntity m WHERE m.id = :messageId")
@NamedQuery(name = "Message.findPreviousMessages", query = "SELECT m FROM MessageEntity m WHERE m.sentTime <= :sentTime AND m.chatId = :chatId")
@NamedQuery(name = "Message.findMessagesForUser", query = "SELECT m FROM MessageEntity m WHERE m.receiver = :user ORDER BY m.sentTime")
@NamedQuery(name = "Message.findMessagesFromUser", query = "SELECT m FROM MessageEntity m WHERE m.sender = :user ORDER BY m.sentTime")
@NamedQuery(name = "Message.findAllUserMessages", query = "SELECT m FROM MessageEntity m WHERE m.sender = :user OR m.receiver = :user ORDER BY m.sentTime")
@NamedQuery(name = "Message.findChangedMessages", query = "SELECT m FROM MessageEntity m WHERE ( m.sender = :sender AND m.receiver = :receiver) OR (m.sender = :receiver AND m.receiver = :sender) ORDER BY m.sentTime")
@NamedQuery(name = "Message.findAllMessagesByChatId", query = "SELECT m FROM MessageEntity m WHERE m.chatId = :chatId ORDER BY m.sentTime")



public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private Timestamp sentTime;

    @Column(name = "message_text", nullable = false, updatable = false)
    private String messageText;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    @Column(name = "chat_id", nullable = false)
    private int chatId;

    //Owning Side User Sender - Message
    @ManyToOne
    private UserEntity sender;

    //Owning Side User Receiver - Message
    @ManyToOne
    private UserEntity receiver;

    public MessageEntity() {
    }

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

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }
}
