$(document).ready(function() {
    $('.nav-link-collapse').on('click', function() {
        $('.nav-link-collapse').not(this).removeClass('nav-link-show');
        $(this).toggleClass('nav-link-show');
    });
    assignButton();
});

function assignButton() {
    $("#submitButton").click(function (e) {
        if ($("#startText").val() == "")
            $("#nameSpan").text("Enter name");
        else
            $("#nameSpan").text("");

        if ($("#startIdea").val() == "")
            $("#ideaSpan").text("Enter idea");
        else
            $("#ideaSpan").text("");

        if (($("#startText").val() != "") && ($("#startIdea").val() != "")) {
            var startUpDTO = {
                name: $("#startText").val(),
                idea: $("#startIdea").val()

            };
            var startUpName = $('#startText').val();
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            //sending data on endpoint via ajax
            $.ajax({
                type: "post",
                    headers: {
                        'X-CSRF-TOKEN': $('#token')
                    },
                url: "/startup/add",
                contentType: "application/json",
                data: JSON.stringify(startUpDTO),
                dataType: "json",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (result, status, xhr) {
                    if (result.description == "OK") {
                        $("#messageSpan").text("Added successfully");
                        setTimeout(function () {
                            window.location = "/startup/" + startUpName;
                        }, 1000);
                    } else {
                        $("#messageSpan").text("Error occured!");
                    }
                },
                error: function (xhr, status, error) {
                    var jsonError = jQuery.parseJSON( xhr.responseText );
                    var desc = (jsonError != "") ? jsonError.description : "no details";

                    $("#messageSpan").text("Result: " + status + " StartUp with such a name already exists");
                }
            });
            $('#startText').text("");
            $('#startIdea').text("");
        }
    });
}

var form = document.getElementById("form");

document.getElementById("your-id").addEventListener("click", function () {
    form.submit();
});
