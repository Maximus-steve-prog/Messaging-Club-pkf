$(document).ready(function() {
    $("#btnUpload").on("click", uploadFile);
});

function uploadFile() {
    $.ajax({
        url: "/auth/edit/profile/7",
        type: "POST",
        data: new FormData($("#upload-file-form")[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
           console.log(data.responseText)
        },
        error: function (data) {
            console.log(data.responseText)
        }
    });
}