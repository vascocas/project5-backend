package paj.project5_vc.dao;

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

    public ArrayList<UserEntity> findAllActiveUsers() {
        try {
            ArrayList<UserEntity> activeUsers = (ArrayList<UserEntity>) em.createNamedQuery("User.findAllActiveUsers")
                    .getResultList();
            return activeUsers;
        } catch (Exception e) {
            return null;
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

    public ArrayList<UserEntity> findUsersByRole(String role, String order) {
        try {
            // Convert the role String to UserRole enum
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            ArrayList<UserEntity> usersByRole = (ArrayList<UserEntity>) em.createNamedQuery("User.findUsersByRole")
                    .setParameter("role", userRole)
                    .setParameter("order", order)
                    .getResultList();
            return usersByRole;
        } catch (Exception e) {
            return null;
        }
    }

}




