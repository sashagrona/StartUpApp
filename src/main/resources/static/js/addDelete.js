function add(email, reloadURL) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    //sending data via ajax
    var email = {json: JSON.stringify(email)};
    console.log(email);
    $.ajax({
        type: "post",
        headers: {
            'X-CSRF-TOKEN': $('#token')
        },
        data: email,
        dataType: "json",
        url: "/friend/add",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            if (result.description == "OK") {
                setTimeout(function () {
                    window.location = reloadURL;
                }, 200);
            }

        },
        error: function (xhr, status, error) {
            var jsonError = jQuery.parseJSON(xhr.responseText);
            var desc = (jsonError != "") ? jsonError.description : "no details";

        }
    });

}

function addToStartUp(email, startUp) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    var email = {json: JSON.stringify(email)};
    console.log(email);
    //sending data via ajax
    $.ajax({
        type: "post",
        headers: {
            'X-CSRF-TOKEN': $('#token')
        },
        data: email,
        dataType: "json",
        url: "/startup/" + startUp + "/add_participant",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            if (result.description == "OK") {
               alert("Added successfully");
            }

        },
        error: function (xhr, status, error) {
            var jsonError = jQuery.parseJSON(xhr.responseText);
            var desc = (jsonError != "") ? jsonError.description : "no details";

        }
    });

}

function deleteObject(obj, url, reloadURL) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    var data = {json: JSON.stringify(obj)};
    //sending data via ajax
    $.ajax({
        type: "post",
        headers: {
            'X-CSRF-TOKEN': $('#token')
        },
        data: data,
        dataType: "json",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            if (result.description == "OK") {
                setTimeout(function () {
                    window.location = reloadURL;
                }, 200);
            }

        },
        error: function (xhr, status, error) {
            var jsonError = jQuery.parseJSON(xhr.responseText);
            var desc = (jsonError != "") ? jsonError.description : "no details";

        }
    });

}

$('#deleteTask').click(function () {
    var data = [];
    console.log(data);
    //putting checked checkbox
    $(':checked').each(function () {
        data.push($(this).val());

    });
    console.log(data);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    var ids = {json: JSON.stringify(data)};
    //sending data via ajax
    $.ajax({
        type: "post",
        headers: {
            'X-CSRF-TOKEN': $('#token')
        },
        data: ids,
        dataType: "json",
        url: "/startup/task/delete",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            if (result.description == "OK") {
                setTimeout(function () {
                    window.location = "/startup/" + $('#sName').val()+"/plan";
                }, 200);
            }
        },
        error: function (xhr, status, error) {
            var jsonError = jQuery.parseJSON(xhr.responseText);
            var desc = (jsonError != "") ? jsonError.description : "no details";

        }
    });

});

$('#doneTask').click(function () {
    var data = [];
    //putting checked checkbox
    $(':checked').each(function () {
        data.push($(this).val());

    });
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    var ids = {json: JSON.stringify(data)};
    //sending data via ajax
    $.ajax({
        type: "post",
        headers: {
            'X-CSRF-TOKEN': $('#token')
        },
        data: ids,
        dataType: "json",
        url: "/startup/task/done",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (result, status, xhr) {
            if (result.description == "OK") {
                setTimeout(function () {
                    window.location = "/startup/" + $('#sName').val()+"/plan";
                }, 200);
            }
        },
        error: function (xhr, status, error) {
            var jsonError = jQuery.parseJSON(xhr.responseText);
            var desc = (jsonError != "") ? jsonError.description : "no details";

        }
    });

});
