$(document).ready(function(){
    GetAllContact();
    let btn = $("#btnSaveContact"),
        btnUpdateContact = $("#btnUpdateContact"),
        btnclose = $("#btnClose"),
        btn_close =$(".btn-close");
    function  message (icon,  content,title){
        Swal.fire({
            icon: icon,
            title: title,
            text: content
        });
        return;
    }

    btn.click(function (e){
        e.preventDefault();
        CheckIfIsEmptyOrNotBeforeCreating();
    });

    btnUpdateContact.click(function (e){
        e.preventDefault();
        UpdateContacts ();
    });
    btn_close.click(function (e){
        btn.prop('disabled', false);
        e.preventDefault();
        clearField();
    });
    btnclose.click(function (e){
        btn.prop('disabled', false);
        e.preventDefault();
        clearField();
    });
    function clearField() {
        var id = $("#id").val(""),
            firstname =$("#firstname").val(""),
            lastname = $("#lastname").val(""),
            phone =$("#phone").val(""),
            email =$("#email").val("");
    }
    function CheckIfIsEmptyOrNotBeforeCreating() {
        var FormData =document.forms['ContactForm'];
        if (FormData.firstname.value === ""
            || FormData.lastname.value === ""
            || FormData.email.value === ""
            || FormData.phone.value === "") {
            message("error","All fields are required for the Contact form","warning");
            return false;
        }else{
            var firstname =$("#firstname").val(),
                lastname = $("#lastname").val(),
                phone =$("#phone").val(),
                email =$("#email").val(),
                FormData ={
                    firstname :firstname,
                    lastname : lastname,
                    phone :phone,
                    email :email
                };

            $.ajax({
                type:'POST',
                contentType:"application/json",
                url :'saveContact',
                data: JSON.stringify(FormData),
                dataType:"json",
                success:function (response){
                    if(response.status === "success") {
                        message("success", response.message, "Add contact")
                        clearField();
                        window.location.reload();
                    }else if( response.status === "error" ){
                        message("error",response.message, "warning")
                        clearField();
                    }else if(response.status === "alreadyExist"){
                        message("error",response.message, "warning")
                        $("#email").val("");
                    }
                }
            });
            return true;
        }
    }

    function UpdateContacts (){
        var FormData =document.forms['ContactForm'];
        if (FormData.firstname.value === ""
            || FormData.lastname.value === ""
            || FormData.email.value === ""
            || FormData.phone.value === "") {
            message("error","All fields are required for the Contact form","warning");
            return false;
        }else{
            var firstname =$("#firstname").val(),
                lastname = $("#lastname").val(),
                phone =$("#phone").val(),
                email =$("#email").val(),
                FormData ={
                    firstname :firstname,
                    lastname : lastname,
                    phone :phone,
                    email :email
                };

            $.ajax({
                type:'POST',
                contentType:"application/json",
                url :`/UpdateContact/${id}`,
                data: JSON.stringify(FormData),
                dataType:"json",
                success:function (response){
                    if(response.status === "success") {
                        message("success", response.message, "Add contact")
                        clearField();
                        window.location.reload();
                    }else if( response.status === "error" ){
                        message("error",response.message, "warning")
                        clearField();
                    }else if(response.status === "alreadyExist"){
                        message("error",response.message, "warning")
                        $("#email").val("");
                    }
                }
            });
            return true;
        }
    }

    function  GetAllContact(){
        $.ajax({
            type: 'GET',
            url: 'getContact',
            contentType: 'application/json',
            dataType:"json",
            success : function (response){
                if(response.status =="success"){
                    var ContactList ="";
                    $.each(response.data, function (i, contact){
                        ContactList = "<tr>" +
                            "<td>"+contact.id+"</td>" +
                            "<td >"+contact.firstname+"</td>" +
                            "<td >"+contact.lastName+"</td>" +
                            "<td >"+contact.phone+"</td>" +
                            "<td >"+contact.email+"</td>" +
                            "<td>" +
                            "<a class='btn btn-sm btn-light btn-edit' id='"+ contact.id +"'>" +
                            "<i class='fa-solid fa-pen-to-square text-success'></i></a>" +
                            "<a class='btn btn-sm btn-light btn-delete' id='"+ contact.id +"'>" +
                            "<i class='fa-solid fa-trash text-danger'></i>" +
                            "</a> </td></tr>";
                        $("#ListContact").append("");
                        $("#ListContact").append(ContactList);
                    });
                }

            }
        })
    }

    function DeleteContact(){
        $.ajax({
            type: 'DELETE',
            url: `/contact/deleteContact/${id}`,
            contentType: 'application/json',
            dataType:"json",
            success : function (response){
                window.location.reload();
                message("success","deleted successfully", id);
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

    $(".ContactBody").on("click",".btn-delete",function(e){
        e.preventDefault();
        id = $(this).attr('id');
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this! for Id : "+id,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
        }).then((result) => {
            if (result.isConfirmed) {
                DeleteContact();
            }
        });

    });

    $("body").on("click",".btn-edit",function (e){
        btn.prop('disabled', true);
        $('#ContactModal').modal('show');
        e.preventDefault();
        id = $(this).attr('id');
        $.ajax({
            type: 'GET',
            url: `/contact/updateContact/${id}`,
            contentType: 'application/json',
            dataType:"json",
            success : function (data){
                $("#id").val(data.id);
                $("#firstname").val(data.firstname);
                $("#lastname").val(data.lastName);
                $("#phone").val(data.phone);
                $("#email").val(data.email);
            },
            fail : function (e){
                e.preventDefault();
                console.log("fail");
            },
            error:function (){
                console.log("error");
            }
        });
    });


});