(function() {
    "use strict";

    var parseURL = function() {
        var urlParts = document.URL.split("?");
        if(urlParts.length > 1) {
            if (urlParts[1] === "error") {
                swal({
                title: "Error!",
                text: "Error occured when uploading! Check if the course code is valid. Leave the course code field blank otherwise.",
                icon: "error",
              });
            }

            if (urlParts[1] === "crawler-success") {
                swal({
                    title: "Success!",
                    text: "You have successfully started a crawl. You will be notified when it is complete",
                    icon: "success",
                  });
            }
        }
    }

    parseURL();

    $("#upload-files-button").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait.",
            icon: "./media/icons/loading.gif",
            buttons: false
        });
    });

    $("#crawler-upload-files-button").click(function() {
        swal({
            title: "Processing!",
            text: "Please wait. This may take a few minutes.",
            icon: "./media/icons/loading.gif",
            buttons: false
        });
    });

    $(document).ready(function(){
        $("#tab-menu .item").tab({history:false});
    });

})();
