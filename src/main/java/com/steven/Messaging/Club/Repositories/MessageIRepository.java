package com.steven.Messaging.Club.Repositories;

import com.steven.Messaging.Club.Entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageIRepository extends JpaRepository<MessageEntity, Long> {


    @Query("SELECT m FROM MessageEntity m  " +
            "WHERE (m.PhoneSender= :sender " +
            "AND m.PhoneReceptor = :receptor) " +
            "OR (m.PhoneSender = :receptor " +
            "AND m.PhoneReceptor = :sender) " +
            "ORDER BY m.MessageId ASC")
    List<MessageEntity> findMessages(@Param("sender") String sender, @Param("receptor") String receptor );

    @Query("SELECT COUNT(m) FROM MessageEntity m " +
            "WHERE m.MessageStatus = :status " +
            "and m.PhoneSender=:Receptor " +
            "and m.PhoneReceptor=:Sender")
    Long findMessagesByStatus(@Param("status") String status,@Param("Sender") String Sender,@Param("Receptor") String Receptor);

    @Modifying
    @Transactional
    @Query("UPDATE MessageEntity m SET m.MessageStatus = :status " +
            "WHERE m.PhoneReceptor = :phone_sender " +
            "AND m.PhoneSender = :Phone_Receiver")
    void updateMessageStatus(@Param("status") String status, @Param("Phone_Receiver") String Phone_Receiver, @Param("phone_sender") String phone_sender);


    @Query("SELECT DISTINCT m FROM MessageEntity m  " +
            "WHERE (m.PhoneSender= :sender " +
            "AND m.PhoneReceptor = :receptor) " +
            "OR (m.PhoneSender = :receptor " +
            "AND m.PhoneReceptor = :sender) " +
            "ORDER BY m.MessageId DESC   LIMIT 1")
    List<MessageEntity> findLastMessages(@Param("sender") String sender, @Param("receptor") String receptor );


    @Query("SELECT m FROM MessageEntity m  " +
            "WHERE (m.PhoneSender= :sender " +
            "AND m.PhoneReceptor = :receptor) " +
            "OR (m.PhoneSender = :receptor " +
            "AND m.PhoneReceptor = :sender) " +
            "ORDER BY m.MessageId DESC")
    List<MessageEntity> DetailsLastMessages(@Param("sender") String sender, @Param("receptor") String receptor );

    @Query("SELECT m FROM MessageEntity m " +
            "WHERE  m.PhoneReceptor=:PhoneSender " +
            "ORDER BY m.MessageId desc  LIMIT 1")
    Optional<MessageEntity> SendNotificationToReceiver(@Param("PhoneSender") String PhoneSender);

    @Modifying
    @Transactional
    @Query("UPDATE MessageEntity m SET m.Notify=:Notify " +
            "WHERE  m.PhoneReceptor=:Phone_Receiver")
    void ValidateSendingNotification(@Param("Notify") String Notify, @Param("Phone_Receiver") String Phone_Receiver);

    @Query("SELECT DISTINCT(m.PhoneReceptor), m FROM MessageEntity m WHERE m.PhoneReceptor !=:phone_sender ORDER BY m.MessageId DESC ")
    List<Object[]>  findLastConversationsByPhone(@Param("phone_sender") String phone_sender);
}
