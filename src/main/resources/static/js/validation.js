jQuery(function ($) {
    $('#register').on('submit', function (event) {
        if (validateForm()) { // если есть ошибки возвращает true

            event.preventDefault();
        }
    });



    function validateForm() {
        $(".text-error").remove();

        // Login check
        var login = $('#login');
        if (login.val().length < 4) {
            var v_login = true;
            $('#mLogin').text("Login should contain more than 3 characters");

        }else {
            $('#mLogin').text("");
        }
        $("#login").toggleClass('error', v_login);

        // Email check

        var reg = /^\w+([\.-]?\w+)*@(((([a-z0-9]{2,})|([a-z0-9][-][a-z0-9]+))[\.][a-z0-9])|([a-z0-9]+[-]?))+[a-z0-9]+\.([a-z]{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$/i;
        var email = $('#email');
        var v_email = email.val() ? false : true;
        var emails = $('#emails').val();
        console.log(emails);

        if (!reg.test(email.val())) {
            v_email = true;
            $('#mEmail').text("Email must be valid");
        } else if(emails.includes(email.val())){
            v_email = true;
            $('#mEmail').text("This email already exists");

        } else {
            $('#mEmail').text("");
        }

        $("#email").toggleClass('error', v_email);

        // Password check

        var password = $('#password');


        var v_password = password.val() ? false : true;

        if (password.val().length < 6) {
            var v_password = true;
            $('#mPassword').text("Password should have at least 6 characters");
        }else {
            $('#mPassword').text("");
        }

        $('#password').toggleClass('error', v_password);

        return (v_login || v_email || v_password);
    }

});
