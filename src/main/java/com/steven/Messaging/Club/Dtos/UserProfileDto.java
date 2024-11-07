package com.steven.Messaging.Club.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private int id;
    private MultipartFile file;
    private String profession;
    private String roles;
    private String password;

}
