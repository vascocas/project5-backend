package paj.project5_vc.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.entity.NotificationEntity;
import paj.project5_vc.entity.UserEntity;

import java.sql.Timestamp;
import java.util.ArrayList;


@Stateless
public class NotificationDao extends AbstractDao<NotificationEntity> {

    private static final long serialVersionUID = 1L;

    public NotificationDao() {
        super(NotificationEntity.class);
    }

    // Method to find a notification by its ID
    public NotificationEntity findById(int notificationId) {
        try {
            return em.createNamedQuery("Notification.findById", NotificationEntity.class)
                    .setParameter("notificationId", notificationId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to find previous notifications based on creationTime
    public ArrayList<NotificationEntity> findPreviousNotifications(Timestamp creationTime) {
        try {
            return (ArrayList<NotificationEntity>) em.createNamedQuery("Notification.findPreviousNotifications")
                    .setParameter("creationTime", creationTime)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    // Method to find All user notifications
    public ArrayList<NotificationEntity> findAllUserNotifications(UserEntity recipient) {
        try {
            return (ArrayList<NotificationEntity>) em.createNamedQuery("Notification.findUserNotifications")
                    .setParameter("recipient", recipient)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    // Method to find unread user notifications
    public ArrayList<NotificationEntity> findUnreadUserNotifications(UserEntity recipient) {
        try {
            return (ArrayList<NotificationEntity>) em.createNamedQuery("Notification.findUnreadUserNotifications")
                    .setParameter("recipient", recipient)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
