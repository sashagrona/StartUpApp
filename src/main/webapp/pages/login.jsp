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
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/validation.css">
    <title>Login</title>
</head>
<body>
<br><br><br>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
<%--            Login form--%>
            <c:url value="/security_check" var="logURL"/>
            <form class="form-horizontal" method="POST" action=${logURL}><br>
                <h2>StartUpApp</h2>
                <div class="form-group">
                    <i class="fa fa-envelope-o"></i><input required type="email" class="form-control"
                                                           placeholder="E-mail"
                                                           name="email">

                </div>
                <div class="form-group">
                    <i class="fa fa-lock"></i><input required type="password" class="form-control"
                                                     placeholder="Password" name="password">
                </div>
                    <c:if test="${param.error ne null}">
                        <div class="requirements">
                            Wrong email or password
                        </div><br>
                    </c:if>
<%--                forgot password function --%>
                <div><a href="/forgot/showPage"> Forgot password?</a></div>
                <div>
                    <br>
                    <button type="submit" class="btn btn-primary">Login</button>
                    <br><br>
                    <p>Are you new user?<br>You can sign up</p>
                    <a href="/register" class="btn btn-primary">Sign up</a><br><br>
<%--login via other apps--%>
                    <p>Login via:</p><br>
                    <button type="button" class="btn btn-default" onclick="goToURL('${google}');"/>
                    <img src="icons/google.png" width="40" height="40"/></button>
                    <button type="button" class="btn btn-default" onclick="goToURL('${facebook}');"/>
                    <img src="icons/fb.png" width="40" height="40"/></button>
                    <button type="button" class="btn btn-default" onclick="goToURL('${github}');"/>
                    <img src="icons/gh.svg" width="40" height="40"/></button><br><br>

                </div>
                <sec:csrfInput/>
            </form>
        </div>
    </div>
</div>
<script>
    function goToURL(url) {
        location.href = url;
    }
</script>


<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>
