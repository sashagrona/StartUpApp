<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/validation.css">
    <title>Login</title>
</head>
<body>
<br><br><br>
<div class="container" align="center">
    <div class="row form-horizontal">
        <div class="col-md-offset-3 col-md-6">
            <%--            new password input--%>
            <h2>Input your new password</h2><br>
            <form id="regist" action="/forgot/updatePassword" method="post">
                <div class="form-group">
                    <input required type="password" id="one" name="password" placeholder="New password"
                           class="form-control">
                    <div class="requirements" id="m_PasswordOne"></div>
                    <br>
                    <%--                            checking password--%>
                    <h2>Confirm your password</h2><br>
                    <input required type="password" id="two" name="passConfirm"
                           placeholder="Confirm new password"
                           class="form-control">
                    <div class="requirements" id="m_PasswordTwo"></div>
                    <br>
                    <input type="hidden" value="${token}" name="token">
                    <input type="submit" class="btn btn-success" value="Reset password">
                    <sec:csrfInput/>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<script src="/js/validatePasswords.js"></script>
</body>
</html>
