package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement
public class PublicProfileDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private int userId;
    @XmlElement
    private String username;
    @XmlElement
    private String email;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private String photo;
    @XmlElement
    private int totalTasks;
    @XmlElement
    private int totalToDoTasks;
    @XmlElement
    private int totalDoingTasks;
    @XmlElement
    private int totalDoneTasks;

    public PublicProfileDto() {
    }

    public PublicProfileDto(int userId, String username, String email, String firstName, String lastName, String photo, int totalTasks, int totalToDoTasks, int totalDoingTasks, int totalDoneTasks) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.totalTasks = totalTasks;
        this.totalToDoTasks = totalToDoTasks;
        this.totalDoingTasks = totalDoingTasks;
        this.totalDoneTasks = totalDoneTasks;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTotalToDoTasks() {
        return totalToDoTasks;
    }

    public void setTotalToDoTasks(int totalToDoTasks) {
        this.totalToDoTasks = totalToDoTasks;
    }

    public int getTotalDoingTasks() {
        return totalDoingTasks;
    }

    public void setTotalDoingTasks(int totalDoingTasks) {
        this.totalDoingTasks = totalDoingTasks;
    }

    public int getTotalDoneTasks() {
        return totalDoneTasks;
    }

    public void setTotalDoneTasks(int totalDoneTasks) {
        this.totalDoneTasks = totalDoneTasks;
    }

    @Override
    public String toString() {
        return "PublicProfileDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", totalTasks=" + totalTasks +
                ", totalToDoTasks=" + totalToDoTasks +
                ", totalDoingTasks=" + totalDoingTasks +
                ", totalDoneTasks=" + totalDoneTasks +
                '}';
    }
}
