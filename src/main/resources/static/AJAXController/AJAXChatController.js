$(document).ready(






    function (options){

    $("#MessageText").click(function(){
        scrollDown();
    });

    const modal = $('#myModal');

    $("input.images").change(function () {
        let images_file = this.files[0],
            url = URL.createObjectURL(images_file);
        $(this).closest("#images").find(".profile-photo").attr("src", url);
    });
    var stompClient = null;
    //
    // let socket = new SockJS('http://192.168.48.220:7777/ws'); // Connect to the WebSocket endpoint
    // stompClient = Stomp.over(socket);
    //
    // stompClient.connect({}, function (frame) {
    //         console.log('Connected: ' + frame);
    //         // Subscribe to the /topic/updates channel
    //         stompClient.subscribe('/messagingClub/topic/messages', function (message) {
    //             // Handle the incoming message
    //             var entity = JSON.parse(message.body);
    //             console.log(entity); // For demonstration
    //             alert("New record added: " + JSON.stringify(entity));
    //             // You can update your DOM here with the new data
    //         });
    //     });

    function scrollDown() {
        if($("#PhoneReceiver").val() !== 0){
            let boxChat = $('#chatbox');
            boxChat.animate({scrollTop: 9999});
            boxChat.animate({scrollTop: 9999}).delay(0);
            boxChat.animate({scrollTop: 9999});
        }

    }


    // function scrollDown() {
    //     const boxChat = $('#chatbox');
    //     boxChat.scrollTop = boxChat.scrollHeight;
    // }


    setInterval(() => {
        ReceiveNotificationFrom();
    }, 2000);

    $("#emoji-button").click(function(){
        Swal.fire({
            html: " <div id='emoji-picker' class='hidden'> </div>",
            background: 'transparent',
            showCancelButton: false,
            showConfirmButton: false,
            showCloseButton: false,
            position: 'center-end'
        });
        $("#emoji-picker").css({
            'display': 'block',
            'z-index':'0'
        });
        const picker = document.getElementById('emoji-picker');
        picker.classList.toggle('hidden');

        if (!picker.classList.contains('hidden')) {
            // Load emojis (this could also be done via AJAX)
            const emojis = [
                 '😂', '🤣', '😃', '😄', '😅', '😆', '😇',
                 '😋', '😌', '😍', '😘', '😗', '😙', '😚', '😋',
                 '😐', '😑', '😶', '🙄', '😏', '😒', '😓', '😔',
                 '😣', '😫', '😩', '😤', '😮', '😱', '😨', '😰',
                 '😡', '😠', '🤬', '😈', '👿',  '😞', '😓',
                 '😬', '🙁', '😔', '😕', '😣', '😖', '😱', '😰',
                 '😡', '😠', '🤬', '😤', '😮', '😯', '😲', '😳',
                 '😏', '😑', '😒', '😓', '😥', '😞', '😔', '😟',
                 '😦', '😧', '😨', '😩', '😫', '😥', '😰', '😓',
                 '😟', '🙁', '😕', '😶', '😬',  '😻', '😍',
                '🙌','👏', '👍', '👎', '✊', '🤞', '🤟', '👌', '🤏','✋','👊',
                '🤛', '🤜', '🤚','✋', '👋', '🤚',  '👀','👃','👅', '👊',
                '✋', '👋',  '👍','👎', '✊', '👈', '👉', '👆', '👇', '🖖', '👐'
            ];
            picker.innerHTML = emojis.map(emoji => `<span class="emoji">${emoji}</span>`).join('');

            // Add click event for each emoji
            const emojiElements = document.querySelectorAll('.emoji');
            emojiElements.forEach(emoji => {
                emoji.addEventListener('click', () => {
                    // Handle emoji selection (e.g., insert into an input field)
                    console.log(emoji.textContent); // Replace with your logic

                    let   message = $("#MessageText").val();

                    $("#MessageText").val(message +""+emoji.textContent)


                    picker.classList.add('hidden'); // Hide picker after selection
                });
            });
        }
    })
    const searchParams = new URLSearchParams(window.location.search);
    id = atob(searchParams.get('li'));

    GetFriends(id);

    loadUserConnected (id);

    UpdateStatus(id);

    let SearchFriend = $("#SearchFriend");
    $(document).keydown(function(event) {
        // Check if the Windows key is pressed and the semicolon key is pressed
        if (event.which === 186 && event.ctrlKey) { // 186 is the semicolon key code
            event.preventDefault(); // Prevent the default action

            // Trigger AJAX request on button click
            $.ajax({
                url: 'your-server-endpoint', // Replace with your server URL
                type: 'POST', // or 'GET' depending on your use case
                data: {action: 'emoji-picker'}, // Example data
                success: function (response) {
                    // Handle the success response
                    $('#response').html(response);
                },
                error: function (xhr, status, error) {
                    // Handle the error
                    console.error(error);
                }
            });
        }
    })
    let Conversation =$(".rightSide"),
        ListFriends =$(".leftSide"),
        backtolist =$("#backtoList"),
        openFileBtn = $("#openFileBtn"),
        startBtnRecord = $("#startBtnRecord"),
        BtnSend = $("#BtnSend") ,
        isRecording = false,
        BtnSendRecord = $("#BtnSendRecord"),
        mediaRecorder,
        audioChunks = [],
        SelectPic = $("#SelectPic"),
        today = new Date(),
        date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate(),
        time = (today.getHours()-2) + ":" + today.getMinutes() + ":" + today.getSeconds(),
        statusMessage ="unread",
        phone_sender = atob(searchParams.get('pe')),
         Sound = '';

    function stopRecording() {
            if (mediaRecorder) {
                mediaRecorder.stop();
            }
        }

    function  messages (icon,  content,title){
        const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
            }
        });
        Toast.fire({
            icon: icon,
            title: title,
            text: content
        });
        return;
    }

    SelectPic.click( function(){
            (async () => {
                const { value: file } =await Swal.fire({
                    title: "Select image",
                    input: "file",
                    inputAttributes: {
                        "accept": "image/*",
                        "aria-label": "Upload your profile picture"
                    }
                });
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        Swal.fire({
                            title: "Your uploaded picture",
                            imageUrl: e.target.result,
                            imageAlt: "The uploaded picture"
                        });
                    };
                    reader.readAsDataURL(file);
                }
            })()
        });

    startBtnRecord.click(async function () {
        $(".chatbox_inputs").addClass("VocalPanel");
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaRecorder = new MediaRecorder(stream);
        mediaRecorder.start();
        isRecording = true;
        mediaRecorder.ondataavailable = event => {
            audioChunks.push(event.data);
        };

        mediaRecorder.onstop =function() {
            const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
            const audioUrl = URL.createObjectURL(audioBlob);
            $('#audioPlayback').attr('src', audioUrl);

            window.audioBlob = audioBlob;
            window.audioUrl = audioUrl;

            audioChunks = []; // Reset for next recording
        };
    });

    $("#BtnStopRecord").click(function (){
        $(".chatbox_inputs").removeClass("VocalPanel");
        if (mediaRecorder && isRecording) {
            mediaRecorder.stop();
            isRecording = false;
            window.audioBlob = null;// Update the recording state
            $('#audioPlayback').attr('src', "");
        }
    })

    BtnSend.click(function () {
        scrollDown();
        let message = $("#MessageText").val(),
            phone_receptor = $("#PhoneReceiver").val(),
            fileName = `audioVocal_${Date.now()}.wav`,
            vocal = new File([window.audioBlob], `${fileName}`, { type: 'audio/wav' });

        const formDataMessage = new FormData();
              formDataMessage.append("message_date", date);
              formDataMessage.append("phone_message_receptor", phone_receptor);
              formDataMessage.append("message_time", time);
              formDataMessage.append("phone_message_sender", phone_sender);
              formDataMessage.append("message_status", statusMessage);
              formDataMessage.append("message_text ", message);
              formDataMessage.append("file",vocal);
        if(phone_receptor !==""){
            if(message !== ""){
                SendingMessageSound();
                sendMessage(formDataMessage);
                GetFriends(id);
                $("#MessageText").val("");
            }
        }else{
            messages("warning", "Receptor message cannot be empty", "warning");
        }

        window.audioBlob = null;// Update the recording state
    });

    BtnSendRecord.click(function (){
        scrollDown();
        if (mediaRecorder && isRecording) {
            mediaRecorder.stop();
            isRecording = false;
        }
        if (window.audioBlob) {
        let message = $("#MessageText").val(),
            fileName = `audioVocal_${Date.now()}.wav`,
            phone_receptor = $("#PhoneReceiver").val(),
            vocal = new File([window.audioBlob], `${fileName}`, { type: 'audio/wav' });
            const formDataMessage = new FormData();
                    formDataMessage.append("message_date", date);
                    formDataMessage.append("phone_message_receptor", phone_receptor);
                    formDataMessage.append("message_time", time);
                    formDataMessage.append("phone_message_sender", phone_sender);
                    formDataMessage.append("message_status", statusMessage);
                    formDataMessage.append("message_text ", message);
                    formDataMessage.append("file",vocal);
                    if(phone_receptor !==""){
                        // sendDetectMessage(phone_sender, phone_receptor, message);
                       // setTimeout(
                       //     sendVocalMessage(formDataMessage);
                       //  GetFriends(id);
                       // )
                        setTimeout(() => {
                            sendVocalMessage(formDataMessage);
                            GetFriends(id);
                        }, 5000);
                    }else{
                        messages("warning", "Receptor message cannot be empty", "warning");
                    }
                    window.audioBlob = null;// Update the recording state
             }
    })

    openFileBtn.click(function(){
        $("#fileInput").click();
    });

    SearchFriend.keyup(function () {
        let username = this.value;
        if (username !== "") {
            Loadsearch(username);
        }else{
            this.focus();
        }
    });

    backtolist.click(function(){
        Conversation.removeClass("showMessages");
        ListFriends.removeClass("Hidelist");
        $('#offcanvasInfo').removeClass('open');
        let id = atob(searchParams.get('li'));
        GetFriends(id);
    });

    function sendMessage(formDataMessage) {
            $.ajax({
                url: '/messagingClub/SendMessage', // Your server endpoint
                method: 'POST',
                data:formDataMessage,
                enctype: 'multipart/form-data',
                cache: false,
                processData: false, // Prevent jQuery from processing the data
                contentType: false, // Prevent jQuery from overriding the content type
                success: function(response) {
                    SendingMessageSound();
                    DisplayConversation();
                    scrollDown();
                    GetFriends(id);
                  // window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.log('Upload failed:', error);
                }
            });

    }

    function sendVocalMessage(formDataMessage){
        if (window.audioBlob) { // Check if audioBlob exists
            // AJAX request to upload the audio file
            $.ajax({
                url: '/messagingClub/SendVocalMessage', // Your server endpoint
                method: 'POST',
                data:formDataMessage,
                enctype: 'multipart/form-data',
                cache: false,
                processData: false, // Prevent jQuery from processing the data
                contentType: false, // Prevent jQuery from overriding the content type
                success: function(response) {
                    SendingMessageSound();
                    DisplayConversation();
                    scrollDown();
                    $(".chatbox_inputs").removeClass("VocalPanel");
                },
                error: function(xhr, status, error) {
                    console.log('Upload failed:', error);
                }
            });
        }
    }

    function UpdateStatus(id){
        $.ajax({
            type: 'POST',
            url: `/messagingClub/update/status/${id}`,
            contentType: 'application/json',
            dataType:"json",
            success : function (response){

            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        });
    }

    function loadUserConnected (id){
        $.ajax({
            type: 'POST',
            url: `/messagingClub/load/userid/${id}`,
            contentType: 'application/json',
            dataType:"json",
            success : function (response){
                $("#ImgProfile").attr('src',  "/Images/"+response.file+"");
                $("#profile-photo").attr('src',  "/Images/"+response.file+"");
                $("#username").val(response.username);
                $("#profession").val(response.profession);
                $("#email").val(response.email);
                $("#dob").val(response.dob);
                $("#phone").val(response.phone);
                $("#gender").val(response.gender);
                $("#country").val(response.country);
                $("#address").val(response.address);
                $("#Desired").val(response.roles);
            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        });
    }

    function  GetFriends(id){
        let   phone_sender = atob(searchParams.get('pe'));
        let GetId = new     FormData();
        GetId.append("id",id);
        GetId.append("phone",phone_sender);
        $.ajax({
            type: 'POST',
            url: `/messagingClub/displayAllFriend`,
            data : GetId,
            contentType: false, // Prevent jQuery from overriding the content type
            cache: false,
            processData: false, // Prevent jQuery from processing the data
            success: function (response) {
                $("#ChatList").html(response);
                $('.show-conversation').each(function() {
                    // Add a click event listener to each element
                    // scrollDown();
                    $('.show-conversation').click(function() {
                        let userSelectedName = $("#ConnectedUser");
                        $("#PhoneReceiver").val($(this).data("user-phone"));
                        userSelectedName.html($(this).attr("id") +`<br> <span>`+$(this).attr("status")+`</span>`);
                        ReadStatus($(this).data("user-phone"));
                        $("#SelectedUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        // let imgSrc = $('#SelectedUserImg').attr('src');
                        $(".convUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        Conversation.addClass("showMessages");
                        ListFriends.addClass("Hidelist");
                        DisplayConversation();
                        $(".incoming .cover.GetImage").attr('src', $("#SelectedUserImg").attr('src'));                    });
                });

                $('.imgbx').each(function(){
                    $('.imgbx').click(function(){
                        $('.modal-content').css({
                            'background-image': 'url(/Images/' + $(this).attr("imageProf") + ')',
                            'background-size': 'cover',
                            'background-repeat': 'no-repeat',
                            'background-attachment': 'fixed'
                        });

                        modal.css({
                            'display': 'flex',
                        });
                        // Use flex to center content
                        setTimeout(() => {
                            modal.addClass('show'); // Trigger the show class after display
                        }, 10); // Small delay to allow rendering
                    });
                });
            },
            error: function (xhr, status, error) {
                console.log( error);
            }
        });


    }
    // ListOfCurrentMessage (id);

    function ListOfCurrentMessage (id){
        let   phone_sender = atob(searchParams.get('pe'));
        let GetId = new     FormData();
        GetId.append("id",id);
        GetId.append("phone",phone_sender);
        $.ajax({
            type: 'POST',
            url: `/messagingClub/ListOfCurrentMessage/${id}/${phone_sender}`,
            contentType: false, // Prevent jQuery from overriding the content type
            cache: false,
            processData: false,// Prevent jQuery from processing the data
            success: function (response) {
                console.log(response);
                $("#ChatList").html(response);
                $('.show-conversation').each(function() {
                    // Add a click event listener to each element
                    $('.show-conversation').click(function() {
                        let userSelectedName = $("#ConnectedUser");
                        $("#PhoneReceiver").val($(this).data("user-phone"));
                        userSelectedName.html($(this).attr("id") +`<br> <span>`+$(this).attr("status")+`</span>`);
                        ReadStatus($(this).data("user-phone"));
                        $("#SelectedUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        // let imgSrc = $('#SelectedUserImg').attr('src');
                        $(".convUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        Conversation.addClass("showMessages");
                        ListFriends.addClass("Hidelist");
                        DisplayConversation();
                        $(".incoming .cover.GetImage").attr('src', $("#SelectedUserImg").attr('src'));                    });
                });

                $('.imgbx').each(function(){
                    $('.imgbx').click(function(){
                        $('.modal-content').css({
                            'background-image': 'url(/Images/' + $(this).attr("imageProf") + ')',
                            'background-size': 'cover',
                            'background-repeat': 'no-repeat',
                            'background-attachment': 'fixed'
                        });

                        modal.css({
                            'display': 'flex',
                        });
                        // Use flex to center content
                        setTimeout(() => {
                            modal.addClass('show'); // Trigger the show class after display
                        }, 10); // Small delay to allow rendering
                    });
                });
            },
            error: function (xhr, status, error) {
                console.log( error);
            }
        });

    }

    setInterval(() =>{
       let readphone = $("#PhoneReceiver").val();
        if(readphone !=="") {
            ReadStatus(readphone);
        }
    },500);

    function ReadStatus(Phone_Receiver){
        let   phone_sender = atob(searchParams.get('pe'));
        let GetReadStatus = new     FormData();
        GetReadStatus.append("PhoneReceptor",Phone_Receiver);
        GetReadStatus.append("PhoneSender",phone_sender);
        $.ajax({
            type: 'POST',
            url:`/messagingClub/readStatus`,
            data: GetReadStatus,
            contentType: false, // Prevent jQuery from overriding the content type
            cache: false,
            processData: false, // Prevent jQuery from processing the data
            success : function (response){

            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        })
    }

    $('#MessageText').on('keypress', function(event) {
        if (event.key === 'Enter') {
            scrollDown();
            event.preventDefault(); // Prevent the default newline action
            let message = $("#MessageText").val(),
                phone_receptor = $("#PhoneReceiver").val();
            const formDataMessage = new FormData();

            formDataMessage.append("message_date", date);
            formDataMessage.append("phone_message_receptor", phone_receptor);
            formDataMessage.append("message_time", time);
            formDataMessage.append("phone_message_sender", phone_sender);
            formDataMessage.append("message_status", statusMessage);
            formDataMessage.append("message_text ", message);
            if (phone_receptor !== "") {
                // sendDetectMessage(phone_sender, phone_receptor, message);
              if(message !== ""){
                  sendMessage(formDataMessage);
                  $("#MessageText").val("");
              }
            } else {
               let id = atob(searchParams.get('li'));
                GetFriends(id);
            }
            window.audioBlob = new Blob([null], {type: 'audio/wav'});// Update the recording state

        }
    });

    function Loadsearch(username){
        let id = atob(searchParams.get('pe'));
        $.ajax({
            type: 'POST',
            url: `/messagingClub/searchFriend/${username}/${id}`,
            contentType: false, // Prevent jQuery from overriding the content type
            cache: false,
            processData: false, // Prevent jQuery from processing the data
            success : function (response){
                // console.log(response);
                // friend = response.data;
                $("#ChatList").html(response);
                $('.show-conversation').each(function() {
                    // Add a click event listener to each element
                    $('.show-conversation').click(function() {
                        $("#MessageText").focus();
                        scrollDown();
                        const userSelectedName = $("#ConnectedUser");
                        $("#PhoneReceiver").val($(this).data("user-phone"));
                        userSelectedName.html($(this).attr("id") +`<br> <span>`+$(this).attr("status")+`</span>`);
                        ReadStatus($(this).data("user-phone"));
                        $("#SelectedUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        $(".convUserImg").attr('src',  "/Images/"+$(this).attr("image")+"");
                        Conversation.addClass("showMessages");
                        ListFriends.addClass("Hidelist");
                        DisplayConversation();
                    });
                });

                $('.imgbx').each(function(){
                    $('.imgbx').click(function(){
                        $('.modal-content').css({
                            'background-image': 'url(/Images/' + $(this).attr("imageProf") + ')',
                            'background-size': 'cover',
                            'background-repeat': 'no-repeat',
                            'background-attachment': 'fixed'
                        });

                        modal.css({
                            'display': 'flex',
                        });
                        // Use flex to center content
                        setTimeout(() => {
                            modal.addClass('show'); // Trigger the show class after display
                        }, 10); // Small delay to allow rendering
                    });
                });
            }
        });
    }

    function DisplayConversation(){
        let phone_receptor = $("#PhoneReceiver").val();
        const ContactConversations = new FormData();
        ContactConversations.append("phone_message_sender", phone_sender);
        ContactConversations.append("phone_message_receptor", phone_receptor);
        if (phone_receptor !== "") {
            $.ajax({
                url: '/messagingClub/displayConversation',
                method: 'POST',
                data: ContactConversations,
                cache: false,
                processData: false, // Prevent jQuery from processing the data
                contentType: false, // Prevent jQuery from overriding the content type
                success: function(Conversation) {
                    $(".chat-box").html(Conversation);
                    $(".incomming .details p ").css({
                        'background':'hsl(230,75%,56%)',
                        'color':'#fff',
                        'align-items':'center',
                        'text-align':'center)',
                        'justify-content':'center'
                    });

                    $("#noconversation").css({
                        'text-align':'center)',
                        'justify-content':'center'
                    })
                },
                error: function(xhr, status, error) {
                    console.log('displayed failed:', error);
                }
            });
        }

    }

    function ReceivingMessageSound(){
            // audioPlaySendSound.play();
            // Specify the path to the audio file
            const audioReceivingFilePath = '/MessageSounds/confirmationtone.wav';
            // Create an AJAX request to fetch the audio file
            const xhr = new XMLHttpRequest();
            xhr.open('GET', audioReceivingFilePath, true);
            xhr.responseType = 'blob';

            xhr.onload = function() {
                if (this.status === 200) {
                    const audioBlobs = this.response;
                    const audioUrls = URL.createObjectURL(audioBlobs);

                    // Create a new Audio object
                    const audioPlayer = new Audio(audioUrls);
                    audioPlayer.play();
                    ValidateNotification();
                } else {
                    console.error('Failed to load audio file:', this.status);
                }
            };

            xhr.onerror = function() {
                console.error('Error during the AJAX request.');
            };

            xhr.send();
    }

    function SendingMessageSound(){
        // const audioPlaySendSound = $('#audioPlaySendSound');
        // audioPlaySendSound.play();

        // Specify the path to the audio file
        const audioFilePath = '/MessageSounds/MessageSent.wav';

        // Create an AJAX request to fetch the audio file
        const xhr = new XMLHttpRequest();
        xhr.open('GET', audioFilePath, true);
        xhr.responseType = 'blob';

        xhr.onload = function() {
            if (this.status === 200) {
                const audioBlob = this.response;
                const audioUrl = URL.createObjectURL(audioBlob);

                // Create a new Audio object
                const audioPlayer = new Audio(audioUrl);
                audioPlayer.play(); // Start playback
            } else {
                console.error('Failed to load audio file:', this.status);
            }
        };

        xhr.onerror = function() {
            console.error('Error during the AJAX request.');
        };

        xhr.send();
    }

    function ReceiveNotificationFrom(){
        $.ajax({
            type: 'POST',
            url: `/messagingClub/notifyMe/${phone_sender}`,
            contentType: 'application/json',
            dataType:"json",
            success: function(response){
                showNotification(response.phoneReceptor, response.MessageText);
                MyPhone = response.phoneReceptor;
                SendMe = response.notify;
                if(MyPhone === phone_sender){
                    if(SendMe === "SendNotification"){
                        id = atob(searchParams.get('li'));
                        GetFriends(id);

                        ReceivingMessageSound();
                        DisplayConversation();
                    }

                }
            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        })
    }

    function ValidateNotification(){
        let   phone_sender = atob(searchParams.get('pe')),
            InfoNotificationReceiver = new     FormData();
        InfoNotificationReceiver.append("PhoneSender",phone_sender);
        $.ajax({
            type: 'POST',
            url: `/messagingClub/validateNotification`,
            data:InfoNotificationReceiver,
            contentType: false, // Prevent jQuery from overriding the content type
            cache: false,
            processData: false,
            success: function(response){

            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        })
    }

    function showNotification(title, body) {
        // Check if the user has granted permission to display notifications
        if (Notification.permission === 'granted') {
            const notification = new Notification(title, {
                body: body,
                icon: '/favicon.ico', // Replace with your notification icon
            });
        } else if (Notification.permission !== 'denied') {
            Notification.requestPermission().then(permission => {
                if (permission === 'granted') {
                    const notification = new Notification(title, {
                        body: body,
                        icon: '/favicon.ico', // Replace with your notification icon
                    });
                }
            });
        }
    }

});