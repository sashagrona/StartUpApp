<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: olexandr
  Date: 06.04.19
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
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
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/validation.css">

    <title>Registration</title>
</head>
<body>
<br><br><br>
<div class="container" align="center">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">

            <form class="form-horizontal" id="register" action="/sign_up" method="post">
<%--Registration form--%>
                <div class="form-group">
                    <h2>Sign up to start creating your perfect StartUp</h2><br>
                    <br><p>Your login:</p><br>
                    <input required type="text" id="login" class="form-control" name="login" placeholder="Login">
                    <div class="requirements" id="mLogin"></div>
                    <br><p>Your email:</p><br>
                    <input required type="email" class="form-control" id="email" name="email" placeholder="Email">
                    <div class="requirements" id="mEmail"></div>

                    <br><p>Your password:</p><br>
                    <input required type="password" class="form-control" id="password" name="password"
                           placeholder="Password">
                    <div class="requirements" id="mPassword"></div>
                </div>

                <input type="submit" class="btn btn-primary" value="Sign up">
                <sec:csrfInput/>
                <br><br><p><a href="login">Return to login?</a> </p>
            </form>

        </div>
    </div>
</div>
<input type="hidden" id="emails" value="${emails}">


<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>

<script src="/js/validation.js"></script>

</body>
</html>
