package paj.project5_vc.dao;

import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.enums.TaskState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Stateless
public class TaskDao extends AbstractDao<TaskEntity> {

    private static final long serialVersionUID = 1L;

    public TaskDao() {
        super(TaskEntity.class);
    }

    public TaskEntity findTaskById(int id) {
        try {
            return (TaskEntity) em.createNamedQuery("Task.findTaskById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public TaskEntity findTaskByIdAndUser(int taskId, int userId) {
        try {
            return (TaskEntity) em.createNamedQuery("Task.findTaskByIdAndUser").setParameter("id", taskId).setParameter("creator", userId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public ArrayList<TaskEntity> findAllActiveTasks() {
        try {
            ArrayList<TaskEntity> taskEntityEntities = (ArrayList<TaskEntity>) em.createNamedQuery("Task.findAllActiveTasks").setParameter("deleted", false).getResultList();
            return taskEntityEntities;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<TaskEntity> findTasksByUser(UserEntity userEntity) {
        try {
            ArrayList<TaskEntity> taskEntityEntities = (ArrayList<TaskEntity>) em.createNamedQuery("Task.findTasksByUser").setParameter("creator", userEntity).setParameter("deleted", false).getResultList();
            return taskEntityEntities;
        } catch (Exception e) {
            return null;
        }
    }

    public int findTotalTasksByUser(String username) {
        try {
            return em.createNamedQuery("Task.findTotalTasksByUser", Long.class)
                    .setParameter("username", username)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException e) {
            return 0; // Return 0 if no tasks found for the username
        }
    }

    public int findTotalTasksByStateAndUser(String username, TaskState state) {
        try {
            return em.createNamedQuery("Task.findTotalTasksByStateAndUser", Long.class)
                    .setParameter("username", username)
                    .setParameter("state", state.getValue())
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public int countTotalTasks() {
        try {
            return em.createNamedQuery("Task.countTotalTasks", Long.class)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public ArrayList<TaskEntity> findTasksByDeleted() {
        try {
            ArrayList<TaskEntity> taskEntityEntities = (ArrayList<TaskEntity>) em.createNamedQuery("Task.findTasksByDeleted").setParameter("deleted", true).getResultList();
            return taskEntityEntities;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<TaskEntity> findTasksByCategoryId(int categoryId) {
        try {
            return (ArrayList<TaskEntity>) em.createNamedQuery("Task.findTasksByCategoryId")
                    .setParameter("categoryId", categoryId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Object[]> countTasksByCategoryOrderedByCount() {
        try {
            return em.createNamedQuery("Task.countTasksByCategoryOrderedByCount").getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return Collections.emptyList();
        }
    }

    public List<Object[]> countTasksByStatus() {
        try {
            return em.createNamedQuery("Task.countTasksByStatus")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return Collections.emptyList();
        }
    }

    public int countCompletedTasksByDate(LocalDate completedDate) {
        try {
            return em.createNamedQuery("Task.countCompletedTasksByDate", Long.class)
                    .setParameter("dateParam", completedDate)
                    .getSingleResult()
                    .intValue();
        } catch (NoResultException e) {
            return 0; // Return 0 if no tasks found for the completed date
        }
    }

    public double findAverageTaskDuration() {
        try {
            // Query to retrieve the average value of the task duration
            Double averageDuration = (Double) em.createNamedQuery("Task.findAverageTaskDuration")
                    .getSingleResult();
            return averageDuration != null ? averageDuration : 0.0; // Return 0.0 if no tasks exist or duration is null
        } catch (NoResultException e) {
            return 0.0; // Return 0.0 if no tasks exist
        }
    }

}
