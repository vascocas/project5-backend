package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import paj.project5_vc.enums.UserRole;

@XmlRootElement
public class UserManagmentDto {


    @XmlElement
    private int userId;
    @XmlElement
    private String username;
    @XmlElement
    private UserRole role;
    @XmlElement
    private boolean deleted;

    public UserManagmentDto() {
    }

    public UserManagmentDto(int userId, String username, UserRole role, boolean deleted) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.deleted = deleted;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
