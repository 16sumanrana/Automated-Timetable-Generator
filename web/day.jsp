<%@ page import="inverse.Config" %>
<%--
  Created by IntelliJ IDEA.
  User: Suman Rana
  Date: 22-03-2019
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Days | Automated Timetable Generator</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/x-icon" href="icon.png">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css"
          integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="custom.css">
    <script src="custom.js"></script>
</head>
<body onload="loaddaytable();">
<div class="d-flex justify-content-center head-flex">Automated Timetable Generator</div>
<nav class="navbar navbar-expand-lg navbar-light sticky-top custom-nav">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Automated Timetable Generator</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02"
                aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
            <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="generate.jsp">Generate Table</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="class.jsp">Classes</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="day.jsp">Days</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="time.jsp">Time Slots</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="subject.jsp">Subjects</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="classwisesubject.jsp">Class Wise Subjects</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="teacher.jsp">Teachers</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="map.jsp">Map</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<section>
    <div class="container">
        <br>
        <br>
        <div class="row mb-3" style="text-align: center;">
            <div class="col">
                <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#exampleModalCenter">
                    Add Day
                </button>
            </div>
        </div>
        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="addday" onsubmit="return addday(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="add-day-modal-title">Add Day</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <input type="hidden" name="isaddday" value="true">
                            <div class="form-group">
                                <label for="add-day-dayname"></label>
                                <input id="add-day-dayname" type="text" name="dayname" placeholder="e.g. Monday"
                                       class="form-control text-center" aria-describedby="add-day-dayname-HelpInline"
                                       required>
                                <small id="add-day-dayname-HelpInline" class="text-muted">
                                    Day Must be unique.
                                </small>
                            </div>
                            <div class="form-group">
                                <label for="edit-day-dayperiods"></label>
                                <input id="add-day-dayperiods" type="number" name="dayperiods"
                                       placeholder="No of periods"
                                       class="form-control text-center" aria-describedby="add-day-dayperiods-helpline"
                                       value="" required>
                                <small id="add-day-dayperiods-helpline" class="text-muted">
                                    No of periods.
                                </small>
                            </div>
                            <small id="status" style="display: none;">
                                Day is added!
                            </small>
                        </div>
                        <div class="modal-footer" style="justify-content: space-between;">
                            <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-info">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal fade" id="edit-day-modal" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="edit-day" onsubmit="return editday(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="edit-day-modal-title">Edit Day</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <input type="hidden" name="iseditday" value="true">
                            <input type="hidden" name="dayid" value="" id="edit-day-dayid">
                            <div class="form-group">
                                <label for="edit-day-dayname"></label>
                                <input id="edit-day-dayname" type="text" name="dayname" placeholder="e.g. Monday"
                                       class="form-control text-center" aria-describedby="edit-day-dayname-helpline"
                                       value="" required>
                                <small id="edit-day-dayname-helpline" class="text-muted">
                                    Day Must be unique.
                                </small>
                            </div>
                            <div class="form-group">
                                <label for="edit-day-dayperiods"></label>
                                <input id="edit-day-dayperiods" type="number" name="dayperiods"
                                       placeholder="No of periods"
                                       class="form-control text-center" aria-describedby="edit-day-dayperiods-helpline"
                                       value="" required>
                                <small id="edit-day-dayperiods-helpline" class="text-muted">
                                    No of periods.
                                </small>
                            </div>
                            <small id="edit-status" style="display: none;">
                                Day is added!
                            </small>
                        </div>
                        <div class="modal-footer" style="justify-content: space-between;">
                            <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-info">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="table-responsive">
            <table class="table table-sm table-hover table-dark table-hover table-striped table-bordered">
                <caption>Day Table</caption>
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Day</th>
                    <th scope="col">Periods</th>
                    <th scope="col">Edit</th>
                    <th scope="col">Delete</th>
                </tr>
                </thead>
                <tbody id="daytable">

                </tbody>
            </table>
        </div>
    </div>
</section>
<section>
    <div id="notification" class="container" style="display: none;">
        <div class="row">
            <div class="col-10">
                <span id="notification-message">Successfully Deleted</span>
            </div>
            <div class="col-2 close-notification" onclick="closenotification();">
                <span>X</span>
            </div>
        </div>
    </div>
</section>
<section class="footer">
    <div style="background-color:#002041;color: white;">
        <div class="d-flex justify-content-center" style="background-color:#012B55">
            <div class="p-3 bd-highlight"><i class="fa fa-copyright"></i>
                2019 Automated Timetable Generator
            </div>
        </div>
    </div>
</section>
<script>
    function loaddaytable() {
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/DayServlet",
            data: {
                isloaddaytable: true
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                // console.log(exception);
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect. Verify Network.';
                } else if (jqXHR.status === 404) {
                    msg = 'Requested page not found. [404]';
                } else if (jqXHR.status === 500) {
                    msg = 'Internal Server Error [500].';
                } else if (exception === 'parsererror') {
                    msg = 'Requested JSON parse failed.';
                } else if (exception === 'timeout') {
                    msg = 'Time out error.';
                } else if (exception === 'abort') {
                    msg = 'Ajax request aborted.';
                } else {
                    msg = 'Uncaught Error.' + jqXHR.responseText;
                }
                // console.log(msg);
                shownotification(msg);
            },
            success: function (msg) {
                // console.log("success");
                // console.log(msg);
                // console.log(msg["status"]);
                // console.log(msg["statusText"]);
                if (msg["status"] === true) {
                    $('#daytable').html(msg["statusText"]);
                } else {
                    // console.log(msg["statusText"]);
                    shownotification(msg["statusText"]);
                }
            }
        });
    }

    function addday(form) {
        var addstatus = $('#status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/DayServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                // console.log($(form).serialize());
                addstatus.css("display", "none");
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                // console.log(exception);
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect. Verify Network.';
                } else if (jqXHR.status === 404) {
                    msg = 'Requested page not found. [404]';
                } else if (jqXHR.status === 500) {
                    msg = 'Internal Server Error [500].';
                } else if (exception === 'parsererror') {
                    msg = 'Requested JSON parse failed.';
                } else if (exception === 'timeout') {
                    msg = 'Time out error.';
                } else if (exception === 'abort') {
                    msg = 'Ajax request aborted.';
                } else {
                    msg = 'Uncaught Error.' + jqXHR.responseText;
                }
                // console.log(msg);
                addstatus.html(msg);
                addstatus.css("display", "block");
                addstatus.css("color", "red");
                setTimeout(function () {
                    addstatus.css("display", "none");
                }, 5000);
            },
            success: function (msg) {
                // console.log(msg);
                addstatus.html(msg["statusText"]);
                addstatus.css("display", "block");
                if (msg["status"] === false) {
                    addstatus.css("color", "red");
                } else {
                    addstatus.css("color", "green");
                    loaddaytable();
                }
                setTimeout(function () {
                    addstatus.css("display", "none");
                }, 5000);
            }
        });
        return false;
    }

    function showeditform(form) {
        // console.log($(form));
        var values = {};
        $.each($(form).serializeArray(), function (i, field) {
            values[field.name] = field.value;
        });
        // console.log(values);
        $('#edit-day-dayid').val(values["dayid"]);
        $('#edit-day-dayname').val(values["dayname"]);
        $('#edit-day-dayperiods').val(values["dayperiods"]);
        $('#edit-day-modal').modal('toggle');
        return false;
    }

    function editday(form) {
        var editstatus = $('#edit-status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/DayServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                // console.log($(form).serialize());
                editstatus.css("display", "none");
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                // console.log(exception);
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect. Verify Network.';
                } else if (jqXHR.status === 404) {
                    msg = 'Requested page not found. [404]';
                } else if (jqXHR.status === 500) {
                    msg = 'Internal Server Error [500].';
                } else if (exception === 'parsererror') {
                    msg = 'Requested JSON parse failed.';
                } else if (exception === 'timeout') {
                    msg = 'Time out error.';
                } else if (exception === 'abort') {
                    msg = 'Ajax request aborted.';
                } else {
                    msg = 'Uncaught Error.' + jqXHR.responseText;
                }
                // console.log(msg);
                editstatus.html(msg);
                editstatus.css("display", "block");
                editstatus.css("color", "red");
                setTimeout(function () {
                    editstatus.css("display", "none");
                }, 5000);
            },
            success: function (msg) {
                // console.log(msg);
                editstatus.html(msg["statusText"]);
                editstatus.css("display", "block");
                if (msg["status"] === false) {
                    editstatus.css("color", "red");
                } else {
                    editstatus.css("color", "green");
                    loaddaytable();
                }
                setTimeout(function () {
                    editstatus.css("display", "none");
                }, 5000);
            }
        });
        return false;
    }

    function deleteday(form) {
        if (confirmdeletion() === false) {
            return false;
        }
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/DayServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                // console.log($(this).serialize());
                closenotification();
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                // console.log(exception);
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect. Verify Network.';
                } else if (jqXHR.status === 404) {
                    msg = 'Requested page not found. [404]';
                } else if (jqXHR.status === 500) {
                    msg = 'Internal Server Error [500].';
                } else if (exception === 'parsererror') {
                    msg = 'Requested JSON parse failed.';
                } else if (exception === 'timeout') {
                    msg = 'Time out error.';
                } else if (exception === 'abort') {
                    msg = 'Ajax request aborted.';
                } else {
                    msg = 'Uncaught Error.' + jqXHR.responseText;
                }
                // console.log(msg);
                shownotification(msg);
            },
            success: function (msg) {
                // console.log("success");
                // console.log(msg);
                if (msg["status"] === true) {
                    shownotification(msg["statusText"]);
                    loaddaytable();
                } else {
                    shownotification(msg["statusText"]);
                }
            }
        });
        return false;
    }
</script>
</body>
</html>
