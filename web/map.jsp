<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="inverse.Config" %>
<%--
  Created by IntelliJ IDEA.
  User: Suman Rana
  Date: 26-03-2019
  Time: 02:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Map | Automated Timetable Generator</title>
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
<body onload="return loadallsubjectteachertable();">
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
                <li class="nav-item">
                    <a class="nav-link" href="classwisesubject.jsp">Class Wise Subjects</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="teacher.jsp">Teachers</a>
                </li>
                <li class="nav-item active">
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
                    Add Teacher
                </button>
            </div>
        </div>
        <%!
            public String javascriptarray(ArrayList<String> arr) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < arr.size(); i++) {
                    sb.append("\"").append(arr.get(i)).append("\"");
                    if (i != arr.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
                return sb.toString();
            }
        %>
        <%
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            ArrayList<String> classroomid = new ArrayList<>();
            ArrayList<String> classroomname = new ArrayList<>();
            ArrayList<String> teacherid = new ArrayList<>();
            ArrayList<String> teachername = new ArrayList<>();
            StringBuilder classroomsubject = new StringBuilder();
            classroomsubject.append("{");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                out.println("<p class=\"error\">" + e.getMessage() + "<br>Class file not found!</p>");
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query = "select * from classroom";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                // for each classroom
                while (res.next()) {
                    out.print("<div class=\"row\">");
                    out.print("<div class=\"col-4\">");
                    out.print("<div class=\"list-group\" role=\"tablist\">");
                    String query1 = "select * from subjectclassroom " +
                            "left join subject on subjectclassroom.subjectid=subject.subjectid " +
                            "where subjectclassroom.classroomid=\"" + res.getString("classroomid") + "\" " +
                            "order by subjectclassroom.subjectid";
                    Statement st1 = con.createStatement();
                    ResultSet rs = st1.executeQuery(query1);
                    ArrayList<String> subjectid = new ArrayList<>();
                    ArrayList<String> subjectname = new ArrayList<>();
                    // printing all tab list items <#classroomid-subjectid> <start>
                    if (rs.next()) {
                        out.print("<a class=\"list-group-item list-group-item-action active\" data-toggle=\"list\"" +
                                " href=\"#" + res.getString("classroomid") + "-" + rs.getString("subjectid") + "\" role=\"tab\">" +
                                rs.getString("subjectname") + "</a>");
                        subjectid.add(rs.getString("subjectid"));
                        subjectname.add(rs.getString("subjectname"));
                    }
                    while (rs.next()) {
                        out.print("<a class=\"list-group-item list-group-item-action\" data-toggle=\"list\"" +
                                " href=\"#" + res.getString("classroomid") + "-" + rs.getString("subjectid") +
                                "\" role=\"tab\">" + rs.getString("subjectname") + "</a>");
                        subjectid.add(rs.getString("subjectid"));
                        subjectname.add(rs.getString("subjectname"));
                    }
                    // printing all tab list items <#classroomid-subjectid> <end>
                    rs.close();
                    st1.close();
                    sb.append("\"" + res.getString("classroomid") + "\": ");
                    sb.append(javascriptarray(subjectid));
                    sb.append(",");
                    out.print("<br>");
                    out.print("<b style=\"color: #edff00;\">" + res.getString("classroomname") + "</b>");
                    out.print("</div>");
                    out.print("</div>");
                    // printing table format
                    out.print("<div class=\"col-8\">");
                    out.print("<div class=\"tab-content\">");
                    if (subjectid.size() != 0) {
                        out.print("<div class=\"tab-pane fade show active\" id=\"" + res.getString("classroomid") + "-" + subjectid.get(0) + "\" role=\"tabpanel\">");
                        out.print("</div>");
                    }
                    for (int i = 1; i < subjectid.size(); i++) {
                        out.print("<div class=\"tab-pane fade\" id=\"" + res.getString("classroomid") + "-" + subjectid.get(i) + "\" role=\"tabpanel\">");
                        out.print("</div>");
                    }
                    out.print("</div>");
                    out.print("</div>");
                    out.print("</div>");
                    out.print("<br><br>");
                    classroomid.add(res.getString("classroomid"));
                    classroomname.add(res.getString("classroomname"));
                    classroomsubject.append("\"" + res.getString("classroomid") + "\":");
                    StringBuilder subject = new StringBuilder();
                    for (int i = 0; i < subjectid.size(); i++) {
                        subject.append("<option value=\\\"" + subjectid.get(i) + "\\\">" + subjectname.get(i) + "</option>");
                    }
                    classroomsubject.append("\"" + subject + "\"");
                    classroomsubject.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");
                classroomsubject.deleteCharAt(classroomsubject.length() - 1);
                classroomsubject.append("}");
                query = "select * from teacher";
                res = st.executeQuery(query);
                while (res.next()) {
                    teacherid.add(res.getString("teacherid"));
                    teachername.add(res.getString("teachername"));
                }
            } catch (Exception e) {
                out.print("<p class=\"error\">" + e.getMessage() + "</p>");
//                e.printStackTrace();
            }
        %>
        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <form id="addteacher" onsubmit="return addteacher(this);">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLongTitle">Add Teacher</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" style="text-align: center;">
                            <input type="hidden" name="isaddteacher" value="true">
                            <div class="form-group">
                                <label for="classroomid">Select Class: </label>
                                <select class="form-control form-control-sm" id="classroomid"
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
                                <label for="subjectid">Select Subject: </label>
                                <select class="form-control form-control-sm" id="subjectid"
                                        name="subjectid">
                                    <%--<option>Subject Name</option>--%>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="teacherid">Select Teacher: </label>
                                <select class="form-control form-control-sm" id="teacherid"
                                        name="teacherid">
                                    <%
                                        for (int i = 0; i < teacherid.size(); i++) {
                                            out.print("<option value=\"" + teacherid.get(i) + "\">" + teachername.get(i) + "</option>");
                                        }
                                    %>
                                    <%--<option>Teacher Name</option>--%>
                                </select>
                            </div>
                            <small id="add-status" style="display: none;">Status</small>
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
    var classroomsubject =<%= sb.toString()%>;
    // console.log(classroomsubject);
    var classroomsubjectoption =<%= classroomsubject.toString()%>;
    // console.log(classroomsubjectoption);
    $(document).ready(function () {
        $('#subjectid').html(classroomsubjectoption[Object.keys(classroomsubjectoption)[0]]);
        $('#classroomid').change(function () {
            // console.log($(this).children("option:selected").val());
            $('#subjectid').html(classroomsubjectoption[$(this).children("option:selected").val()]);
        });
    });

    function loadallsubjectteachertable() {
        for (var key in classroomsubject) {
            for (var i = 0; i < classroomsubject[key].length; i++) {
                loadsubjectteachertable(key, classroomsubject[key][i]);
            }
        }
    }

    function loadsubjectteachertable(classroomid, subjectid) {
        // console.log(classroomid);
        // console.log(subjectid);
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/MapServlet",
            data: {
                classroomid: classroomid,
                subjectid: subjectid,
                isload: true
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
                // $('#' + msg["classroomid"] + '-' + msg["subjectid"]).html(msg);
            }
        }).done(function (msg) {
            // console.log(msg);
            if (msg["status"] === true) {
                $('#' + msg["classroomid"] + '-' + msg["subjectid"]).html(msg["statusText"]);
            } else {
                // console.log(msg["statusText"]);
                shownotification(msg["statusText"]);
            }
        });
    }

    function addteacher(form) {
        var addstatus = $('#add-status');
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/MapServlet",
            data: $(form).serialize(),
            beforeSend: function () {
                addstatus.css('display', 'none');
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
                addstatus.css('display', 'block');
                addstatus.css('color', 'red');
                addstatus.html(msg["statusText"]);
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
                loadsubjectteachertable(msg["classroomid"], msg["subjectid"]);
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

    function deleteclassroomsubjectteacher(form) {
        if (confirmdeletion() === false) {
            return false;
        }
        $.ajax({
            method: "post",
            url: "<%out.print(Config.BASEURL);%>/MapServlet",
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
                    loadsubjectteachertable(msg["classroomid"], msg["subjectid"]);
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
