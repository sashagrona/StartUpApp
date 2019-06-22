$(document).ready(function() {
    $('.nav-link-collapse').on('click', function() {
        $('.nav-link-collapse').not(this).removeClass('nav-link-show');
        $(this).toggleClass('nav-link-show');
    });
    assignButton();
});

function assignButton() {
    $("#submitButton").click(function (e) {
        if ($("#taskText").val() == "")
            $("#nameSpan").text("Enter task");
        else
            $("#nameSpan").text("");

        if ($("#taskDesc").val() == "")
            $("#descSpan").text("Enter description");
        else
            $("#descSpan").text("");

        if (($("#taskText").val() != "") && ($("#taskDesc").val() != "")) {
            var taskDTO = {
                name: $("#taskText").val(),
                description: $("#taskDesc").val(),
                priority: document.getElementById("priority").value

            };
            var startUpName = $('#sName').val();
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            //sending data via ajax on endpoint
            $.ajax({
                type: "post",
                headers: {
                    'X-CSRF-TOKEN': $('#token')
                },
                url: "/startup/" + startUpName + "/plan/add",
                contentType: "application/json",
                data: JSON.stringify(taskDTO),
                dataType: "json",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (result, status, xhr) {
                    if (result.description == "OK") {
                        $("#messageSpan").text("Added successfully");
                        setTimeout(function () {
                            window.location = "/startup/" + startUpName + "/plan";
                        }, 1000);
                    } else {
                        $("#messageSpan").text("Error occured!");
                    }
                },
                error: function (xhr, status, error) {
                    var jsonError = jQuery.parseJSON( xhr.responseText );
                    var desc = (jsonError != "") ? jsonError.description : "no details";

                    $("#messageSpan").text("Result: " + status + " Something wrong! Please, try again");
                }
            });
            $('#startText').text("");
            $('#startIdea').text("");
        }
    });
}
