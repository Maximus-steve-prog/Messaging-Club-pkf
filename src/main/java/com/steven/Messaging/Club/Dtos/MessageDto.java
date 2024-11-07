package com.steven.Messaging.Club.Dtos;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private int id;
    private Long MessageId;
    private String phone_message_receptor;
    private String phone_message_sender;
    private String message_text;
    private String MessageImage;
    private MultipartFile file;
    private String fileNameVocal;
    private String MessageDocs;
    private String message_date;
    private String message_time;
    private String message_status;
    private String Notify;

}


