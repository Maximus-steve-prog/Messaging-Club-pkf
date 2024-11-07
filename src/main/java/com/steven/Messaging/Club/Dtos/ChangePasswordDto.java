package com.steven.Messaging.Club.Dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    private String email;
    private String password;

}
