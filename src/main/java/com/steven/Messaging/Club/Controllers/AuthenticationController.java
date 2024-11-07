package com.steven.Messaging.Club.Controllers;

import com.steven.Messaging.Club.Configurations.EncryptionSecurity;
import com.steven.Messaging.Club.Dtos.*;
import com.steven.Messaging.Club.Entities.MessageEntity;
import com.steven.Messaging.Club.Entities.UserEntity;
import com.steven.Messaging.Club.Repositories.MessageIRepository;
import com.steven.Messaging.Club.Repositories.UserIRepository;
import com.steven.Messaging.Club.Repositories.UserRepository;
import com.steven.Messaging.Club.Responses.LoginResponse;
import com.steven.Messaging.Club.Responses.RegistrationResponse;
import com.steven.Messaging.Club.Services.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.crypto.spec.SecretKeySpec;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequestMapping("/messagingClub")
@Controller
@MultipartConfig
public class AuthenticationController extends HttpServlet {



    private final UserIRepository userIRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final FileSystemStorageService fileSystemStorageService;
    LoginResponse loginResponse = new LoginResponse();
    private final AuthenticationService authenticationService;
    public String status,message;
    ResponseEntity responseEntity;

    private BCryptPasswordEncoder passwordEncoder;
    private MessageIRepository messageIRepository;

    public AuthenticationController(UserIRepository userIRepository, UserRepository userRepository, UserService userService, MessageService messageService, HandlerExceptionResolver handlerExceptionResolver, JwtService jwtService, StorageService storageService, FileSystemStorageService fileSystemStorageService, AuthenticationService authenticationService) {
        this.userIRepository = userIRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.messageService = messageService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterDto registerDto) {

            try{
                UserEntity userDetails = userRepository.FindUserByEmail(registerDto.getEmail());
                if(userDetails != null){
                    if (!Objects.equals(userDetails.getFile(), "DefaultUser.png")) {
                        status = "alreadyExist";
                        message = "this user is already in use";
                    }else{
                        status="complete";
                        message= String.valueOf(userDetails.getId());
                    }
                }else{
                        registerDto.setImageName("DefaultUser.png");
                        registerDto.setStatus("connected");
                        UserEntity registeredUser = authenticationService.signup(registerDto);
                        String jwtToken = jwtService.generateToken(registeredUser);
                        loginResponse.setToken(jwtToken);
                        loginResponse.setExpiresIn(jwtService.getExpirationTime());
                        status="success";
                        message = String.valueOf(registeredUser.getId());
                }

                RegistrationResponse registrationResponse = new RegistrationResponse(status,message);
                switch (status){
                    case "success":
                        responseEntity =  new ResponseEntity<Object>(registrationResponse, HttpStatus.OK);
                        break;
                    case "alreadyExist":
                        responseEntity =  new ResponseEntity<Object>(registrationResponse, HttpStatus.OK);
                        break;
                    case "complete":
                        responseEntity =  new ResponseEntity<Object>(registrationResponse, HttpStatus.OK);
                        break;
                }
                return  responseEntity;
            }catch(Exception e){
                status="error";
                message="An error occured";
                RegistrationResponse registrationResponse = new RegistrationResponse(status,message);
                responseEntity =  new ResponseEntity<Object>(registrationResponse, HttpStatus.OK);
                return  responseEntity;
            }
    }

    @RequestMapping(value = "/edit/profile/{id}" , method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> editProfile( @PathVariable int id, @RequestParam("file") MultipartFile file,
                                         @ModelAttribute UserProfileDto input
                                         ) throws Exception {
           String fileName = file.getOriginalFilename().toString();
           fileSystemStorageService.store(file);
           UserEntity user = userIRepository.findById(id).get();
           user.setRoles(input.getRoles());
           user.setPassword(getPasswordEncoder(input.getPassword()));
           user.setProfession(input.getProfession());
           user.setFile(fileName);
           UserEntity UserProfile = authenticationService.uploadProfile( id, user);
           String jwtToken = jwtService.generateToken(UserProfile);
           loginResponse.setToken(jwtToken);
           loginResponse.setExpiresIn(jwtService.getExpirationTime());
           return  ResponseEntity.ok(loginResponse);
    }


    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> updateStatus(@PathVariable int id) {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setStatus("connected");
        try{
            UserEntity user = userIRepository.findById(id).get();
            user.setStatus(registerDto.getStatus());
            authenticationService.updateStatusById(id,user);
        }catch (Exception e){
            status="error";
            message="An error occured";
            return  ResponseEntity.ok(loginResponse);
        }
        return  ResponseEntity.ok(loginResponse);
    }

    @RequestMapping(value="/load/userid/{id}" , method = RequestMethod.POST)
    public ResponseEntity<?>LoadUserId(@PathVariable int id){
        Object GetUserDetails = userIRepository.findById(id);
        return new  ResponseEntity<>(GetUserDetails, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginAuthenticationDto loginUserDto) {
        UserEntity userDetails = userRepository.FindUserByEmail(loginUserDto.getEmail());
        if(userDetails != null){
            try{
                UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);
                String jwtToken = jwtService.generateToken(authenticatedUser);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());
                loginResponse.setToken(jwtToken);
                status="success";
                message= String.valueOf(userDetails.getId());
                Object GetUserDetails = userIRepository.findById((int) userDetails.getId());
                return new  ResponseEntity<>(GetUserDetails, HttpStatus.OK);
            }catch (Exception e ){
                status="error";
                message="Bad credentials";
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setExpiresIn(jwtService.getExpirationTime());
                return new  ResponseEntity<>(new RegistrationResponse(status,message), HttpStatus.OK);
            }
        }else {
            status = "notExisted";
            message = "Bad credentials User doesn't exist";
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            return new ResponseEntity<>(new RegistrationResponse(status, message), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/searchFriend/{username}/{id}" , method = RequestMethod.POST)
    public ResponseEntity<?> searchFriendByName(@PathVariable() String username,@PathVariable String id) throws Exception {
            List<UserEntity> GetUserAllDetails =  userIRepository.findUserLikeUsername(username);
            StringBuilder SearchFriendsHTML = new StringBuilder();
            EncryptionSecurity encryptionSecurity = new EncryptionSecurity();
            if(!GetUserAllDetails.isEmpty()){
                for (UserEntity SearchFriend  : GetUserAllDetails) {
//                    SecretKey secretKey = encryptionSecurity.generateKey();
//                    String DecryptedMessage = encryptionSecurity.decrypt(LastSMS(id, SearchFriend.getPhone()),secretKey);
                    SearchFriendsHTML.append("<div class='block show-conversation' id='")
                            .append(SearchFriend.getUsername()).append("'")
                            .append("data-user-phone='").append(SearchFriend.getPhone())
                            .append("' ").append("image='").append(SearchFriend.getFile())
                            .append("' ").append("status='")
                            .append(SearchFriend.getStatus())
                            .append("'>").append("<div class='imgbx' imageProf='").append(SearchFriend.getFile()).append("'>")
                            .append("<img class='userimg' src='/Images/").append(SearchFriend.getFile())
                            .append("'class='cover'>").append("</div>")
                            .append("<div class='details'>").append("<div class='listHead'>")
                            .append("<h4>").append(SearchFriend.getUsername())
                            .append("</h4>").append("<p class='time'>").append(LastTime(id, SearchFriend.getPhone())).append("</p>")
                            .append("</div>").append("<div class='message_p'>")
                            .append("<p>").append(LastSMS(id, SearchFriend.getPhone())).append("</p>").append("<b class=''>").append(messageService.getUnreadMessagesCount(id,SearchFriend.getPhone())).append("</b>")
                            .append("</div>").append("</div>").append("</div>");
                }
            }

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML) // Set the content type to HTML
                .body(SearchFriendsHTML.toString());
    }

    @RequestMapping(value = "/GetFriends/{id}", method = RequestMethod.POST)
    public ResponseEntity<?>GetFriends(@PathVariable int id){
        List<UserEntity> AllFriend = userIRepository.findByIdNot(id);
        return ResponseEntity.ok(AllFriend);
    }

    public String LastSMS(@PathVariable String PhoneSender, @PathVariable String PhoneReceptor){
        StringBuilder LastMessageHTML = new StringBuilder();
        List<MessageEntity> LastMessage = messageService.LastMessage(PhoneSender, PhoneReceptor);
        String you ="";
            if(!LastMessage.isEmpty()){
                if(LastMessage.get(0).getMessageVocal().equals("null")){
                    if(LastMessage.get(0).getPhoneSender().equals(PhoneSender)){
                        you ="You : ";
                    }else {
                        you="";
                    }
                    LastMessageHTML.append(you)
                            .append(LastMessage.get(0).getMessageText());
                }else {
                        if(LastMessage.get(0).getPhoneSender().equals(PhoneSender)){
                            you ="You : ";
                        }else {
                            you="";
                        }
                        LastMessageHTML.append(you)
                                .append("<i class='ri-mic-fill'></i>");
                }
            }else {
                LastMessageHTML.append("No Message");
            }
            return LastMessageHTML.toString();
    }

    public String LastTime(@PathVariable String PhoneSender, @PathVariable String PhoneReceptor) {
        StringBuilder LastTimeHTML = new StringBuilder();
        List<MessageEntity> LastMessage = messageService.LastMessage(PhoneSender, PhoneReceptor);
        try {
            if (!LastMessage.isEmpty()) {
                Date dateToCompare = new SimpleDateFormat("yyyy-MM-dd").parse(LastMessage.get(0).getMessageDate());

                if (isUtilDateBeforeToday(dateToCompare)) {
                    LastTimeHTML.append(LastMessage.get(0).getMessageDate());
                } else {
                    LastTimeHTML.append(LastMessage.get(0).getMessageTime());
                }
            } else {
                LastTimeHTML.append("---");
            }
            return LastTimeHTML.toString();
        } catch (ParseException e) {
            System.out.println(e);
            return LastTimeHTML.toString();
        }
    }


    public boolean isUtilDateBeforeToday(Date inputDate) {
        LocalDate today = LocalDate.now();
        LocalDate inputLocalDate = inputDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return inputLocalDate.isBefore(today);
    }

    @RequestMapping(value = "/displayAllFriend" , method = RequestMethod.POST)
    public ResponseEntity<String> DisplayAllFriend(@Valid @ModelAttribute UserEntity id) throws Exception {
        List<UserEntity> AllFriend = userIRepository.findByIdNot((int) id.getId());
        StringBuilder friendsHTML = new StringBuilder();
        EncryptionSecurity encryptionSecurity = new EncryptionSecurity();


        if(!AllFriend.isEmpty()){
            for (UserEntity friend  : AllFriend) {
                SecretKey secretKey = encryptionSecurity.generateKey();
//                String DecryptedMessage = encryptionSecurity.decrypt(LastSMS(id.getPhone(), friend.getPhone()),secretKey);
                    friendsHTML.append("<div class='block show-conversation' id='")
                            .append(friend.getUsername()).append("'")
                            .append("data-user-phone='").append(friend.getPhone())
                            .append("' ").append("image='").append(friend.getFile())
                            .append("' ").append("status='")
                            .append(friend.getStatus())
                            .append("'>").append("<div class='imgbx' imageProf='").append(friend.getFile()).append("'>")
                            .append("<img class='userimg' src='/Images/").append(friend.getFile())
                            .append("'class='cover'>").append("</div>")
                            .append("<div class='details'>").append("<div class='listHead'>")
                            .append("<h4>").append(friend.getUsername())
                            .append("</h4>").append("<p class='time'>").append(LastTime(id.getPhone(), friend.getPhone())).append("</p>")
                            .append("</div>").append("<div class='message_p'>")
                            .append("<p>").append(LastSMS(id.getPhone(), friend.getPhone())).append("</p>").append("<b class=''>").append(messageService.getUnreadMessagesCount(id.getPhone(),friend.getPhone())).append("</b>")
                            .append("</div>").append("</div>").append("</div>");
            }

        }

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML) // Set the content type to HTML
                .body(friendsHTML.toString()); // Return the constructed message HTML
    }


//    @RequestMapping(value = "/DisplayCurrentConversationBetweenFriend" , method = RequestMethod.POST)
//    public ResponseEntity<?> DisplayCurrentConversationBetweenFriend(@Valid @RequestBody MessageDto messageDto){
//        return ResponseEntity.ok(authenticationService.getCurrentConversationsByPhones(messageDto.getPhone_message_sender(),messageDto.getPhone_message_receptor(),messageDto.getId()));
//    }


    @PostMapping(value = "/changePassword" )
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto){
        UserEntity userDetails = userRepository.FindUserByEmail(changePasswordDto.getEmail());
        if (userDetails != null){
           try{
               UserEntity user = userIRepository.findById((int) userDetails.getId()).get();
               user.setPassword(getPasswordEncoder(changePasswordDto.getPassword()));
               authenticationService.uploadProfile((int) userDetails.getId(), user);
               status = "success";
               message="Changed password";
               return new ResponseEntity<>(new RegistrationResponse(status, message), HttpStatus.OK);
           }catch (Exception e ){
               status="error";
               message="Bad credentials";
               return new ResponseEntity<>(new RegistrationResponse(status, message), HttpStatus.OK);
           }
        }else {
            status = "notExisted";
            message = "This user doesn't exist";
            return new ResponseEntity<>(new RegistrationResponse(status, message), HttpStatus.OK);
        }
    }

    public String getPasswordEncoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @GetMapping("/users")
    public ResponseEntity<LoginResponse> allUsers() {
        List<UserEntity> users = userService.allUsers();
        String jwtToken = jwtService.generateTokenSignup(users.toString());
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/chat")
    public String chat(){
        return "Views/Pages/ChatView";
    }

}