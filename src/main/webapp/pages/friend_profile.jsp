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
<nav class="navbar navbar-expand-lg navbar-dark fixed-top " style="background-color: rgba(107,111,112,0.78);opacity: 0.99">
    <a class="navbar-brand" style="color: black; font-size: 40px" href="/about">StartUpApp</a>
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
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #242424" href="/myprofile">My
                    Profile</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #242424" href="/colleg">My
                    Friends</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #242424" href="/create_startup">My
                    StartUps</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #242424" href="/friend/find">Find
                    friends</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" style="font-size: 26px;align-self: center; color: #242424" href="/chat/all">StartUp
                    chats</a>
            </li>

            <form action="/logout" method="post" id="form">
                <li class="nav-item">
                    <a class="nav-link" href="#" style="font-size: 26px;align-self: center; color: #333333" onclick="document.getElementById('form').submit();">Log
                        Out</a>
                </li>
                <sec:csrfInput/>
            </form>
        </ul>
        <div class="form-inline ml-auto mt-2 mt-md-0">
            <div class="navbar-collapse collapse w-100 order-3 dual-collapse2">
                <ul class="nav justify-content-end">
                    <li class="nav-item">
                        <div style="color: #000;font-size: 20px;margin:15px;"><c:out value="${login}"/></div>
                    </li>
                    <li class="nav-item">
                        <img src="${photo}" width="50" style="margin: 10px" height="50"
                             onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png'; ">
                    </li>

                </ul>
            </div>

        </div>
    </div>
</nav>

<div align="right">
    <main class="form-horizontal" style="width: 72%;margin: 50px;background-color: rgba(107,111,112,0.78);opacity: 80%;">
        <div class="container-fluid"><br>
<%--            Just a visit card of user to contact--%>
            <h2>${friend.login}</h2><br>

            <img src="${friend.pictureURL}" width="250" height="250" align="center"
                 onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png';">
            <br><br>

            <br>
            <p style="font-size: 22px;align-self: center; color: #333333">Email: ${friend.email}</p><br>


            <br>
            <p style="font-size: 22px;align-self: center; color: #333333">Phone: ${friend.phone}</p><br>

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
