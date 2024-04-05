package paj.project5_vc.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.entity.MessageEntity;
import java.sql.Timestamp;
import paj.project5_vc.entity.UserEntity;
import java.util.ArrayList;


@Stateless
public class MessageDao extends AbstractDao<MessageEntity> {

    private static final long serialVersionUID = 1L;

    public MessageDao() {
        super(MessageEntity.class);
    }

    // Method to find a message by its ID using named query
    public MessageEntity findById(int messageId) {
        try {
            return em.createNamedQuery("Message.findById", MessageEntity.class)
                    .setParameter("messageId", messageId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to find previous messages based on sentTime
    public ArrayList<MessageEntity> findPreviousMessages(Timestamp sentTime) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findPreviousMessages")
                    .setParameter("sentTime", sentTime)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<MessageEntity> findMessagesForUser(UserEntity user) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findMessagesForUser")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    // Method to find messages for a specific user
    public ArrayList<MessageEntity> findMessagesFromUser(UserEntity user) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findMessagesFromUser")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ArrayList<MessageEntity> findAllUserMessages(UserEntity user) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findAllUserMessages")
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to find messages changed between two users (sent messages)
    public ArrayList<MessageEntity> findChangedMessages(UserEntity senderUser, UserEntity receiverUser) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findChangedMessages")
                    .setParameter("sender", senderUser)
                    .setParameter("receiver", receiverUser)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}