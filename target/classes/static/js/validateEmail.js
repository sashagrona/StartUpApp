jQuery(function ($) {
    $('#reg').on('submit', function (event) {
        if (validateForm()) { //for errors returns true

            event.preventDefault();
        }
    });

    function validateForm() {
        $(".text-error").remove();

        // Email check

        var reg = /^\w+([\.-]?\w+)*@(((([a-z0-9]{2,})|([a-z0-9][-][a-z0-9]+))[\.][a-z0-9])|([a-z0-9]+[-]?))+[a-z0-9]+\.([a-z]{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))$/i;
        var email = $('#emailOne');
        var v_email = email.val() ? false : true;

        if (!reg.test(email.val())) {
            v_email = true;
            $('#m_Email').text("Email must be valid");
        }else {
            $('#m_Email').text("");
        }
        $("#emailOne").toggleClass('error', v_email);



        return (v_email);
    }

});

