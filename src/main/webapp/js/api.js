//TO DO
var api = (function(){
    "use strict";

    function send(method, url, data, callback){
        var xhr = new XMLHttpRequest();
        xhr.onload = function() {
            if (xhr.status !== 200) callback("[" + xhr.status + "] " + xhr.responseText, null);
            else{
                try{
                    callback(null, JSON.parse(xhr.responseText));
                }
                catch(err) {
                    callback(null, "");
                }
            }
        };
        xhr.open(method, url, true);
        if (!data) xhr.send();
        else{
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send(JSON.stringify(data));
        }
    }

    var module = {};

    module.search = function(query, filters, callback) {
        var queryString = buildQueryString(query, filters);
        send("GET", "/search?query=" + queryString, null, callback);
    };

    module.register = function(username, name, email, password, callback) {
        send("POST", "/register", {"username":username, "name":name, "email":email, "password":password}, callback);
    };

    function buildQueryString(query, filters) {

        var query = query.replace(" ","+");

        if (filters.length <= 0) return query;

        return query + "&filters=" + parseFilters(filters);
    }

    function parseFilters(filters) {
        // assume a list of filters are passed
        var result = "";
        filters.forEach(function(filter){
            result += filter + "%2C";
        });

        result = result.slice(0, -3);
        console.log(result);
        return result;
    }

    return module;
})();
