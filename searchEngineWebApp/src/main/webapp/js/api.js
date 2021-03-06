var api = (function(){
    "use strict";

    function send(method, url, data, callback){
        var xhr = new XMLHttpRequest();
        xhr.onload = function() {
            if (xhr.status !== 200) callback(xhr.statusText, null);
            else{
                try{
                    var x = JSON.parse(xhr.responseText);
                    callback(null, x);
                }
                catch(err) {
                    callback(err, null);
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

    module.getCurrentUser = function() {
        var l = document.cookie.split("currentUserName=");
        if (l.length > 1) return l[1].replace(/%20/g, " ").split(';')[0];
        return null;
    };

    module.getUserId = function() {
        var l = document.cookie.split("currentUserId=");
        if (l.length > 1) return l[1].split(';')[0];
        return null;
    };

    module.search = function(query, filters, perm, callback) {
        var queryString = buildQueryString(query, filters, perm);
        send("GET", "/search?query=" + queryString, null, callback);
    };


    module.signin = function(username, password, callback) {
        send("POST", "/signin", {"username":username, "password":password}, callback);
    };
    
    module.register = function(username, name, email, password, permission, callback) {
        send("POST", "/register", {"username":username, "name":name, "email":email, "password":password, "permission":permission}, callback);
    };
    
    module.signout = function (callback) {
    	send ("POST", "/signout", {}, callback);
    }
    
    module.addStudent = function(courseID, username, callback) {
        send("POST", "/course?id="+courseID, {"addStudnet":username}, callback);
    }

    module.addInstructor = function(courseID, username, callback) {
        console.log(username);
        send("POST", "/course?id=" + courseID, {"addInstructor":username}, callback);
    }

    module.addFile = function(courseID, id, callback) {
        send("POST", "/course?id="+courseID, {"addFile":id}, callback);
    }

    module.removeStudent = function(courseID, username, callback) {
        send("POST", "/course?id="+courseID, {"removeStudent":username}, callback);
    }

    module.removeInstructor = function(courseID, username, callback) {
        send("POST", "/course?id="+courseID, {"removeInstructor":username}, callback);
    }

    module.removeFile = function(courseID, id, callback) {
        send("POST", "/course?id="+courseID, {"removeFile":id}, callback);
    }

    module.editCourse = function(courseID, newCode, newName, newDesc, newSize, callback) {
        send("POST", "/course?id="+courseID, {"courseCode":newCode, "courseName":newName,"courseDesc":newDesc, "courseSize":newSize}, callback);
    }

    module.getFile = function(id, callback) {
        send("GET", "/file?id=" + id + "&get=true", null, callback);
    }

    module.getUser = function(id, callback) {
        send("GET", "/profile?id=" + id + "&get=true", null, callback);
    }

    module.getCourse = function(id, callback) {
        send("GET", "/course?id=" + id + "&get=true", null, callback);
    }

    module.updateUser = function(id, name, desc, image, callback) {
        send("POST", "/profile?id=" + id, {"id":id, "name":name, "desc":desc, "image":image}, callback);
    }

    module.createCourse = function(courseId, callback) {
        send("POST", "/profile?createCourse=true", {"courseId":courseId}, callback);
    }

    module.removeUserFile = function(id, callback) {
        send("POST", "/profile?deleteFile=true", {"fileId":id}, callback);
    }


    module.updateFile = function(id, name, course, permission, callback) {
        send("POST", "/file?id=" + id, {"name":name, "course":course, "permission":permission}, callback);
    }

    module.followUser = function(id, unfollow, callback) {
        send("POST", "/follow-user", {"id":id, "unfollow":unfollow}, callback);
    }

    module.openNotification = function(id, callback) {
        send("POST", "/notifications", {"id":id, "clear":true}, callback);
    }

    module.deleteNotification = function(id, callback) {
        send("POST", "/notifications", {"id":id, "delete":true}, callback);
    }

    module.buildQuery = function buildQueryString(query, filters, perm) {

        var query = query.replace(" ","+");

        if (filters.length <= 0) return query;

        return query + "&filters=" + parseFilters(filters) + "&perm=" + perm;
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
