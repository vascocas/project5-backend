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

    // Method to find previous chat messages based on sentTime
    public ArrayList<MessageEntity> findPreviousChatMessages(int senderId, int receiverId, Timestamp sentTime) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findPreviousChatMessages")
                    .setParameter("senderId", senderId)
                    .setParameter("receiverId", receiverId)
                    .setParameter("sentTime", sentTime)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    // Method to find messages changed between two users (sent/received messages)
    public ArrayList<MessageEntity> findChangedMessages(int senderId, int receiverId) {
        try {
            return (ArrayList<MessageEntity>) em.createNamedQuery("Message.findChangedMessages")
                    .setParameter("senderId", senderId)
                    .setParameter("receiverId", receiverId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}