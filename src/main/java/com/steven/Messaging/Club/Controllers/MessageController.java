package com.steven.Messaging.Club.Controllers;


import com.steven.Messaging.Club.Configurations.EncryptionSecurity;
import com.steven.Messaging.Club.Dtos.MessageDto;
import com.steven.Messaging.Club.Entities.MessageEntity;
import com.steven.Messaging.Club.Entities.UserEntity;
import com.steven.Messaging.Club.Repositories.MessageIRepository;
import com.steven.Messaging.Club.Repositories.UserIRepository;
import com.steven.Messaging.Club.Responses.MessageResponse;
import com.steven.Messaging.Club.Services.FileSystemStorageService;
import com.steven.Messaging.Club.Services.MessageService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RequestMapping("/messagingClub")
@Controller
@MultipartConfig
public class MessageController {

    private final FileSystemStorageService fileSystemStorageService;
    private final MessageService messageService;
    public final MessageIRepository messageIRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserIRepository userIRepository;
    private final AuthenticationController authenticationController;
    public  ResponseEntity responseEntity;
    public String SendStatus,senders,messages;
    public MessageController(FileSystemStorageService fileSystemStorageService, MessageService messageService, MessageIRepository messageIRepository, SimpMessagingTemplate messagingTemplate, UserIRepository userIRepository, AuthenticationController authenticationController) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.messageService = messageService;
        this.messageIRepository = messageIRepository;
        this.messagingTemplate = messagingTemplate;
        this.userIRepository = userIRepository;
        this.authenticationController = authenticationController;
    }

    public String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

    public String getCurrentTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return now.format(formatter);
    }

    @PostMapping("/SendVocalMessage")
    public ResponseEntity<String> SendVocalMessage(@Valid @RequestParam("file") MultipartFile file,
                                             @ModelAttribute MessageDto input
    ) throws Exception {
        try {
            // You can save the file or process it as needed
            input.setMessage_date(getCurrentDate());
            input.setMessage_time(getCurrentTime());
            input.setNotify("SendNotification");

            // Save file logic here...
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                fileSystemStorageService.storeAudio(file);
                input.setFileNameVocal(fileName);
                messageService.sendingMessage(input);
            }else{
                input.setFileNameVocal("null");
                messageService.sendingMessage(input);
            }

            return new ResponseEntity<>("File uploaded successfully: ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/SendMessage")
    public ResponseEntity<String> SendMessage(@Valid @ModelAttribute MessageDto input
    ) throws Exception {
        try {
            // You can save the file or process it as needed
            input.setMessage_date(getCurrentDate());
            input.setMessage_time(getCurrentTime());
            input.setNotify("SendNotification");
            // Save file logic here...
            input.setFileNameVocal("null");
            messageService.sendingMessage(input);
            messagingTemplate.convertAndSend("/topic/messages/" + input.getPhone_message_receptor(), input.getMessage_text());
            return new ResponseEntity<>("File uploaded successfully: ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/displayConversation", method = RequestMethod.POST)
    public ResponseEntity<String> displayMessageInChatbox(@Valid @ModelAttribute MessageDto input) {

        try{
            List<MessageEntity> Conversation = messageService.DisplayMessages(input.getPhone_message_sender(), input.getPhone_message_receptor());
            UserEntity userReceptorInfo = userIRepository.findByPhone(input.getPhone_message_receptor());
            StringBuilder Messages = new StringBuilder();

            if (!Conversation.isEmpty()) {
                for (MessageEntity message : Conversation) {
                    if(message.getMessageVocal().equals("null")){
                        if (message.getPhoneSender().equals(input.getPhone_message_sender())) {
                            // Message sent
                            Messages.append("<div class='chat outgoing'>")
                                    .append("<div class='details'>")
                                    .append("<p>")
                                    .append(message.getMessageText())
                                    .append("<br><span>").append(message.getMessageTime())
                                    .append("</span></p>")
                                    .append("</div></div>");
                        } else {
                            //

                            //Message Received
                            Messages.append("<div class='chat incoming'>")
                                    .append("<img class='userimg' src='/Images/").append(userReceptorInfo.getFile())
                                    .append("'class='cover'>")
                                    .append("<div class='details'>")
                                    .append("<p>")
                                    .append(message.getMessageText())
                                    .append("<br><span>")
                                    .append(message.getMessageTime())
                                    .append("</span></p>")
                                    .append("</div></div>");
                        }
                    }else {
                        EncryptionSecurity encryptionSecurity = new EncryptionSecurity();
                        SecretKey secretKey = encryptionSecurity.generateKey();
                        String DecryptedMessage = encryptionSecurity.decrypt(message.getMessageText(),secretKey);
                        if (message.getPhoneSender().equals(input.getPhone_message_sender())) {

                            // Message sent
                            Messages.append("<div class='chat outgoing'>")
                                    .append("<div class='details'>")
                                    .append("<p>").append("<audio style='height: 30px' src='/Audios/")
                                    .append(message.getMessageVocal()).append("' controls ></audio>")
                                    .append(message.getMessageText())
                                    .append("<br><span>").append(message.getMessageTime())
                                    .append("</span></p>")
                                    .append("</div></div>");
                        } else {
                            //Message Received
                            Messages.append("<div class='chat incoming'>")
                                    .append("<img class='userimg' src='/Images/").append(userReceptorInfo.getFile())
                                    .append("'class='cover'>")
                                    .append("<div class='details'>")
                                    .append("<p>").append("<audio style='height: 30px' src='/Audios/")
                                    .append(message.getMessageVocal()).append("' controls ></audio>")
                                    .append(message.getMessageText())
                                    .append("<br><span>")
                                    .append(message.getMessageTime())
                                    .append("</span></p>")
                                    .append("</div></div>");
                        }
                    }

                }
            } else {
                Messages.append("<div class='chat incomming'>")
                        .append("<div class='detail'>")
                        .append("<p id='noconversation'>").append("There is no conversation")
                        .append("<br><span>")
                        .append("</span></p>")
                        .append("</div></div>");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML) // Set the content type to HTML
                    .body(Messages.toString()); // Return the constructed message HTML
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @RequestMapping(value = "/readStatus", method = RequestMethod.POST)
    public ResponseEntity<?> unreadCounter(@Valid @ModelAttribute MessageEntity message) {
        try{
            String status ="read";
            messageService.ReadMessage(status,message.getPhoneReceptor(),message.getPhoneSender());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/notifyMe/{phone_sender}" , method = RequestMethod.POST)
    public ResponseEntity<?> notifyMeMessage(@Valid @PathVariable String phone_sender) {
        MessageResponse messageResponse = new MessageResponse(SendStatus,senders,messages);
        try{
            Object GetValueToNotify = messageService.SendNotify(phone_sender);

            if (GetValueToNotify != null) {
                responseEntity = ResponseEntity.ok(GetValueToNotify);
            }else{
                responseEntity = ResponseEntity.notFound().build();
            }
            return new  ResponseEntity<>(GetValueToNotify, HttpStatus.OK);
        }catch (Exception e){
            SendStatus = "FoundErrorDuringReceivingMessage";
            responseEntity =  new ResponseEntity<Object>(messageResponse, HttpStatus.BAD_REQUEST);
            return responseEntity;
        }
    }

    @RequestMapping(value = "/validateNotification", method = RequestMethod.POST)
    public ResponseEntity<String> validateNotification(@Valid @ModelAttribute MessageEntity message) {
        try {
            message.setNotify("NotificationSent");
            messageService.ValidateNotify(message.getNotify(),message.getPhoneSender());
            return ResponseEntity.status(HttpStatus.OK).body("Notification Validated");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
