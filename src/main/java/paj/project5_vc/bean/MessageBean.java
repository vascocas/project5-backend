package paj.project5_vc.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.websocket.*;
import paj.project5_vc.dao.MessageDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.dto.TaskDto;
import paj.project5_vc.entity.MessageEntity;
import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.UserEntity;

@Stateless
public class MessageBean implements Serializable {

    @EJB
    MessageDao messageDao;

    @EJB
    UserDao userDao;

    // Method for sending a message
    public boolean sendMessage(MessageDto messageDto) {
        UserEntity sender = userDao.findUserById(messageDto.getSenderId());
        UserEntity receiver = userDao.findUserById(messageDto.getReceiverId());
        if (sender != null && receiver != null) {
            MessageEntity message = new MessageEntity();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageText(messageDto.getMessageText());
            message.setReadStatus(false);
            messageDao.persist(message);
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

    // (apply in websocket package)
    // Method for handling incoming websocket messages
    public void handleMessageFromWebSocket(MessageEntity message, Session session) {
        try {
            // Access the session to get information from the sender: ID and username
            Integer senderId = (Integer) session.getUserProperties().get("userId");
            String senderUsername = (String) session.getUserProperties().get("username");

            if (senderId != null && senderUsername != null) {

                // Save the message to the database
                UserEntity sender = userDao.findUserById(senderId);
                message.setSender(sender);
                messageDao.persist(message);

                // Send a confirmation back to the sender
                session.getBasicRemote().sendText("Message received and saved");
            }

        } catch (Exception e) {
            // Handle any exceptions that might occur during message processing
            e.printStackTrace();
        }
    }

    // (apply in websocket package)
    // Method for sending a message via websocket
    public void sendMessageViaWebSocket(MessageEntity message, Session session) {
        try {
            // Convert the message entity to JSON format
            String jsonMessage = convertMessageEntityToJson(message);

            // Send the message via websocket
            session.getBasicRemote().sendText(jsonMessage);
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    // (apply in websocket package)
    // Method to convert message entity to JSON format
    private String convertMessageEntityToJson(MessageEntity message) {
        // Implement logic to convert message entity to JSON format
        // You can use libraries like Gson or Jackson for JSON serialization
        return ""; // Placeholder for JSON message
    }
}
