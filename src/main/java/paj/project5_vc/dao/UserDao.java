package paj.project5_vc.dao;

import jakarta.persistence.Query;
import paj.project5_vc.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.enums.UserRole;

import java.util.ArrayList;

@Stateless
public class UserDao extends AbstractDao<UserEntity> {

    private static final long serialVersionUID = 1L;

    public UserDao() {
        super(UserEntity.class);
    }


    public UserEntity findUserByToken(String token) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByToken").setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity findUserByUsername(String username) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByUsername").setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity findUserByEmail(String email) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByEmail").setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity findUserById(int id) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserById")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ArrayList<UserEntity> findAllActiveUsernames() {
        try {
            // Apply pagination using the offset and pageSize parameters
            ArrayList<UserEntity> activeUsers = (ArrayList<UserEntity>) em.createNamedQuery("User.findAllActiveUsers")
                    .getResultList();
            return activeUsers;
        } catch (Exception e) {
            return null;
        }
    }


    public ArrayList<UserEntity> findAllActiveUsers(String order, int offset, int pageSize) {
        try {
            // Apply pagination using the offset and pageSize parameters
            ArrayList<UserEntity> activeUsers = (ArrayList<UserEntity>) em.createNamedQuery("User.findAllActiveUsers")
                    .setParameter("order", order)
                    .setFirstResult(offset) // Set the offset
                    .setMaxResults(pageSize) // Set the max number of results to fetch
                    .getResultList();
            return activeUsers;
        } catch (Exception e) {
            return null;
        }
    }

    // Method to count total active users
    public int findTotalUserCountbyActive() {
        try {
            Long count = (Long) em.createNamedQuery("User.findTotalActiveUserCount")
                    .getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            // Handle any exceptions
            return -1; // Or return any appropriate default value
        }
    }

    public int findTotalPagesActiveUserCount(int pageSize) {
        try {
            Long totalPages = (Long) em.createNamedQuery("User.findTotalPagesActiveUserCount")
                    .setParameter("pageSize", pageSize)
                    .getSingleResult();
            return totalPages != null ? totalPages.intValue() : 0;
        } catch (Exception e) {
            return 0; // Handle any exceptions appropriately
        }
    }

    public ArrayList<UserEntity> findAllDeletedUsers() {
        try {
            ArrayList<UserEntity> activeUsers = (ArrayList<UserEntity>) em.createNamedQuery("User.findAllDeletedUsers")
                    .getResultList();
            return activeUsers;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<UserEntity> findUsersByRole(UserRole role, String order, int offset, int pageSize) {
        try {
            // Convert the UserRole enum to int
            int intRole = role.getValue();
            ArrayList<UserEntity> usersByRole = (ArrayList<UserEntity>) em.createNamedQuery("User.findUsersByRole")
                    .setParameter("role", intRole)
                    .setParameter("order", order)
                    .setFirstResult(offset) // Set the offset
                    .setMaxResults(pageSize) // Set the max number of results to fetch
                    .getResultList();
            return usersByRole;
        } catch (Exception e) {
            return null;
        }
    }

    // Method to count total users by role
    public int findTotalUsersCountByRole(UserRole role) {
        try {
            int intRole = role.getValue();
            Long count = (Long) em.createNamedQuery("User.findTotalUsersCountByRole")
                    .setParameter("role", intRole)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            return 0; // Handle any exceptions appropriately
        }
    }

    public int findTotalPagesCountByRole(UserRole role, int pageSize) {
        try {
            int intRole = role.getValue();
            Long totalPages = (Long) em.createNamedQuery("User.findTotalPagesCountByRole")
                    .setParameter("role", intRole)
                    .setParameter("pageSize", pageSize)
                    .getSingleResult();
            return totalPages != null ? totalPages.intValue() : 0;
        } catch (Exception e) {
            return 0; // Handle any exceptions appropriately
        }
    }

}




