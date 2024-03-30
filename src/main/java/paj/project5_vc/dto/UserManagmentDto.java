package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import paj.project5_vc.enums.UserRole;

@XmlRootElement
public class UserManagmentDto {


    @XmlElement
    private int id;
    @XmlElement
    private String username;
    @XmlElement
    private UserRole role;
    @XmlElement
    private boolean deleted;

    public UserManagmentDto() {
    }

    public UserManagmentDto(int id, String username, UserRole role, boolean deleted) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
