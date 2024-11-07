package com.steven.Messaging.Club.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "Messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId",unique = true , nullable = false)
    private Long MessageId;
    @Column(name = "PhoneReceptor" , nullable = false)
    private String PhoneReceptor;
    @Column(name = "PhoneSender", nullable = false)
    private String PhoneSender;
    @Column(columnDefinition ="TEXT",name = "MessageText", nullable = true)
    private String MessageText;
    @Column(name = "MessageImage", nullable = true)
    private String MessageImage;
    @Column(name = "MessageVocal", nullable = true)
    private String MessageVocal;
    @Column(name = "MessageDocs", nullable = true)
    private String MessageDocs;
    @Column(name = "MessageDate", nullable = false)
    private String MessageDate;
    @Column(name = "MessageTime", nullable = false)
    private String MessageTime;
    @Column(name = "MessageStatus", nullable = false)
    private String MessageStatus;
    @Column(name = "Notify", nullable = true)
    private String Notify;

    public MessageEntity() {

    }
}
