package com.steven.Messaging.Club.Services;


import com.steven.Messaging.Club.Configurations.EncryptionSecurity;
import com.steven.Messaging.Club.Dtos.MessageDto;
import com.steven.Messaging.Club.Entities.MessageEntity;
import com.steven.Messaging.Club.Repositories.MessageIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class MessageService {


    private final MessageIRepository messageIRepository;
    public  String iFiSRead ;
    public MessageService(MessageIRepository messageIRepository) {
        this.messageIRepository = messageIRepository;
    }




    public void sendingMessage(MessageDto messageDetails) throws Exception {
        EncryptionSecurity encryptionSecurity = new EncryptionSecurity();
        SecretKey secretKey = encryptionSecurity.generateKey();
        String EncryptedMessageText = encryptionSecurity.encrypt(messageDetails.getMessage_text(), secretKey);
        MessageEntity message = new MessageEntity();
        message.setMessageStatus(messageDetails.getMessage_status());
        message.setMessageTime(messageDetails.getMessage_time());
        message.setMessageText(messageDetails.getMessage_text());
        message.setMessageDate(messageDetails.getMessage_date());
        message.setMessageDocs(messageDetails.getMessageDocs());
        message.setMessageImage(messageDetails.getMessageImage());
        message.setPhoneReceptor(messageDetails.getPhone_message_receptor());
        message.setPhoneSender(messageDetails.getPhone_message_sender());
        message.setMessageVocal(messageDetails.getFileNameVocal());
        message.setNotify(messageDetails.getNotify());
        messageIRepository.save(message);
    }

    public List<MessageEntity> DisplayMessages(String sender, String receptor) {
        return messageIRepository.findMessages(sender, receptor);
    }

    public String getUnreadMessagesCount(String phoneSender ,String phoneReceptor) {
        Long unreadMessagesCount = messageIRepository.findMessagesByStatus("unread", phoneSender, phoneReceptor);
        List<MessageEntity> lastMessage = LastMessage(phoneSender, phoneReceptor);
        // Check if the LastMessage list is empty before accessing it
        if (!lastMessage.isEmpty()) {
            // Accessing the first element safely as the list is not empty
            if (lastMessage.get(0).getMessageStatus().equals("unread")) {
                iFiSRead = "<i class='ri-check-line'></i>";
            } else {
                iFiSRead = "<i class='ri-check-double-line' id='DoubleCheck'></i>";
            }
        } else {
            // Handle empty LastMessage case, if needed
            iFiSRead = "<i class='ri-check-double-line'></i>"; // Or whatever default status you want
        }

        // Return unread messages count or iFiSRead based on the count value
        if (unreadMessagesCount == 0) {
            return iFiSRead;
        } else {
            return "<i class='CounterUnread'>"+unreadMessagesCount+"</i>";
        }
    }

    public void ReadMessage(String status,String phoneReceptor, String phoneSender) {
        messageIRepository.updateMessageStatus(status, phoneReceptor, phoneSender);
    }

    public List<MessageEntity> LastMessage(String phoneSender ,String phoneReceptor) {
        return messageIRepository.findLastMessages(phoneSender,phoneReceptor);
    }

    public List<MessageEntity> DetailsLastMessage(String phoneSender ,String phoneReceptor) {
        return messageIRepository.DetailsLastMessages(phoneSender,phoneReceptor);
    }

    public Object SendNotify(String PhoneSender ) {
        return  messageIRepository.SendNotificationToReceiver(PhoneSender);
    }

    public void ValidateNotify(String Notify ,String phoneReceptor) {
        messageIRepository.ValidateSendingNotification(Notify,phoneReceptor);
    }


}
