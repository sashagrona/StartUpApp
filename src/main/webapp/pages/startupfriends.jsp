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
    <%--Tokens for csrf security--%>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/index.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/validation.css">

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


            <%--            <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">--%>

            <sec:csrfInput/>
        </form>
    </div>
</nav>

<div align="right">
    <main class="form-horizontal" style="width: 72%;margin: 50px;background-color: #00b7f7;opacity: 100;">
        <div class="container-fluid">
            <h2>Add your friend to StartUp</h2>
            <br> <a href="/startup/${startUp}"><input type="button" class="btn btn-primary" value="Back to startup"></a><br><br>
            <c:choose>
                <c:when test="${users eq null}">
                    <h2> No friends yet</h2>
                </c:when>
                <c:otherwise>
<%--                    List of friends for adding to start up--%>
                    <table id="data" class="table table-hover">
                        <thead>
                        </thead>
                        <tbody>
                        <c:forEach items="${users}" var="u">
                            <tr>
                                <td><a href="/friend/profile?email=${u.email}"><img src="${u.pictureURL}" width="40"
                                                                                    height="40"
                                                                                    onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png';"></a>
                                </td>
                                <td><c:out value="${u.login}"></c:out></td>
                                <c:choose>
                                    <c:when test="${u.disableButton eq true}">
                                        <td>
                                            <button class="btn btn-success" disabled>Add to StartUp</button>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>
                                            <button class="btn btn-success"
                                                    onclick="addToStartUp('${u.email}', '${startUp}')">Add to StartUp
                                            </button>
                                        </td>
                                    </c:otherwise>
                                </c:choose>

                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
        <br>
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
<script src="/js/validateEmail.js"></script>
<script src="/js/addDelete.js"></script>
</body>
</html>
