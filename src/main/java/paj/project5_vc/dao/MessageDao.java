package paj.project5_vc.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.entity.MessageEntity;
import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.TokenEntity;
import paj.project5_vc.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

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

}