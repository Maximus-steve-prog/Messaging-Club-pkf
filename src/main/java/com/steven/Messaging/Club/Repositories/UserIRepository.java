package com.steven.Messaging.Club.Repositories;

import com.steven.Messaging.Club.Entities.MessageEntity;
import com.steven.Messaging.Club.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);


//    @Query("SELECT distinct m FROM MessageEntity m  ORDER BY m.MessageId  desc  ")
    List<UserEntity> findByIdNot(@Param("id") int id);

    UserEntity findByPhone(String phone);

    @Query("UPDATE UserEntity SET status=:status")
    Optional<UserEntity> updateStatusBeforeConnecting(String status);

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:username%")
    List<UserEntity> findUserLikeUsername(@Param("username") String username);


    @Query("SELECT distinct m,u FROM MessageEntity m,UserEntity u " +
            "WHERE (m.PhoneSender= :sender " +
            "AND m.PhoneReceptor = :receptor) " +
            "OR (m.PhoneSender = :receptor " +
            "AND m.PhoneReceptor = :sender)  " +
            "AND u.id <>:id ORDER BY m.MessageId DESC LIMIT 1")
   List<Object>  findLastConversationsByPhone(@Param("sender") String sender, @Param("receptor") String receptor, @Param("id") int id);

}
