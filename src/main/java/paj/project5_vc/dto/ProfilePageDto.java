package paj.project5_vc.dto;

public class ProfilePageDto {

    private UserDto user;
    private int totalTasks;
    private int totalToDoTasks;
    private int totalDoingTasks;
    private int totalDoneTasks;

    public ProfilePageDto() {
    }

    public ProfilePageDto(UserDto user, int totalTasks, int totalToDoTasks, int totalDoingTasks, int totalDoneTasks) {
        this.user = user;
        this.totalTasks = totalTasks;
        this.totalToDoTasks = totalToDoTasks;
        this.totalDoingTasks = totalDoingTasks;
        this.totalDoneTasks = totalDoneTasks;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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
}
