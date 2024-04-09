package paj.project5_vc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import paj.project5_vc.dao.MessageDao;
import paj.project5_vc.dao.NotificationDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.entity.MessageEntity;
import paj.project5_vc.entity.NotificationEntity;
import paj.project5_vc.entity.UserEntity;
import paj.project5_vc.websocket.MessageWeb;
import paj.project5_vc.websocket.NotificationWeb;

@Stateless
public class MessageBean implements Serializable {
    private static final long serialVersionUID = 1L;

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
            MessageEntity message = new MessageEntity();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageText(messageDto.getMessageText());
            message.setReadStatus(false);
            messageDao.persist(message);
            // Serialize MessageDto to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage;
            try {
                jsonMessage = objectMapper.writeValueAsString(messageDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle or log exception
                return false; // Unable to serialize messageDto
            }
            // Send message over WebSocket
            messageWeb.send(token, jsonMessage);
            NotificationEntity notif = new NotificationEntity();
            notif.setRecipientUser(receiver);
            notif.setContentText("New message from: <" + sender.getUsername()+">");
            notif.setReadStatus(false);
            notificationDao.persist(notif);

            // Serialize MessageDto to JSON string
            ObjectMapper objMapper = new ObjectMapper();
            String jsMessage;
            try {
                jsMessage = objMapper.writeValueAsString(notif);
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle or log exception
                return false; // Unable to serialize messageDto
            }
            // Send message over WebSocket
            notifWeb.send(token, jsMessage);

            return true;
        }
        return false;
    }

    // Method for marking a message as read
    public boolean markMessageAsRead(int messageId) {
        MessageEntity message = messageDao.findById(messageId);
        if (message != null) {
            // Fetch all previous messages based on sentTime
            ArrayList<MessageEntity> previousMessages = messageDao.findPreviousMessages(message.getSentTime());
            // Mark all previous messages as read
            for (MessageEntity prevMessage : previousMessages) {
                prevMessage.setReadStatus(true);
            }
            return true;
        } else {
            return false;
        }
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

    // Method for fetching messages for a user
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
    public ArrayList<MessageDto> getChatMessages(int senderId, int receiverId) {
        // Retrieve the user entity corresponding to the id
        UserEntity sender = userDao.findUserById(senderId);
        UserEntity receiver = userDao.findUserById(receiverId);
        if (sender != null && receiver!= null) {
            ArrayList<MessageEntity> messageEntities = messageDao.findChangedMessages(sender, receiver);
            // Convert MessageEntity objects to MessageDto objects
            ArrayList<MessageDto> messages = convertMessagesFromEntityListToDtoList(messageEntities);
            return messages;
        } else {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }
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
