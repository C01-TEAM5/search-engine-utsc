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
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
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
                    swal.close();
                    $("#courses-upload-text").val("");
                    swal("Success!", "File uploaded successfully", "success");
                    console.log("Over here", data);
                    setFiles(JSON.parse(data));
                },
                error: function (e) {
                    swal.close();
                    $("#courses-upload-text").val("");
                    console.log("ERROR : ", e);
                    swal("Error!", "Cannot upload file: " + e, "error");
                }
            });
        }
    });

    $("#course-save-info").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        api.editCourse($(".courseID").html(), $("#newCode").val(), $("#newName").val(), $("#newDesc").val(), $("#newSize").val(), function(err, data) {
                if (err) {
                    swal.close();
                    swal("Error!", "Could not update course : \n" + err, "error");
                }
                else {
                    swal.close();
                    swal("Success!", "Successfully updated course.", "sucess");
                    window.location.replace("/course?id=" + $("#newCode").val());
                }
            }
        );
    });

    $("#course-add-instructor").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        var instUser = $("#course-add-instructor-input").val();
        if (instUser && instUser !== "") {
            api.addInstructor($(".courseID").html(), instUser, function(err, data) {
                if (err) {
                    console.log(err);
                    swal.close();
                    swal("Error!", "Failed to add Instructor : \n" + err, "error");
                }
                else {
                    swal.close();
                    swal("Success!", "Successfully added new Instructor.", "sucess");
                    setInstructors(data["instructors"]);
                }
            });
        }
        $("#course-add-instructor-input").val("");
    });

    $("#course-add-student").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        console.log("hello adding student");
        var student = $("#course-add-student-input").val();
        if (student && student !== "") {
            api.addStudent($(".courseID").html(), student, function(err, data) {
                if (err) {
                    swal.close();
                    console.log(err);
                    swal("Error!", "Failed to add Student : \n" + err, "error");
                }
                else {
                    swal.close();
                    swal("Success!", "Successfully added new Student.", "sucess");
                    setStudents(data["students"]);
                }
            });
        }
        $("#course-add-student-input").val("");
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
                                $("#"+id+"-list-item").remove();
                            }
                        });
                    });
                }
            });
        });
    }

    function setStudents(data) {
        $("#students-list .custom-list").html("");
        data.forEach(function(id) {
            console.log(id);
            api.getUser(id, function(err, result) {
                if (err) {
                    console.log(err);
                }
                else {
                    $("#students-list .custom-list").append(`
                    <div class="ui raised segment" id="${id}-list-item">
                        <div class="ui list">
                            <div class="item">
                                <i class="user icon"></i>
                                <div class="content">
                                    <a href="/profile?id=${id}">${result["name"]}</a>
                                    <button class="ui button remove-student-button" id="${id}">Remove</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    `);

                    $("#"+id).click(function() {
                        console.log("something");
                        //$("#"+$(this).attr("id")+"-list-item").remove();
                        api.removeStudent($(".courseID").html(), $(this).attr("id"), function(err, res) {
                            if (err) {
                                console.log(err);
                            }
                            else {
                                $("#"+id+"-list-item").remove();
                            }
                        });
                    });
                }
            });
        });
    }

    function setFiles(data) {
        console.log(data);
        $("#course-files-info #files-list .custom-list").html("");
        $(".courseNumSize").html(data.length);
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
                                $("#"+id+"-list-item").remove();
                            }
                        });
                    });
                }
            });
        });
    }

    function setInfo(data) {
        //code
        //name
        //desc
        //size
        $(".courseID").html(data["code"].toUpperCase());
        $(".courseName").html(data["name"]);
        $(".courseDesc").html(data["description"]);
        $(".courseSize").html(data["size"]);

    }

    function init() {
        console.log($(".courseID").html());
        api.getCourse($(".courseID").html(), function(err, data) {
            if (err) {
                console.log(err, "its in the init");
            }
            else {
                console.log(data["instructors"]);
                setInfo(data);
                setInstructors(data["instructors"]);
                setStudents(data["students"]);
                setFiles(data["files"]);
            }
        });
    }

    init();

}());