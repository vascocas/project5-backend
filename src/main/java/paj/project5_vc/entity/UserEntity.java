package paj.project5_vc.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import paj.project5_vc.enums.UserRole;

@Entity
@Table(name = "user")
@NamedQuery(name = "User.findUserByUsername", query = "SELECT u FROM UserEntity u WHERE u.username = :username AND u.deleted = false")
@NamedQuery(name = "User.findUserByToken", query = "SELECT u FROM UserEntity u JOIN u.tokens t WHERE t.tokenValue = :token AND u.deleted = false")
@NamedQuery(name = "User.findUserByValidationToken", query = "SELECT u FROM UserEntity u WHERE u.validationToken = :validationToken")
@NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email AND u.deleted = false")
@NamedQuery(name = "User.findUserById", query = "SELECT u FROM UserEntity u WHERE u.id = :id")
@NamedQuery(name = "User.findAllActiveUsernames", query = "SELECT u FROM UserEntity u WHERE u.deleted = false ORDER BY u.username")
@NamedQuery(name = "User.findAllActiveUsers", query = "SELECT u FROM UserEntity u WHERE u.deleted = false ORDER BY CASE WHEN :order = 'ASC' THEN u.id END ASC, CASE WHEN :order = 'DESC' THEN u.id END DESC")
@NamedQuery(name = "User.findAllDeletedUsers", query = "SELECT u FROM UserEntity u WHERE u.deleted = true ORDER BY u.id")
@NamedQuery(name = "User.findUsersByRole", query = "SELECT u FROM UserEntity u WHERE u.role = :role AND u.deleted = false ORDER BY CASE WHEN :order = 'ASC' THEN u.id END ASC, CASE WHEN :order = 'DESC' THEN u.id END DESC")
@NamedQuery(name = "User.findTotalUserCount", query = "SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false")
@NamedQuery(name = "User.findTotalValidatedUserCount", query = "SELECT COUNT(u) FROM UserEntity u WHERE u.validated = true AND u.deleted = false")
@NamedQuery(name = "User.findTotalNonValidatedUserCount", query = "SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false AND u.validated = false")
@NamedQuery(name = "User.findTotalPagesActiveUserCount", query = "SELECT FUNCTION('CEIL', COUNT(u) / :pageSize) FROM UserEntity u WHERE u.deleted = false")
@NamedQuery(name = "User.findTotalUsersCountByRole", query = "SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false AND u.role = :role")
@NamedQuery(name = "User.findTotalPagesCountByRole", query = "SELECT FUNCTION('CEIL', COUNT(u) / :pageSize) FROM UserEntity u WHERE u.deleted = false AND u.role = :role")
@NamedQuery(name = "User.findTotalUsersCountByValidatedAt", query = "SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false AND DAY(u.registerAt) = DAY(:registerAtParam)")



public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "profile_photo")
    private String photo;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "validated", nullable = false)
    private boolean validated;

    @Column(name = "validation_token", unique = true)
    private String validationToken;

    @CreationTimestamp
    @Column(name = "register_at", nullable = false)
    private Timestamp registerAt;

    @Column(name = "role", nullable = false)
    private int role;

    @OneToMany(mappedBy = "creator")
    private Set<TaskEntity> tasks;

    @OneToMany(mappedBy = "user")
    private Set<TokenEntity> tokens;

    @OneToMany(mappedBy = "sender")
    private Set<MessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private Set<MessageEntity> receivedMessages;

    @OneToMany(mappedBy = "recipientUser")
    private Set<NotificationEntity> inNotifications;


    //default empty constructor
    public UserEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(String validationToken) {
        this.validationToken = validationToken;
    }

    public Timestamp getRegisterAt() {
        return registerAt;
    }

    public void setRegisterAt(Timestamp registerAt) {
        this.registerAt = registerAt;
    }

    public Set<TokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(Set<TokenEntity> tokens) {
        this.tokens = tokens;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public UserRole getRole() {
        return UserRole.valueOf(this.role);
    }

    public void setRole(UserRole role) {
        this.role = role.getValue();
    }

    public Set<MessageEntity> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<MessageEntity> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<MessageEntity> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<MessageEntity> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<NotificationEntity> getInNotifications() {
        return inNotifications;
    }

    public void setInNotifications(Set<NotificationEntity> inNotifications) {
        this.inNotifications = inNotifications;
    }
}