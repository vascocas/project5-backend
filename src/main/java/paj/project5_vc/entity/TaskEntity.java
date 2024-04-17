package paj.project5_vc.entity;

import paj.project5_vc.enums.TaskPriority;
import paj.project5_vc.enums.TaskState;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "task")
@NamedQuery(name = "Task.findAllActiveTasks", query = "SELECT t FROM TaskEntity t WHERE t.deleted = :deleted ORDER BY t.priority DESC, t.startDate, t.endDate")
@NamedQuery(name = "Task.findTaskById", query = "SELECT t FROM TaskEntity t WHERE t.id = :id")
@NamedQuery(name = "Task.findTaskByIdAndUser", query = "SELECT t FROM TaskEntity t WHERE t.id = :id AND t.creator.id = :creator")
@NamedQuery(name = "Task.findTasksByUser", query = "SELECT t FROM TaskEntity t WHERE t.creator = :creator AND t.deleted = :deleted")
@NamedQuery(name = "Task.findTasksByCategoryId", query = "SELECT t FROM TaskEntity t WHERE t.category.id = :categoryId")
@NamedQuery(name = "Task.findTasksByDeleted", query = "SELECT t FROM TaskEntity t WHERE t.deleted = :deleted ORDER BY t.id")
@NamedQuery(name = "Task.countTasksByCategory", query = "SELECT COUNT(t) FROM TaskEntity t WHERE t.category.id = :categoryId AND t.deleted = false")
@NamedQuery(name = "Task.countTasksByCategoryOrderedByCount", query = "SELECT t.category.id, COUNT(t) AS taskCount FROM TaskEntity t WHERE t.deleted = false GROUP BY t.category.id ORDER BY taskCount DESC")
@NamedQuery(name = "Task.findTotalTasksByUser", query = "SELECT COUNT(t) FROM TaskEntity t WHERE t.creator.username = :username AND t.deleted = false")
@NamedQuery(name = "Task.findTotalTasksByStateAndUser", query = "SELECT COUNT(t) FROM TaskEntity t WHERE t.creator.username = :username AND t.state = :state AND t.deleted = false")
@NamedQuery(name = "Task.countTasksByStatus", query = "SELECT t.state, COUNT(t) AS taskCount FROM TaskEntity t WHERE t.deleted = false GROUP BY t.state")
@NamedQuery(name = "Task.countTotalTasks", query = "SELECT COUNT(t) FROM TaskEntity t WHERE t.deleted = false")
@NamedQuery(name = "Task.countCompletedTasksByDate", query = "SELECT COUNT(t) FROM TaskEntity t WHERE t.deleted = false AND t.completedDate = :dateParam")
@NamedQuery(name = "Task.findAverageTaskDuration", query = "SELECT AVG(t.duration) FROM TaskEntity t WHERE t.duration IS NOT NULL")


public class TaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, length = 65500, columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "duration")
    private int duration;

    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    //Owning Side User - Task
    @ManyToOne
    private UserEntity creator;

    //Owning Side Category - Task
    @ManyToOne
    private CategoryEntity category;

    public TaskEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TaskState getState() {
        return TaskState.fromValue(this.state);
    }

    public void setState(TaskState state) {
        this.state = state.getValue();
    }

    public TaskPriority getPriority() {
        return TaskPriority.fromValue(this.priority);
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority.getValue();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}