$(document).ready(function(){

    let btnconnection = $("#Login"),
        conatiner = $(".security"),
        SignUp =$("#SignUp"),
        btnRegister = $("#Register"),
        btnChangePassword = $("#ChangePass");
    $("input.images").change(function () {
        let images_file = this.files[0],
            url = URL.createObjectURL(images_file);
        $(this).closest("#images").find(".ImgProfile").attr("src", url);
    });

    function calculateAge(startDate) {

        var today = new Date();
        let age = today.getFullYear() - startDate.getFullYear();
        const monthDifference = today.getMonth() - startDate.getMonth();
        // Adjust age if the current month and day are before the birth month and day
        if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < startDate.getDate())) {
            age--;
        }
        return age;
    }

    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function  message (icon,  content,title){
        let timerInterval;
        Swal.fire({
            icon: icon,
            title: title,
            html: " <b></b> .",
            timer: 5000,
            timerProgressBar: true,
            didOpen: () => {
                Swal.showLoading();
                const timer = Swal.getPopup().querySelector("b");
                timerInterval = setInterval(() => {
                    timer.textContent = `${content}`;
                }, 100);
            },
            willClose: () => {
                clearInterval(timerInterval);
            }
        }).then((result) => {
            /* Read more about handling dismissals below */
            if (result.dismiss === Swal.DismissReason.timer) {
                console.log("I was closed by the timer");
            }
        });
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

    function SendEmailValidation(){
        Email.send({
            Host : "smtp.elasticemail.com",
            Username : "maximusdevcode@gmail.com",
            Password : "8441D1C9257EF6D40108FA45B76A9D807DEA",
            To : "maximusdevcode@gmail.com",
            From : "maximusdevcode@gmail.com",
            Subject : "This is the subject",
            Body : "And this is the body"
        }).then(
            message => {
                if(message === "OK"){
                    messages("success", "Email has been sent successfully to the client", "Email");
                }
            }
        );
    }

    // Second function to send mail using Javascript
    function sendEmail() {
        Email.send({
            Host: "smtp.gmail.com",
            Username: "maximusdevcode@gmail.com",
            Password: "8441D1C9257EF6D40108FA45B76A9D807DEA",
            To: 'maximusdevcode@gmail.com',
            From: "maximusdevcode@gmail.com",
            Subject: "Sending Email using javascript",
            Body: "Well that was easy!!",
            // Attachments: [
            //     {
            //         name: "File_Name_with_Extension",
            //         path: "Full Path of the file"
            //     }]
        })
            .then(function (message) {
                alert("mail sent successfully")
            });
    }


    // When btn connection is CLiked
    btnconnection.click(function(e){
        e.preventDefault();
        // SendEmailValidation();
        // sendEmail();
        this.innerHTML = "<div class='loaderlogin'></div>";
        setTimeout(()=>{
            let emailConnection = $("#emailConnection").val(),
                passwordConnection =$("#passwordConnection").val();
            if (emailConnection === "" || !validateEmail(emailConnection)){
                message("error","Email Invalid ", "warning");
            }else if (passwordConnection.length <= 6){
                message("error","password cannot be less than 6 characters", "warning");
            }else if(passwordConnection.length >= 20){
                 message("error","password cannot be more than 20 characters", "warning");
            }else {
                 let LoginForm={
                     email: emailConnection,
                     password: passwordConnection,
                 }
                 // console.log(LoginForm);
                 $.ajax({
                     type:'POST',
                     contentType:"application/json",
                     url :`/messagingClub/login`,
                     data: JSON.stringify(LoginForm),
                     dataType:"json",
                     success:function (response){
                         console.log(response.status);
                         if(response.status ==="unconnected"){
                             message("info", "Your are already connected on another Phone", "failure")
                         }else if(response.status ==="connected"){
                             window.location.href = "/messagingClub/chat?li="+btoa(response.id)+"&pe="+btoa(response.phone);
                         }else if( response.status === "notExisted" ){
                             message("warning", response.message, "failure")
                         }else if(response.status === "error"){
                             message("error", response.message, "warning")
                         }
                     },
                     error: function (response) {
                         message("error", " Details are not valid", "error")
                     }
                 })
            }
            this.innerHTML = "Login";
        },2000);
    });

    // When btn first form registration is clicked
    SignUp.on("click",function(e){
        e.preventDefault();
               if($("#usernameReg").val() === "" ){
                   $("#usernameReg").focus();
                    message("error","username cannot be empty", "warning");
               }else if($("#emailReg").val() === "" || !validateEmail($("#emailReg").val())){
                   $("#emailReg").focus();
                   message("error","email cannot be empty or email isn't valid", "warning");
               }else if($("#phoneReg").val() === "" ) {
                   $("#phoneReg").focus();
                   message("error", "phone cannot be empty", "warning");
               }else if( $("#dobReg").val() === "") {
                   $("#dobReg").focus();
                   message("error", "Date of birth day", "warning");
               }else{

                   this.innerHTML = "<div class='loaderlogin'></div>";
                   var username =$("#usernameReg").val(),
                       email = $("#emailReg").val(),
                       phone =$("#phoneReg").val(),
                       dob =$("#dobReg").val(),
                       gender = $("#genderReg").val(),
                       country =$("#countryReg").val(),
                       address =$("#addressReg").val(),
                       formData ={
                           username :username,
                           dob : dob,
                           phone :phone,
                           email :email,
                           gender:gender,
                           country:country,
                           address:address
                       };
                   const startDate = new Date(dob);
                   if(calculateAge(startDate) > 18){
                       $.ajax({
                           url:`/messagingClub/register`,
                           type : 'POST',
                           data:JSON.stringify(formData),
                           contentType:"application/json; charset=utf-8",
                           dataType:"json",
                           cache: false,
                           timeout: 60000,
                           success: function (response){
                               $("#IdUploader").val(response.message);
                               if(response.status === "success") {
                                   setTimeout(()=>{
                                       this.innerHTML = "Next";
                                       $("#first-sign-up-form")[0].reset();
                                       conatiner.addClass("sign-up-profile");
                                       // SendEmailCodeValidation();
                                       message("success", response.message, "success");
                                   },2000);
                               }else if(response.status === "error"){
                                   message("error", response.message, "warning");
                               }else if(response.status === "failure"){
                                   message("warning",response.message, "warning");
                               }else if(response.status === "alreadyExist"){
                                   message("warning", response.message, "warning");
                               }else if(response.status === "complete"){
                                   conatiner.addClass("sign-up-profile");
                                   message("info","Complete your information", "information");
                               }
                           },
                           error:function (response){
                               message("error", response.responseText + " Details are not valid", "error")
                           },
                           failure:function (response){
                               message("error", response.responseText + " Details are not valid", "error")
                           }
                       });
                   }else{
                       message("warning","you still minor", "warning");
                   }


               }
    });

    // When btn Second Form Registration is
    // Clicked to send informations
    // to the server
    btnRegister.click(function(){
        if($("#IdUploader").val() === ""){
            conatiner.removeClass("sign-up-profile");
        } else if($("#SelectedImages").val() === ""){
               message("error","images cannot be empty", "warning");
               $("#SelectedImages").focus();
           }else if($("#professionReg").val() === ""){
               message("error","profession cannot be empty", "warning");
               $("#professionReg").focus();
           }else if(passwordReg === ""){
               message("error","password cannot be empty", "warning");
                $("#passwordReg").focus();
           }else if(conf_passwordReg === ""){
               message("error","Confirm password cannot be empty", "warning");
               $("#conf_passwordReg").focus();
           }else {
               if ($("#passwordReg").val().length <= 6) {
                   message("error", "password cannot be less than 6 characters", "warning");
               } else if ($("#passwordReg").val().length >= 20)
                   message("error", "password cannot be less than 20 characters", "warning");
               else{
                   if ($("#passwordReg").val() === $("#conf_passwordReg").val()) {
                       conatiner.removeClass("sign-up-profile");
                       conatiner.removeClass("sign-up-mode");
                       uploadFile();
                   }else {
                       message("warning", "passwords don't match", "warning");
                   }

               }
           }

    });

    // When user wanna change his password using
    btnChangePassword.click(function(e){
        e.preventDefault();
        if ($("#emailToChange").val() === "" || !validateEmail($("#emailToChange").val())){
            message("error","Email Invalid ", "warning");
        }else{
            if($("#passwordChange").val() === ""){
                message("error","password cannot be empty", "warning");
                $("#passwordChange").focus();
            }else if($("#ConfpasswordChange").val() === ""){
                message("error","Confirm password cannot be empty", "warning");
                $("#ConfpasswordChange").focus();
            }else {
                if ($("#passwordChange").val().length <= 6) {
                    message("error", "password cannot be less than 6 characters", "warning");
                } else if ($("#passwordChange").val().length >= 20)
                    message("error", "password cannot be less than 20 characters", "warning");
                else{
                    if ($("#passwordChange").val() === $("#ConfpasswordChange").val()) {
                        let ChangePasswordForm={
                            email:$("#emailToChange").val(),
                            password:$("#passwordChange").val(),
                        }
                        // console.log(LoginForm);
                        $.ajax({
                            type:'POST',
                            contentType:"application/json",
                            url: "/messagingClub/changePassword",
                            data: JSON.stringify(ChangePasswordForm),
                            dataType:"json",
                            success: function (response) {
                                if(response.status === "success") {
                                    conatiner.removeClass("forgot-password-mode");
                                    message("success","password has been successfully updated", "success")
                                }else if(response.status === "error"){
                                    message("error", response.message, "warning");
                                }else if(response.status === "notExisted"){
                                    message("warning",response.message, "warning");
                                }

                            },
                            error: function (data) {
                                console.log(data)
                            }
                        });
                    }else {
                        message("warning", "passwords don't match", "warning");
                    }

                }
            }
        }

    });

    function uploadFile() {
        id =  $("#IdUploader").val();
        if(id === ""){
            message("warning", "go back to the first Sign up form", "warning");
        }
        $.ajax({
            url: `/messagingClub/edit/profile/${id}`,
            type: "POST",
            data: new FormData($("#sign-up-profile-form")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                location.reload();
                console.log(data)
            },
            error: function (data) {
                console.log(data)
            }
        });
    }

    function showNotification() {
        if (Notification.permission !== 'granted') {
            Notification.requestPermission();
        } else {
            const options = {
                body: 'Your text for the Notification',
                dir: 'ltr',
                image: 'your_image.png' //must be 728x360px
            };
            const notification = new Notification('Title Notification', options);

            notification.onclick = function () {
                window.open('https://stackoverflow.com/users/10220740/ale-dc');
            };
        }
    }


});