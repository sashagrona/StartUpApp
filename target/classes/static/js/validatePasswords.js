jQuery(function ($) {
    $('#regist').on('submit', function (event) {
        if (validateForm()) { // если есть ошибки возвращает true

            event.preventDefault();
        }
    });


    function validateForm() {
        $(".text-error").remove();

        // Passwords check

        var one = $('#one');
        var two = $("#two");

        var v_one = one.val() ? false : true;
        var v_two = two.val() ? false : true;

        if (one.val().length < 6) {
            var v_passOne = true;
            var v_passTwo = true;
            $('#m_PasswordOne').text("Password should have at least 6 characters");
        } else if (one.val() != two.val()) {
            var v_passOne = true;
            var v_passTwo = true;
            $('#m_PasswordTwo').text("Passwords don't match");
        } else {
            $('#m_PasswordTwo').text("");
        }

        $('#one').toggleClass('error', v_passOne);
        $("#two").toggleClass('error', v_passTwo);

        return (v_passOne || v_passTwo);
    }

});
