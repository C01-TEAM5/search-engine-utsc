(function(){

    "use strict";

    $("#showFiles").click(function() {
        $(".course-content-item").removeClass("item-active");
        $("#file-list").addClass("item-active");
    });

    $("#showStudents").click(function() {
        $(".course-content-item").removeClass("item-active");
        $("#student-list").addClass("item-active");
    });

    $("#addStudent").click(function() {
        var username = $("#studentUsername").val();
        var courseID = $("#courseID").html();
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        api.addStudent(courseID, username, function(err, result) {
            if (err != null) {
                // error occured
                swal.close();
                swal("Error!", 
                    "Failed to add student. Please make sure to enter a valid username.", 
                    "error");
            }
            else {

                swal.close();
                swal("Success!", 
                    "Student added.", 
                    "success");
            }
        });
        $("#studentUsername").val("");
    });

    function init() {
        var instID = $("#instID").html();
        var user = api.getCurrentUser();

        if (user != null && instID === user) {
            $(".instructorOnly").css("display", "flex");
        }
    }

    init();

}());