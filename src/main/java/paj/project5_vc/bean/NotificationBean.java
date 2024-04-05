package paj.project5_vc.bean;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import paj.project5_vc.dao.NotificationDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.NotificationDto;
import paj.project5_vc.entity.NotificationEntity;
import paj.project5_vc.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;

@Stateless
public class NotificationBean implements Serializable {

    @EJB
    NotificationDao notificationDao;

    @EJB
    UserDao userDao;

    // Method for fetching chat messages
    public ArrayList<NotificationDto> getUserNotifications(int userId) {
        // Retrieve the user entity corresponding to the id
        UserEntity recipient = userDao.findUserById(userId);
        if (recipient != null) {
            ArrayList<NotificationEntity> notifEntities = notificationDao.findUserNotifications(recipient);
            // Convert NotificationEntity objects to MessageDto objects
            ArrayList<NotificationDto> notifications = convertNotifFromEntityListToDtoList(notifEntities);
            return notifications;
        } else {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }
    }

    // Method for marking a notification as read
    public boolean markNotificationAsRead(int notificationId) {
        NotificationEntity notification = notificationDao.findById(notificationId);
        if (notification != null) {
            // Fetch all previous notifications based on created Time
            ArrayList<NotificationEntity> previousNotifs = notificationDao.findPreviousNotifications(notification.getCreationTime());
            // Mark all previous notifications as read
            for (NotificationEntity prevNotification : previousNotifs) {
                prevNotification.setReadStatus(true);
            }
            return true;
        } else {
            return false;
        }
    }

    // Method for sending a notification
    public boolean sendNotification(NotificationDto notificationDto) {
        UserEntity recipient = userDao.findUserById(notificationDto.getRecipientId());
        if (recipient != null) {
            NotificationEntity notif = new NotificationEntity();
            notif.setRecipientUser(recipient);
            notif.setContentText(notificationDto.getContentText());
            notif.setReadStatus(false);
            notificationDao.persist(notif);
            return true;
        }
        return false;
    }

    // Method to convert NotificationEntity to NotificationDto
    private NotificationDto convertNotificationEntityToDto(NotificationEntity notifEntity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notifEntity.getId());
        notificationDto.setContentText(notifEntity.getContentText());
        notificationDto.setCreationTime(notifEntity.getCreationTime());
        notificationDto.setRecipientId(notifEntity.getRecipientUser().getId());
        notificationDto.setReadStatus(notifEntity.isReadStatus());
        return notificationDto;
    }

    // Method to convert ArrayList<NotificationEntity> to ArrayList<NotificationDto>
    private ArrayList<NotificationDto> convertNotifFromEntityListToDtoList(ArrayList<NotificationEntity> notifEntities) {
        ArrayList<NotificationDto> notifDtos = new ArrayList<>();
        for (NotificationEntity n : notifEntities) {
            notifDtos.add(convertNotificationEntityToDto(n));
        }
        return notifDtos;
    }
}
