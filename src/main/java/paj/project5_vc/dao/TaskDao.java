package paj.project5_vc.dao;

import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import paj.project5_vc.enums.TaskState;

import java.util.ArrayList;

@Stateless
public class TaskDao extends AbstractDao<TaskEntity> {

    private static final long serialVersionUID = 1L;

    @EJB
    private CategoryDao categoryDao;

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

    public Double getAverageTasksPerUser(int creatorId) {
        try {
            return (Double) em.createNamedQuery("Task.findAverageTasksPerUser")
                    .setParameter("creatorId", creatorId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<TaskEntity> countTasksByStatus() {
        try {
            return (ArrayList<TaskEntity>) em.createNamedQuery("Task.countTasksByStatus")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
