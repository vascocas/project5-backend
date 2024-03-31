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
    private String email;

    public UserManagmentDto() {
    }

    public UserManagmentDto(int id, String username, UserRole role, String email) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
