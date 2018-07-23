(function() {
    "use strict";

    var currentUser = api.getCurrentUser();
    $(".isOwner").hide();
    $(".file-info-edit-item input").prop('disabled', true);
    $(".file-container .user").html("");

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

    $("#remove-file-button").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false
        });
        var id = getUrlParameter("id");
        api.removeUserFile(id, function(err, data) {
            if (err) {
                swal.close();
                swal("Error!", "An error occured \n" + err, "error");
            }
            else {
                swal.close();
                swal("Success!", "File removed \n", "success");
                window.location.replace("/");
            }
        });
    });

    $("#edit-file-name").click(function() {
        $("#file-name-input").prop('disabled', false);
    });

    $("#edit-courseId").click(function() {
        $("#courseId-input").prop('disabled', false);
    });

    $("#save-file-info").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "../media/icons/loading.gif",
            buttons: false,
            allowOutsideClick: false,
            closeOnClickOutside: false
        });
        var id = getUrlParameter("id");
        var name = $("#file-name-input").val();
        var courseId = $("#courseId-input").val();
        var permSelect = document.getElementById("file-permissions");
        var perm = permSelect.options[permSelect.selectedIndex].value;
        api.updateFile(id, name, courseId, perm, function(err, data) {
            if (err) {
                swal.close();
                swal("Error!", "An error occured \n" + err, "error");
            }
            else {
                swal.close();
                console.log(data);
                window.location.replace("/file?id=" + data);
            }
        });
    });

    function init() {
        api.getFile(getUrlParameter("id"), function(err, data) {
            if (err) {
                console.log("err", err);
            }
            else {
                if (data["owner"] === currentUser) {
                    $("#file-permissions").removeClass("disabled");
                    $(".isOwner").show();
                    $('#file-permissions').dropdown();
                    
                }
                else {
                    $("#file-permissions").addClass("disabled");
                    $('#file-permissions').dropdown();
                }
            }
        });

        console.log($(".file-preview").html().length);

        if ($(".file-preview").html() === "") {
            console.log("preview empty");
        }
    }


    init();

}())
