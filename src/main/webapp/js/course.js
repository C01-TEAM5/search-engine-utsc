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
                url: "/upload?id=" + $(".courseID").html(),
                data: data,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (data) {

                    $("#courses-upload-text").val("");
                    swal("Success!", "File uploaded successfully", "success");
                    setFiles(data);
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
        var instUser = $("#course-add-instructor-input").val();
        if (instUser && instUser !== "") {
            api.addInstructor($(".courseID").html(), instUser, function(err, data) {
                if (err) {
                    console.log(err);
                    swal("Error!", "Failed to add Instructor : \n" + err, "error");
                }
                else {
                    swal("Success!", "Successfully added new Instructor.", "sucess");
                    setInstructors(data["instructors"]);
                }
            });
        }
        $("#course-add-instructor-input").val("");
    });

    $("#course-add-student").click(function() {
        alert("not yet implemented");
    });

    function setInstructors(data) {
        $("#instructors-list .custom-list").html("");
        data.forEach(function(id) {
            console.log(id);
            api.getUser(id, function(err, result) {
                if (err) {
                    console.log(err);
                }
                else {
                    $("#instructors-list .custom-list").append(`
                    <div class="ui raised segment" id="${id}-list-item">
                        <div class="ui list">
                            <div class="item">
                                <i class="user icon"></i>
                                <div class="content">
                                    <a href="/profile?id=${id}">${result["name"]}</a>
                                    <button class="ui button remove-instructor-button" id="${id}">Remove</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    `);

                    $("#"+id).click(function() {
                        console.log("something");
                        api.removeInstructor($(".courseID").html(), $(this).attr("id"), function(err, res) {
                            if (err) {
                                console.log(err);
                            }
                            else {
                                setInstructors(res["instructors"]);
                            }
                        });
                    });
                }
            });
        });
    }

    function setFiles(data) {
        $("#course-files-info #files-list .custom-list").html("");
        data.forEach(function(id) {
            api.getFile(id, function(err, result) {
                if (err != null) {
                    console.log(err);
                }
                else {
                    $("#course-files-info #files-list .custom-list").append(`
                    <div class="ui raised segment" id="${id}-list-item">
                        <div class="ui list">
                            <div class="item">
                                <i class="file icon"></i>
                                <div class="content">
                                    <a href="/file?id=${id}">${result["filename"]}</a>
                                    <button class="ui button remove-file-button" id="${id}">Remove</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    `);
                    $("#"+id).click(function() {
                        api.removeFile($(".courseID").html(), $(this).attr("id"), function(err, res) {
                            if (err) {
                                console.log(err);
                            }
                            else {
                                setFiles(res["files"]);
                            }
                        });
                    });
                }
            });
        });
    }

    function init() {
        console.log($(".courseID").html());
        api.getCourse($(".courseID").html(), function(err, data) {
            if (err) {
                console.log(err, "its in the init");
            }
            else {
                console.log(data["instructors"]);
                setInstructors(data["instructors"]);
                setFiles(data["files"]);
            }
        });
    }

    init();

}());