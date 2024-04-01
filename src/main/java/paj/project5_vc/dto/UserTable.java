package paj.project5_vc.dto;

import java.util.ArrayList;

public class UserTable {
    private ArrayList<UserManagmentDto> users;
    private int totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public UserTable(ArrayList<UserManagmentDto> users, int totalItems, int totalPages, int currentPage, int pageSize) {
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
