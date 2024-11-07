package com.steven.Messaging.Club.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String username;
    private String email;
    private String phone;
    private String country;
    private String address;
    private String dob;
    private String gender;
    private String status;
    private String imageName;

}


//{
//    "username":"steve"
//    "email":"steve@gmail.com"
//    "phone":"688086860"
//    "roles":"friend"
//    "password":"M@ximili@no22"
//}