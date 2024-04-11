package paj.project5_vc.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dao.MessageDao;
import paj.project5_vc.dao.NotificationDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.dto.NotificationDto;
import paj.project5_vc.entity.MessageEntity;
import paj.project5_vc.entity.NotificationEntity;
import paj.project5_vc.entity.UserEntity;
import paj.project5_vc.websocket.MessageWeb;
import paj.project5_vc.websocket.NotificationWeb;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
public class MessageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(MessageBean.class);

    @EJB
    MessageDao messageDao;
    @EJB
    NotificationDao notificationDao;
    @EJB
    UserDao userDao;

    @Inject
    MessageWeb messageWeb;

    @Inject
    NotificationWeb notifWeb;


    // Method for sending a message
    public boolean sendMessage(String token, MessageDto messageDto) {
        UserEntity sender = userDao.findUserById(messageDto.getSenderId());
        UserEntity receiver = userDao.findUserById(messageDto.getReceiverId());
        if (sender != null && receiver != null) {
            // Persist the message for chat senderId
            MessageEntity message = new MessageEntity();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageText(messageDto.getMessageText());
            message.setReadStatus(false);
            message.setChatId(messageDto.getSenderId());
            messageDao.persist(message);

            // Create and persist notification
            NotificationEntity notif = new NotificationEntity();
            notif.setRecipientUser(receiver);
            notif.setContentText("New message from: " + sender.getUsername());
            notif.setReadStatus(false);
            notificationDao.persist(notif);

            // Persist the message for chat senderId
            MessageEntity returnEntity = new MessageEntity();
            returnEntity.setReadStatus(false);
            returnEntity.setMessageText(messageDto.getMessageText());
            returnEntity.setSender(sender);
            returnEntity.setReceiver(receiver);
            returnEntity.setChatId(messageDto.getReceiverId());
            messageDao.persist(returnEntity);

            // Prepare return message DTO
            MessageDto returnDto = new MessageDto();
            returnDto.setReadStatus(returnEntity.isReadStatus());
            returnDto.setMessageText(returnEntity.getMessageText());
            returnDto.setReceiverId(returnEntity.getReceiver().getId());
            returnDto.setSenderId(returnEntity.getSender().getId());
            returnDto.setId(returnEntity.getId());
            returnDto.setChatId(returnEntity.getChatId());

            // Convert message DTO to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson;
            try {
                messageJson = objectMapper.writeValueAsString(returnDto);
                logger.warn("Message webSocket sent successfully");
            } catch (JsonProcessingException e) {
                logger.error("Error converting message DTO to JSON", e);
                return false;
            }

            // Send message over WebSocket
            messageWeb.send(token, messageJson);

            // Prepare return notification DTO
            NotificationDto notifDto = new NotificationDto();
            notifDto.setReadStatus(notif.isReadStatus());
            notifDto.setCreationTime(notif.getCreationTime());
            notifDto.setRecipientId(notif.getRecipientUser().getId());
            notifDto.setContentText(notif.getContentText());
            notifDto.setId(notif.getId());

            // Convert notification DTO to JSON
            String notificationJson;
            try {
                notificationJson = objectMapper.writeValueAsString(notifDto);
                logger.warn("Notification webSocket sent successfully");
            } catch (JsonProcessingException e) {
                logger.error("Error converting notification DTO to JSON", e);
                return false;
            }

            // Send notification over WebSocket
            notifWeb.send(token, notificationJson);

            logger.warn("Message sent successfully");
            return true;
        }
        return false;
    }

    // Method for marking a message as read
    public boolean markMessageAsRead(String token, int messageId) {
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            MessageEntity message = messageDao.findById(messageId);
            if (message != null) {
                // Fetch all previous messages based on sentTime
                ArrayList<MessageEntity> previousMessages = messageDao.findPreviousMessages(user.getId(), message.getSentTime());
                // Mark all previous messages as read
                for (MessageEntity prevMessage : previousMessages) {
                    prevMessage.setReadStatus(true);
                }
                return true;
            }
        }
        return false;
    }

    // Method for fetching messages for a user
    public ArrayList<MessageDto> getReceivedUserMessages(int userId) {
        // Retrieve the user entity corresponding to the userId
        UserEntity user = userDao.findUserById(userId);
        if (user != null) {
            // Retrieve messages for the user from the database
            ArrayList<MessageEntity> messageEntities = messageDao.findMessagesForUser(user);
            // Convert MessageEntity objects to MessageDto objects
            ArrayList<MessageDto> messages = convertMessagesFromEntityListToDtoList(messageEntities);
            return messages;
        } else {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }
    }

    // Method for fetching messages from a user
    public ArrayList<MessageDto> getSentUserMessages(int userId) {
        // Retrieve the user entity corresponding to the userId
        UserEntity user = userDao.findUserById(userId);
        if (user != null) {
            // Retrieve messages for the user from the database
            ArrayList<MessageEntity> messageEntities = messageDao.findMessagesFromUser(user);
            // Convert MessageEntity objects to MessageDto objects
            ArrayList<MessageDto> messages = convertMessagesFromEntityListToDtoList(messageEntities);
            return messages;
        } else {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }
    }

    // Method for fetching messages all user messages
    public ArrayList<MessageDto> getAllUserMessages(int userId) {
        // Retrieve the user entity corresponding to the userId
        UserEntity user = userDao.findUserById(userId);
        if (user != null) {
            // Retrieve messages for the user from the database
            ArrayList<MessageEntity> messageEntities = messageDao.findAllUserMessages(user);
            // Convert MessageEntity objects to MessageDto objects
            ArrayList<MessageDto> messages = convertMessagesFromEntityListToDtoList(messageEntities);
            return messages;
        } else {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }
    }

    // Method for fetching chat messages
    public ArrayList<MessageDto> getChatMessages(int senderId) {
        ArrayList<MessageEntity> messageEntities = messageDao.findAllMessagesByChatId(senderId);
        // Convert MessageEntity objects to MessageDto objects
        ArrayList<MessageDto> messages = convertMessagesFromEntityListToDtoList(messageEntities);
        return messages;
    }

    // Method to convert MessageEntity to MessageDto
    private MessageDto convertMessageEntityToDto(MessageEntity messageEntity) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(messageEntity.getId());
        messageDto.setMessageText(messageEntity.getMessageText());
        messageDto.setSentTime(messageEntity.getSentTime());
        messageDto.setSenderId(messageEntity.getSender().getId());
        messageDto.setReceiverId(messageEntity.getReceiver().getId());
        messageDto.setReadStatus(messageEntity.isReadStatus());
        messageDto.setChatId(messageEntity.getChatId());
        return messageDto;
    }

    // Method to convert ArrayList<MessageEntity> to ArrayList<MessageDto>
    private ArrayList<MessageDto> convertMessagesFromEntityListToDtoList(ArrayList<MessageEntity> messageEntities) {
        ArrayList<MessageDto> msgDtos = new ArrayList<>();
        for (MessageEntity m : messageEntities) {
            msgDtos.add(convertMessageEntityToDto(m));
        }
        return msgDtos;
    }

}
