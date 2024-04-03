package paj.project5_vc.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.websocket.*;
import paj.project5_vc.dao.MessageDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.entity.MessageEntity;
import paj.project5_vc.entity.UserEntity;
@Stateless
public class MessageBean implements Serializable {

    @EJB
    MessageDao messageDao;

    @EJB
    UserDao userDao;

    // Method for sending a message
    public void sendMessage(MessageDto messageDto) {
        UserEntity sender = userDao.findUserById(messageDto.getSenderId());
        UserEntity receiver = userDao.findUserById(messageDto.getReceiverId());
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageText(messageDto.getMessageText());
        messageDao.persist(message);
    }

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

    // Method to convert message entity to JSON format (You'll need to implement this)
    private String convertMessageEntityToJson(MessageEntity message) {
        // Implement logic to convert message entity to JSON format
        // You can use libraries like Gson or Jackson for JSON serialization
        return ""; // Placeholder for JSON message
    }

    // Method for marking a message as read
    public void markMessageAsRead(int messageId) {
        MessageEntity message = messageDao.findById(messageId);
        if (message != null) {
            message.setReadStatus(true);

        }
    }

    // Method for fetching messages for a user
    public List<MessageDto> getMessagesForUser(int userId) {
        // Retrieve the user entity corresponding to the userId
        UserEntity user = userDao.findUserById(userId); // Assuming you have a method in your user DAO to find a user by ID

        if (user == null) {
            // If user is not found, return an empty list
            return new ArrayList<>();
        }

        // Retrieve messages for the user from the database
        List<MessageEntity> messageEntities = messageDao.findMessagesForUser(user);

        // Convert MessageEntity objects to MessageDto objects
        List<MessageDto> messages = new ArrayList<>();
        for (MessageEntity messageEntity : messageEntities) {
            messages.add(convertMessageEntityToDto(messageEntity));
        }

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
        return messageDto;
    }


    // Method for handling incoming websocket messages
    public void handleMessageFromWebSocket(MessageEntity message, Session session) {
        try {
            // Access the session to get information from the sender: ID and username
            Integer senderId = (Integer) session.getUserProperties().get("userId");
            String senderUsername = (String) session.getUserProperties().get("username");

            if (senderId!=null && senderUsername!=null) {

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



}
