package com.steven.Messaging.Club.Repositories;

import com.steven.Messaging.Club.Entities.MessageEntity;
import com.steven.Messaging.Club.Entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserEntity FindUserByEmail(String email){
        String sql = "select * from users where email = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,email);
        if(rows.next()){
            UserEntity user = new UserEntity();
            user.setId(rows.getLong(1));
            user.setEmail(rows.getString("email"));
            user.setFile(rows.getString("file"));
            user.setUsername(rows.getString("username"));
            user.setPhone(rows.getString("phone"));
            return user;
        }
        return null;
    }


    public  UserEntity findUserLikeUsername(String username){
        String sql = "select * from users where username LIKE ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,"%"+username+"%");
        if(rows.next()){
            UserEntity user = new UserEntity();
            user.setId(rows.getLong(1));
            user.setEmail(rows.getString("email"));
            user.setFile(rows.getString("file"));
            user.setUsername(rows.getString("username"));
            user.setPhone(rows.getString("phone"));
            return user;
        }
        return null;
    }

    public List<MessageEntity> findUserDiffId(){
        String sql = "SELECT DISTINCT phone_receptor FROM `messages` WHERE message_text <>? ORDER BY message_id Desc ;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,"null");
        List<MessageEntity> messages = new ArrayList<>();
        while (rows.next()) {
            MessageEntity message = new MessageEntity();
//            message.setMessageId(rows.getLong("message_id"));
//            message.setMessageText(rows.getString("message_text"));
//            message.setMessageTime(rows.getString("message_time"));
            message.setPhoneReceptor(rows.getString("phone_receptor"));
//            message.setPhoneSender(rows.getString("phone_sender"));
//            message.setMessageDate(rows.getString("message_date"));

            messages.add(message);
        }

        return messages;
    }


}
