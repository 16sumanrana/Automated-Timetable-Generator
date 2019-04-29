<%@ page import="inverse.Config" %>
<%--
  Created by IntelliJ IDEA.
  User: Suman Rana
  Date: 23-03-2019
  Time: 05:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Time Slots | Automated Timetable Generator</title>
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
<body onload="loadtimeslottable();">
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
                <li class="nav-item">
                    <a class="nav-link" href="day.jsp">Days</a>
                </li>
                <li class="nav-item active">
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
                    Add Time slot
                </button>
            </div>
        </div>
        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="addtimeslot" onsubmit="return addtimeslot(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="add-time-modal-title">Add Time slot</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <input type="hidden" name="isaddtimeslot" value="true">
                            <div class="form-group row">
                                <div class="col">
                                    <label for="add-time-slot-starttime">Start time: </label>
                                    <input id="add-time-slot-starttime" type="time" name="timeslotstarttime"
                                           class="form-control text-center"
                                           aria-describedby="add-time-slot-HelpInline" required="">
                                </div>
                                <div class="col">
                                    <label for="add-time-slot-endtime">End time: </label>
                                    <input id="add-time-slot-endtime" type="time" name="timeslotendtime"
                                           class="form-control text-center"
                                           aria-describedby="add-time-slot-HelpInline" required="">
                                </div>
                            </div>
                            <small id="add-time-slot-HelpInline" class="text-muted">
                                Input time slot must not conflict other constraints.
                            </small>
                            <br>
                            <small id="add-status" style="display: none;">
                                Time is added.
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
                <caption>Time slots</caption>
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Start Time</th>
                    <th scope="col">End Time</th>
                    <th scope="col">Edit</th>
                    <th scope="col">Delete</th>
                </tr>
                </thead>
                <tbody id="time-slot-table">

                </tbody>
            </table>
        </div>
        <div class="modal fade" id="edit-time-slot-modal" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="edit-time-slot" onsubmit="return edittimeslot(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="edit-subject-modal-title">Edit Time Slot</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <input type="hidden" name="isedittimeslot" value="true">
                            <input type="hidden" name="timeslotid" value="" id="edit-time-slot-timeslotid">
                            <div class="form-group row">
                                <div class="col">
                                    <label for="edit-time-slot-starttime">Start time: </label>
                                    <input id="edit-time-slot-starttime" type="time" name="timeslotstarttime"
                                           class="form-control text-center" aria-describedby="edit-time-slot-HelpInline"
                                           required="">
                                </div>
                                <div class="col">
                                    <label for="edit-time-slot-endtime">End time: </label>
                                    <input id="edit-time-slot-endtime" type="time" name="timeslotendtime"
                                           class="form-control text-center" aria-describedby="edit-time-slot-HelpInline"
                                           required="">
                                </div>
                            </div>
                            <small id="edit-time-slot-HelpInline" class="text-muted">
                                Input time slot must not conflict other constraints.
                            </small>
                            <br>
                            <small id="edit-status" style="display: none;">
                                Time is edited.
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
    function loadtimeslottable() {
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/TimeSlotServlet",
            data: {
                isloadtimeslottable: true
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect.\n Verify Network.';
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
                    msg = 'Uncaught Error.\n' + jqXHR.responseText;
                }
                // console.log(msg);
                shownotification(msg);
            }
        }).done(function (msg) {
            // console.log(msg);
            if (msg["status"] === true) {
                $("#time-slot-table").html(msg["statusText"]);
            } else {
                // console.log(msg["statusText"]);
                shownotification(msg["statusText"]);
            }
        });
    }

    function addtimeslot(form) {
        var addstatus = $('#add-status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/TimeSlotServlet",
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
                    loadtimeslottable();
                }
                setTimeout(function () {
                    addstatus.css("display", "none");
                }, 5000);
            }
        });
        return false;
    }

    function showeditform(form) {
        var values = {};
        $.each($(form).serializeArray(), function (i, field) {
            values[field.name] = field.value;
        });
        $('#edit-time-slot-timeslotid').val(values["timeslotid"]);
        $('#edit-time-slot-starttime').val(values["timeslotstarttime"]);
        $('#edit-time-slot-endtime').val(values["timeslotendtime"]);
        $('#edit-time-slot-modal').modal('toggle');
        return false;
    }

    function edittimeslot(form) {
        var editstatus = $('#edit-status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/TimeSlotServlet",
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
                    loadtimeslottable();
                }
                setTimeout(function () {
                    editstatus.css("display", "none");
                }, 5000);
            }
        });
        return false;
    }

    function deletetimeslot(form) {
        if (confirmdeletion() === false) {
            return false;
        }
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/TimeSlotServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                // console.log($(form).serialize());
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
                // console.log(msg);
                if (msg["status"] === true) {
                    shownotification(msg["statusText"]);
                    loadtimeslottable();
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
