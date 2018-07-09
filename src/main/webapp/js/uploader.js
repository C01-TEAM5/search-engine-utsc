(function() {
    "use strict";

    var parseURL = function() {
        var urlParts = document.URL.split("?");
        if(urlParts.length > 1) {
            if (urlParts[1] === "error") swal({
                title: "Error!",
                text: "Please login to Upload!",
                icon: "error",
              });
        }
    }

    parseURL();

})();
