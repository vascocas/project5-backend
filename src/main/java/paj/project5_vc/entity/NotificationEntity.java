package paj.project5_vc.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "notification")
@NamedQuery(name = "Notification.findById", query = "SELECT n FROM NotificationEntity n WHERE n.id = :notificationId")
@NamedQuery(name = "Notification.findPreviousNotifications", query = "SELECT n FROM NotificationEntity n WHERE n.creationTime <= :creationTime")
@NamedQuery(name = "Notification.findUserNotifications", query = "SELECT n FROM NotificationEntity n WHERE n.recipientUser = :recipient")

public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp creationTime;

    @Column(name = "content_text", nullable = false, updatable = false)
    private String contentText;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    //Owning Side recipient User - Notification
    @ManyToOne
    private UserEntity recipientUser;

    public NotificationEntity() {
    }

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

    public UserEntity getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(UserEntity recipientUser) {
        this.recipientUser = recipientUser;
    }
}
