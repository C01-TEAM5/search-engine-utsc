(function(){

    var getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;
    
        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
    
            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

    var userId = api.getUserId();
    var isOwner = false;
    var editted = false;
    

    $(".profile-container .user").html("");

    $('.special.cards .image').dimmer({
        on: 'hover'
    });

    $("#edit-username").click(function() {
        editted = true;
        $("#input-username").removeClass("transparent");
        $("#input-username").addClass("action");
        $("#username").prop('disabled', false);
    });

    $("#edit-desc").click(function() {
        editted = true;
        $("#input-desc").removeClass("transparent");
        $("#input-desc").addClass("action");
        $("#userDesc").prop('disabled', false);
    });

    $("#save-profile-button").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        var name = $("#username").val();
        var desc = $("#userDesc").val();
        api.updateUser(userId, name, desc, "", function(err, data) {
            if (err) {
                swal.close();
                swal("Error!", "An error occured \n" + err, "error");
            }
            else {
                swal.close();
                editted = false;
                location.reload();
            }
        });
    });

    $("#course-add").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        var courseId = $("#course-add-input").val();
        if (courseId != "") {
            api.createCourse(courseId, function(err, data) {
                if (err) {
                    swal.close();
                    swal("Error!", "An error occured \n" + err, "error");
                }
                else {
                    swal.close();
                    window.location.replace("/course?id="+courseId);
                }
            });
        }
        $("#course-add-input").val("");
    });

    $(".remove-file-button").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        var id = $(this).attr("id");
        api.removeUserFile(id, function(err, data) {
            if (err) {
                swal.close();
                swal("Error!", "An error occured \n" + err, "error");
            }
            else {
                swal.close();
                swal("Success!", "File removed \n", "success");
                $("#" + id + "-item").remove();
            }
        });
    });

    $("#unfollow-user").on("click", unfollow);

    $("#follow-user").on("click", follow);


    function follow() {
        console.log("clicking foloow");
        api.followUser(getUrlParameter("id"), false, function(err, data){
            if (err) {
                console.log(err);
            } else {
                console.log(data);
                $("#follow-user").attr("id", "unfollow-user");
                $("#unfollow-user").html(`<i class="address book outline icon"></i>Unfollow`);
                $("#unfollow-user").unbind("click");
                $("#unfollow-user").on("click", unfollow);
            }
        });
    }

    function unfollow() {
        console.log("clicking unfoloow");
        api.followUser(getUrlParameter("id"), true, function(err, data){
            if (err) {

            } else {
                $("#unfollow-user").attr("id", "follow-user");
                $("#follow-user").html(`<i class="address book outline icon"></i>Follow`);
                $("#follow-user").unbind("click");
                $("#follow-user").on("click", follow);
            }
        });
    }

    function loadStats() {
        // code gotted from google developer site

        // Load the Visualization API and the corechart package.
        google.charts.load('current', {'packages':['corechart']});

        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart);

        // Callback that creates and populates a data table,
        // instantiates the pie chart, passes in the data and
        // draws it.
        //console.log(eval('${docxNum}'));
        var x = "${docxNum}";
        console.log(x);
        function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'File Type');
        data.addColumn('number', 'Occurence');
        data.addRows([
            ['DOCX', parseInt($("#docxNum").html())],
            ['HTML', parseInt($("#htmlNum").html())],
            ['PDF', parseInt($("#pdfNum").html())],
            ['TXT', parseInt($("#txtNum").html())]
        ]);

        // Set chart options
        var options = {'title':'File Statistics'};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('user-file-stats'));
            chart.draw(data, options);
        }

    }

    loadStats();

    window.onbeforeunload = function() {
        if (editted) {
            return 'Are you sure you want to leave?';
        }
    }

}());