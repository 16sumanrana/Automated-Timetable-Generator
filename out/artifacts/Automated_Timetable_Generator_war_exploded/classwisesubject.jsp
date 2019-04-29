<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="inverse.Config" %>
<%--
  Created by IntelliJ IDEA.
  User: Suman Rana
  Date: 25-03-2019
  Time: 16:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Class Wise Subjects | Automated Timetable Generator</title>
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
<body onload="loadallclassroomsubjecttable();">
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
                <li class="nav-item">
                    <a class="nav-link" href="time.jsp">Time Slots</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="subject.jsp">Subjects</a>
                </li>
                <li class="nav-item active">
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
<%
    StringBuilder tabletemplate1 = new StringBuilder("<div class=\"table-responsive\">");
    tabletemplate1.append("<table class=\"table table-sm table-hover table-dark table-hover table-striped table-bordered\">");
    tabletemplate1.append("<caption>");
    StringBuilder tabletemplate2 = new StringBuilder("</caption>");
    tabletemplate2.append("<thead>");
    tabletemplate2.append("<tr>");
    tabletemplate2.append("<th scope=\"col\">#</th>");
    tabletemplate2.append("<th scope=\"col\">Subjects</th>");
    tabletemplate2.append("<th scope=\"col\">Delete</th>");
    tabletemplate2.append("</tr>");
    tabletemplate2.append("</thead>");
    tabletemplate2.append("<tbody id=\"");
    StringBuilder tabletemplate3 = new StringBuilder("\">");
    tabletemplate3.append("</tbody>");
    tabletemplate3.append("</table>");
    tabletemplate3.append("</div>");
    StringBuilder sb = new StringBuilder();
    try {
        Class.forName("com.mysql.jdbc.Driver");
    } catch (Exception e) {
        out.println("<p class=\"error\">" + e.getMessage() + "Class file not found!</p>");
    }
    try {
        String url = "jdbc:mysql://localhost:3306/automatictimetablegenerator";
        String username = "root";
        String password = "Rana@16102000";
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();
        String query = "select * from classroom";
        ResultSet res = st.executeQuery(query);
        ArrayList<String> classroomid = new ArrayList<>();
        ArrayList<String> classroomname = new ArrayList<>();
        ArrayList<String> subjectid = new ArrayList<>();
        ArrayList<String> subjectname = new ArrayList<>();
        while (res.next()) {
            classroomid.add(res.getString("classroomid"));
            classroomname.add(res.getString("classroomname"));
        }
        query = "select * from subject";
        res = st.executeQuery(query);
        while (res.next()) {
            subjectid.add(res.getString("subjectid"));
            subjectname.add(res.getString("subjectname"));
        }
%>
<section>
    <div class="container">
        <br>
        <br>
        <!-- Button trigger modal -->
        <div class="row mb-3" style="text-align: center;">
            <div class="col">
                <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#exampleModalCenter">
                    Add Subject
                </button>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="addclassroomsubject" onsubmit="return addclassroomsubject(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLongTitle">Add Subject</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="isaddsubject" value="true">
                            <div class="form-group">
                                <label for="exampleFormControlSelect1">Select Class: </label>
                                <select class="form-control form-control-sm" id="exampleFormControlSelect1"
                                        name="classroomid">
                                    <%
                                        for (int i = 0; i < classroomid.size(); i++) {
                                            out.print("<option value=\"" + classroomid.get(i) + "\">" + classroomname.get(i) + "</option>");
                                        }
                                    %>
                                    <%--<option>Class 5</option>--%>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="exampleFormControlSelect2">Select Subject: </label>
                                <select class="form-control form-control-sm" id="exampleFormControlSelect2"
                                        name="subjectid">
                                    <%
                                        for (int i = 0; i < subjectid.size(); i++) {
                                            out.print("<option value=\"" + subjectid.get(i) + "\">" + subjectname.get(i) + "</option>");
                                        }
                                    %>
                                    <%--<option>Subject Name</option>--%>
                                </select>
                            </div>
                            <div class="d-flex justify-content-center">
                                <small id="add-status" style="display: none;">Add Status</small>
                            </div>
                        </div>
                        <div class="modal-footer" style="justify-content: space-between;">
                            <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-info">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%
                for (int i = 0; i < classroomid.size(); i++) {
                    out.print(tabletemplate1);
                    out.print(classroomname.get(i));
                    out.print(tabletemplate2);
                    out.print(classroomid.get(i));
                    out.print(tabletemplate3);
                }
                sb.append("[");
                for (int i = 0; i < classroomid.size(); i++) {
                    sb.append("\"").append(classroomid.get(i)).append("\"");
                    if (i + 1 < classroomid.size()) {
                        sb.append(",");
                    }
                }
                sb.append("]");
            } catch (Exception e) {
                out.print("<strong class=\"error\">" + e.getMessage() + "</strong>");
            }
        %>
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
    var classroomid =<%= sb.toString()%>;

    function loadallclassroomsubjecttable() {
        for (var i = 0; i < classroomid.length; i++) {
            loadclassroomsubjecttable(classroomid[i]);
        }
    }

    function loadclassroomsubjecttable(classroomid) {
        // console.log(classroomid);
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/ClassWiseSubjectServlet",
            data: {
                loadclasssubjecttable: true,
                classroomid: classroomid
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
                shownotification(msg);
                // $('#' + classroomid).html(msg);
            }
        }).done(function (msg) {
            // console.log(msg);
            if (msg["status"] === true) {
                $('#' + classroomid).html(msg["statusText"]);
            } else {
                shownotification(msg["statusText"]);
                // $('#' + classroomid).html(msg["statusText"]);
            }
        });
    }

    function addclassroomsubject(form) {
        var addstatus = $('#add-status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/ClassWiseSubjectServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                addstatus.css('display', 'none');
            },
            error: function (jqXHR, exception) {
                // console.log(jqXHR);
                // console.log(exception);
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
                addstatus.css('display', 'block');
                addstatus.css('color', 'red');
                addstatus.html(msg);
                setTimeout(function () {
                    addstatus.css('display', 'none');
                }, 5000);
            }
        }).done(function (msg) {
            // console.log(msg);
            addstatus.css('display', 'block');
            if (msg["status"] === true) {
                addstatus.css('color', 'green');
                addstatus.html(msg["statusText"]);
                loadclassroomsubjecttable(msg["classroomid"]);
            } else {
                addstatus.css('color', 'red');
                addstatus.html(msg["statusText"]);
            }
            setTimeout(function () {
                addstatus.css('display', 'none');
            }, 5000);
        });
        return false;
    }

    function deleteclassroomsubject(form) {
        if (confirmdeletion() === false) {
            return false;
        }
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/ClassWiseSubjectServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                closenotification();
                // console.log($(form).serialize());
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
                shownotification(msg);
            },
            success: function (msg) {
                // console.log(msg);
                if (msg["status"] === true) {
                    shownotification(msg["statusText"]);
                    loadclassroomsubjecttable(msg["classroomid"]);
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
