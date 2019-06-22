<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: olexandr
  Date: 06.04.19
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/index.css">
    <link rel="stylesheet" href="/css/login.css">

    <title>MyCabinet</title>
</head>
<body>
<%--Navigation bar--%>
<nav class="navbar navbar-expand-lg navbar-dark fixed-top " style="background-color: #dcdcdc;opacity: 0.99">
    <a class="navbar-brand" style="color: black; font-size: 40px" href="/">StartUpApp</a>
    <button class="navbar-toggler"
            type="button"
            style="color: black;"
            data-toggle="collapse"
            data-target="#navbarCollapse"
            aria-controls="navbarCollapse"
            aria-expanded="false"
            aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto sidenav" id="navAccordion">
            <li class="nav-item active">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #333333" href="/myprofile">My
                    Profile</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #333333" href="/colleg">My
                    Friends</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #333333" href="/create_startup">My
                    StartUps</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #333333" href="/friend/find">Find
                    friends</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #333333" href="/chat/all">StartUp
                    chats</a>
            </li>
        </ul>
        <form class="form-inline ml-auto mt-2 mt-md-0" action="/logout" method="post">
            <div class="navbar-collapse collapse w-100 order-3 dual-collapse2">
                <ul class="nav justify-content-end">
                    <li class="nav-item">
                        <img src="${photo}" width="50" style="margin: 10px" height="50"
                             onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png'; ">
                    </li>
                    <li class="nav-item">
                        <div style="color: #000;font-size: 20px;margin:15px;"><c:out value="${login}"/></div>
                    </li>
                    <li class="nav-item">
                        <button class="btn btn-default" style="margin: 7px;font-size: 20px;color: black" type="submit">
                            Log Out
                        </button>
                    </li>
                </ul>
            </div>
            <sec:csrfInput/>
        </form>
    </div>
</nav>

<div align="right">
    <main class="form-horizontal" style="width: 72%;margin: 50px;background-color: #00b7f7;opacity: 100;">
        <div class="container-fluid">
            <h2>My Profile</h2><br>
            <c:url value="/set_photo" var="url"/>
<%--            Avatar setting--%>
            <form action=${url} enctype="multipart/form-data" method="post">
                <img src="${photo}" width="250" height="250" align="center"
                     onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png';">
                <br><br>


                <div class="custom-file" style="width: 300px">
                    <input type="file" class="custom-file-input" name="file" id="file">
                    <label class="custom-file-label" for="file"><div style="float: left;">Choose file</div></label>
                </div>
                <br><br><input type="submit" class="btn btn-primary" value="Set new photo">
                <br><br>
                <sec:csrfInput/>
            </form>
<%--Profile updating--%>
            <form action="/updateProfile" id="register" method="post">
                <br>
                <p>Change login:(more than 3 characters)</p><br>
                <input type="text" id="login" class="form-control" name="login" placeholder="${login}">
                <div class="requirements" id="mLogin"></div>

                <br>
                <p>Change password:(at least 6 characters)</p><br>
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="******">
                <div class="requirements" id="mPassword"></div>
                <br>
                <p>Change phone(+380..)</p><br>
                <input type="tel" pattern="[\+]\d{3}\d{4}\d{5}" class="form-control" id="phone" name="phone"
                       placeholder="${phone}">
                <div class="requirements" id="mPhone"></div>
                <br><br><input type="submit" class="btn btn-primary" value="Update">
                <sec:csrfInput/>
            </form>
            <br>
<%--            Log out function --%>
            <form action="/logout" method="post">
                <button class="btn btn-success" style="margin: 7px;font-size: 20px;color: black" type="submit">
                    Log Out
                </button>
                <sec:csrfInput/>
            </form>

        </div>
    </main>
</div>

<script src="https://code.jquery.com/jquery-3.4.0.min.js"
        integrity="sha256-BJeo0qm959uMBGb65z40ejJYGSgR7REI4+CW1fNKwOg=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>

<script src="/js/index.js"></script>
</body>
</html>
