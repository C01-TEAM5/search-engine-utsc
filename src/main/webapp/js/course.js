(function(){

    "use strict";

    $(".course-content .user").html("");

    $(".bar-item").click(function() {
        $(".bar-item").removeClass("bar-active");
        $(this).addClass("bar-active");
        $(".content-info-item").removeClass("content-info-item-active");
        $("#" + $(this).attr('id') + "-info").addClass("content-info-item-active");
    });

    $("#courses-upload-text").val("");
    $('#courses-upload').change(function(e){
        var fileName = e.target.files[0].name;
        $("#courses-upload-text").val(fileName);
    });

    $("#courses-upload-btn").click(function() {
        if ($("#courses-upload-text").val() !== "") {
            var data = new FormData();
            $.each($('#courses-upload')[0].files, function(i, file) {
                data.append('file-'+i, file);
            });
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/upload?id=" + $("#courseID").html(),
                data: data,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (data) {

                    $("#courses-upload-text").val("");
                    console.log("SUCCESS : ", data);
                    swal("Success!", "File uploaded successfully", "success");
                },
                error: function (e) {

                    $("#courses-upload-text").val("");
                    console.log("ERROR : ", e);
                    swal("Error!", "Cannot upload file: " + e, "error");
                }
            });
        }
    });

    $("#course-save-info").click(function() {
        alert("not yet implemented");
    });

    $("#course-add-instructor").click(function() {
        alert("not yet implemented");
    });

    $("#course-add-student").click(function() {
        alert("not yet implemented");
    });

}());