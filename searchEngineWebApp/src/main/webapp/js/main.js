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
            
            if (search.value=="") return;
          
            if (!searchTxt.checked && !searchPdf.checked &&
            		!searchHtml.checked && !searchDocx.checked){
            	filterList.push(".txt");
            	filterList.push(".pdf");
            	filterList.push(".html");
            	filterList.push(".docx");
            }
            if (searchTxt.checked) {filterList.push(".txt")}
            if (searchPdf.checked) {filterList.push(".pdf")}
            if (searchHtml.checked) {filterList.push(".html")}
            if (searchDocx.checked) {filterList.push(".docx")}
            
            var queryString = api.buildQuery(searchQuery,filterList,perm);
            document.location.href="/search?query="+queryString;


        }

        submit.addEventListener('click', submitter);

        var perm = 0;
        // uploader filter buttons
        $(".uploader-filter button").click(function(){
            $(".uploader-filter button").removeClass("active");
            $(this).addClass("active");
            
            perm = 0;
            
            if (this.id == "perm-all") {
            	perm = 0;	
            } else if (this.id == "perm-instructor") {
            	perm = 3;
            } else if (this.id == "perm-student") {
            	perm = 2;
            }
        });

    };

}());
