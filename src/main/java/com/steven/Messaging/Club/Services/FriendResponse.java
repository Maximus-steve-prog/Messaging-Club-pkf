package com.steven.Messaging.Club.Services;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendResponse<object>{
    private String status;
    private object data;
}


