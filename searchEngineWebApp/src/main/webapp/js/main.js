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

            if (searchTxt.checked) {filterList.push("eTxt")}
            if (searchPdf.checked) {filterList.push("ePdf")}
            if (searchHtml.checked) {filterList.push("eHtml")}
            if (searchDocx.checked) {filterList.push("eDocx")}
            var queryString = api.buildQuery(searchQuery,filterList);
            document.location.href="/search?query="+queryString;


        }

        submit.addEventListener('click', submitter);

        // uploader filter buttons
        $(".uploader-filter button").click(function(){
            $(".uploader-filter button").removeClass("active");
            $(this).addClass("active");
        });

    };

}());
