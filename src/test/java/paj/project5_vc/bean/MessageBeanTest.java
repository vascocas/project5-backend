package paj.project5_vc.bean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageBeanTest {

    @InjectMocks
    MessageBean messageBean;
    @Mock
    MessageDao messageDaoMock;
    @Mock
    NotificationDao notifDaoMock;
    @Mock
    UserDao userDaoMock;


    @Test
    void givenExistentUsersIdWhenSendMessageThenReturnTrue() {

        // given
        UserEntity senderUser = new UserEntity();
        senderUser.setId(3);
        UserEntity receiverUser = new UserEntity();
        receiverUser.setId(33);
        MessageDto msgDto = new MessageDto();
        msgDto.setSenderId(senderUser.getId());
        msgDto.setReceiverId(receiverUser.getId());
        msgDto.setMessageText("Chat message test");

        when(userDaoMock.findUserById(senderUser.getId())).thenReturn(senderUser);
        when(userDaoMock.findUserById(receiverUser.getId())).thenReturn(receiverUser);

        doNothing().when(messageDaoMock).persist(any(MessageEntity.class));
        doNothing().when(notifDaoMock).persist(any(NotificationEntity.class));

        // when
        boolean result = messageBean.sendMessage(msgDto);

        // then
        assertTrue(result);
        verify(userDaoMock, times(1)).findUserById(senderUser.getId());
        verify(userDaoMock, times(1)).findUserById(receiverUser.getId());
        verify(userDaoMock, times(2)).findUserById(anyInt());
        verify(messageDaoMock, times(1)).persist(any(MessageEntity.class));
        verify(notifDaoMock, times(1)).persist(any(NotificationEntity.class));
    }

    @Test
    void givenNonExistentSenderIdWhenSendMessageThenReturnFalse() {

        // given
        int senderId = 3;
        UserEntity receiverUser = new UserEntity();
        receiverUser.setId(33);
        MessageDto msgDto = new MessageDto();
        msgDto.setSenderId(senderId);
        msgDto.setReceiverId(receiverUser.getId());
        msgDto.setMessageText("Chat message test");

        when(userDaoMock.findUserById(senderId)).thenReturn(null);
        when(userDaoMock.findUserById(receiverUser.getId())).thenReturn(receiverUser);

        // when
        boolean result = messageBean.sendMessage(msgDto);

        // then
        assertFalse(result);
        verify(userDaoMock, times(1)).findUserById(senderId);
        verify(userDaoMock, times(1)).findUserById(receiverUser.getId());
        verify(userDaoMock, times(2)).findUserById(anyInt());
        verifyNoInteractions(messageDaoMock);
        verifyNoInteractions(notifDaoMock);
    }

    @Test
    void givenNonExistentReceiverIdWhenSendMessageThenReturnFalse() {

        // given
        UserEntity senderUser = new UserEntity();
        senderUser.setId(3);
        int receiverId = 33;
        MessageDto msgDto = new MessageDto();
        msgDto.setSenderId(senderUser.getId());
        msgDto.setReceiverId(receiverId);
        msgDto.setMessageText("Chat message test");

        when(userDaoMock.findUserById(senderUser.getId())).thenReturn(senderUser);
        when(userDaoMock.findUserById(receiverId)).thenReturn(null);

        // when
        boolean result = messageBean.sendMessage(msgDto);

        // then
        assertFalse(result);
        verify(userDaoMock, times(1)).findUserById(senderUser.getId());
        verify(userDaoMock, times(1)).findUserById(receiverId);
        verify(userDaoMock, times(2)).findUserById(anyInt());
        verifyNoInteractions(messageDaoMock);
        verifyNoInteractions(notifDaoMock);
    }

    @Test
    void givenNonExistentMessageIdWhenMarkMessageAsReadThenReturnFalse() {
        // given
        int messageId = 0;
        when(messageDaoMock.findById(messageId)).thenReturn(null);
        // when
        boolean result = messageBean.markMessageAsRead(messageId);
        // then
        assertFalse(result);
        verify(messageDaoMock, times(1)).findById(messageId);
    }

    @Test
    void givenExistentMessageIdWithPrevMessagesWhenMarkMessageAsReadThenReturnTrue() {

        // given
        int messageId = 1;
        MessageEntity testMessage = new MessageEntity();
        UserEntity senderUser = new UserEntity();
        senderUser.setId(5);
        UserEntity receiverUser = new UserEntity();
        receiverUser.setId(10);
        testMessage.setSender(senderUser);
        testMessage.setReceiver(receiverUser);
        Timestamp sentTime = Timestamp.from(Instant.now());
        testMessage.setSentTime(sentTime);
        ArrayList<MessageEntity> getMessages = new ArrayList<>();
        getMessages.add(testMessage);

        when(messageDaoMock.findById(messageId)).thenReturn(testMessage);

        when(messageDaoMock.findPreviousChatMessages(senderUser.getId(), receiverUser.getId(),sentTime)).thenReturn(getMessages);

        // when
        boolean result = messageBean.markMessageAsRead(messageId);

        // then
        assertTrue(result);
        assertTrue(testMessage.isReadStatus());
        verify(messageDaoMock, times(1)).findById(messageId);
        verify(messageDaoMock, times(1)).findPreviousChatMessages(senderUser.getId(), receiverUser.getId(),sentTime);
    }
    @Test
    void givenExistentMessageIdWithoutPrevMessagesWhenMarkMessageAsReadThenReturnTrue() {
        // given
        int messageId = 1;
        MessageEntity testMessage = new MessageEntity();
        UserEntity senderUser = new UserEntity();
        senderUser.setId(5);
        UserEntity receiverUser = new UserEntity();
        receiverUser.setId(10);
        testMessage.setSender(senderUser);
        testMessage.setReceiver(receiverUser);
        Timestamp sentTime = Timestamp.from(Instant.now());
        testMessage.setSentTime(sentTime);
        MessageEntity test2Message = new MessageEntity();
        UserEntity receiver2User = new UserEntity();
        receiver2User.setId(8);
        test2Message.setReceiver(receiver2User);
        ArrayList<MessageEntity> getMessages = new ArrayList<>();
        getMessages.add(test2Message);

        when(messageDaoMock.findById(messageId)).thenReturn(testMessage);

        when(messageDaoMock.findPreviousChatMessages(senderUser.getId(), receiverUser.getId(),sentTime)).thenReturn(getMessages);

        // when
        boolean result = messageBean.markMessageAsRead(messageId);

        // then
        assertTrue(result);
        assertFalse(testMessage.isReadStatus());
        assertFalse(test2Message.isReadStatus());

        verify(messageDaoMock, times(1)).findById(messageId);
        verify(messageDaoMock, times(1)).findPreviousChatMessages(senderUser.getId(), receiverUser.getId(),sentTime);
    }

}