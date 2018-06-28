(function(){

    "use strict";

    window.onload = function() {

        // add all the js code in here, this will make sure all code follws proper code format
        // also it will synchrize the frontend, html files

        // use this file for the controls of the search, buttons api calls, redirects, etc.
        const submit = document.getElementById("submit");
        const search = document.getElementById("search");
        const searchTxt = document.getElementById("searchTxt");
        const searchPdf = document.getElementById("searchPdf");
        const searchHtml = document.getElementById("searchHtml");
        const searchDocx = document.getElementById("searchDocx");

        function submitter (event) {

            event.preventDefault();

            var filterList = [];
            const expandSearch = true;
            const searchQuery = search.value;

            if (expandSearch) {filterList.push("eSrch")}
            if (searchTxt.checked) {filterList.push("sTxt")}
            if (searchPdf.checked) {filterList.push("sPdf")}
            if (searchHtml.checked) {filterList.push("sHtml")}
            if (searchDocx.checked) {filterList.push("sDocx")}
            api.search(searchQuery, filterList, function(error, result) {
                if (result != null) {
                    alert(result);//Handle results
                } else {
                    alert("error");//Handle errors
                }
            });


        }

        submit.addEventListener('click', submitter);

    };

}());
