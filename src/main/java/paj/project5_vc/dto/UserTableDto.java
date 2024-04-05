package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
public class UserTableDto {
    @XmlElement
    private ArrayList<UserManagmentDto> users;
    @XmlElement
    private int totalItems;
    @XmlElement
    private int totalPages;
    @XmlElement
    private int currentPage;
    @XmlElement
    private int pageSize;

    public UserTableDto() {
    }

    public UserTableDto(ArrayList<UserManagmentDto> users, int totalItems, int totalPages, int currentPage, int pageSize) {
        this.users = users;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters and setters
    public ArrayList<UserManagmentDto> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserManagmentDto> users) {
        this.users = users;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
