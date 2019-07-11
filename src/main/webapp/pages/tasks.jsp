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
                        <img src="${pictureURL}" width="50" style="margin: 10px" height="50"
                             onerror="if (this.src!='/icons/default.png') this.src = '/icons/default.png'; ">
                    </li>

                </ul>
            </div>

        </div>
    </div>
</nav>


<%--Stylizing checkbx only on the page--%>
<style>
    .container {
        display: block;
        position: relative;
        padding-left: 35px;
        margin-bottom: 12px;
        cursor: pointer;
        font-size: 22px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    /* Hide the browser's default checkbox */
    .container input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
        height: 0;
        width: 0;
    }

    /* Create a custom checkbox */
    .checkmark {
        position: absolute;
        top: 0;
        left: 0;
        height: 25px;
        width: 25px;
        background-color: #eee;
    }

    /* On mouse-over, add a grey background color */
    .container:hover input ~ .checkmark {
        background-color: #ccc;
    }

    /* When the checkbox is checked, add a blue background */
    .container input:checked ~ .checkmark {
        background-color: #2196F3;
    }

    /* Create the checkmark/indicator (hidden when not checked) */
    .checkmark:after {
        content: "";
        position: absolute;
        display: none;
    }

    /* Show the checkmark when checked */
    .container input:checked ~ .checkmark:after {
        display: block;
    }

    /* Style the checkmark/indicator */
    .container .checkmark:after {
        left: 9px;
        top: 5px;
        width: 5px;
        height: 10px;
        border: solid white;
        border-width: 0 3px 3px 0;
        -webkit-transform: rotate(45deg);
        -ms-transform: rotate(45deg);
        transform: rotate(45deg);
    }
</style>
<div align="right">
    <main class="form-horizontal" style="width: 72%;margin: 50px;background-color: rgba(107,111,112,0.78);opacity: 80%;">
        <div class="container-fluid"><br>
            <%--Adding new task--%>
            <button class="buttons btn-hover color-2" data-toggle="modal" data-target="#modal">Add new Task</button>
            <br>
            <c:choose>
                <c:when test="${tasks eq null}">
                    <h2> No tasks yet</h2>
                </c:when>
                <c:otherwise>
                    <h1 style="color:#0c0c0c;">Tasks for BusinessPlan of ${sName}</h1>

                    <%--                        List of tasks--%>
                    <table class="table table-hover" align="center">
                        <thead class="thead-dark">
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Priority</th>
                            <th>Check</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${tasks}" var="t">
                            <c:choose>
                                <c:when test="${t.done eq true}">
                                    <tr style="text-decoration: line-through;font-size: 22px;align-self: center; color: #0c0c0c;">
                                        <td><c:out value="${t.name}"/></td>
                                        <td><c:out value="${t.description}"/></td>
                                        <td><c:out value="${t.priority}"/></td>
                                        <td>
                                            <label class="container">
                                                <input type="checkbox" value="${t.id}">
                                                <span class="checkmark"></span>
                                            </label>

                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr style="font-size: 22px;align-self: center; color: #0c0c0c">
                                        <td><c:out value="${t.name}"/></td>
                                        <td><c:out value="${t.description}"/></td>
                                        <td><c:out value="${t.priority}"/></td>
                                        <td>
                                            <label class="container">
                                                <input type="checkbox" value="${t.id}">
                                                <span class="checkmark"></span>
                                            </label>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        </tbody>
                    </table>
                    <%--                    functions for deleting and matching as done--%>
                    <br>

                    <button class="btn btn-success" id="deleteTask">Delete</button>
                    <button class="btn btn-success" id="doneTask">Cross as done</button>

                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
<%--Modal window for adding new task--%>
<div id="modal" class="modal fade form-group" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" align="center">New Task</h4>
            </div>
            <div class="modal-body" align="center">

                <input class="form-control" type="text" id="taskText" placeholder="New task"/>
                <input type="hidden" id="sName" value="${sName}"/>
                <span class="requirements" id="nameSpan"></span><br>

                <input class="form-control" type="text" id="taskDesc" placeholder="Description"/>
                <span class="requirements" id="descSpan"></span><br>

                Priority: <select name="priority" id="priority">
                <option value="1" selected>1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select><br><br>

                <input type="button" class="btn btn-success" id="submitButton" value="Add new task"/><br><br>
                <span id="messageSpan"></span>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.0.min.js"
        integrity="sha256-BJeo0qm959uMBGb65z40ejJYGSgR7REI4+CW1fNKwOg=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>

<script src="/js/tasks.js"></script>
<script src="/js/validateEmail.js"></script>
<script src="/js/addDelete.js"></script>

</body>
</html>
