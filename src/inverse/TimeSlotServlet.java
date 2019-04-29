package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

@WebServlet(name = "TimeSlotServlet", urlPatterns = {"/TimeSlotServlet"})
public class TimeSlotServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isloadtimeslottable") != null &&
                Boolean.valueOf(request.getParameter("isloadtimeslottable"))) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Class file not found!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query = "select * from timeslot";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                StringBuilder tablerow = new StringBuilder();
                while (res.next()) {
                    tablerow.append("<tr>");
                    tablerow.append("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow.append("<td>" + res.getString("timeslotstarttime") + "</td>");
                    tablerow.append("<td>" + res.getString("timeslotendtime") + "</td>");
                    StringBuilder editform = new StringBuilder();
                    editform.append("<form class=\\\"edit-time-slot\\\" onsubmit=\\\"return showeditform(this);\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"timeslotid\\\" value=\\\"" + res.getString("timeslotid") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"timeslotstarttime\\\" value=\\\"" + res.getString("timeslotstarttime") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"timeslotendtime\\\" value=\\\"" + res.getString("timeslotendtime") + "\\\">");
                    editform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-info btn-sm\\\" name=\\\"action\\\" value=\\\"edit\\\">Edit</button>");
                    editform.append("</form>");
                    tablerow.append("<td>" + editform + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-time-slot\\\" onsubmit=\\\"return deletetimeslot(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeletetimeslot\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"timeslotid\\\" value=\\\"" + res.getString("timeslotid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"timeslotstarttime\\\" value=\\\"" + res.getString("timeslotstarttime") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"timeslotendtime\\\" value=\\\"" + res.getString("timeslotendtime") + "\\\">");
                    deleteform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-danger btn-sm\\\" name=\\\"action\\\" value=\\\"delete\\\">Delete</button>");
                    deleteform.append("</form>");
                    tablerow.append("<td>" + deleteform + "</td>");
                    tablerow.append("</tr>");
                }
                out.print("{" +
                        "\"status\":" + true + "," +
                        "\"statusText\":" + "\"" + tablerow.toString() + "\"" +
                        "}");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + e.getMessage() +
                        "}");
            }
        }

        if (request.getParameter("isedittimeslot") != null &&
                Boolean.valueOf(request.getParameter("isedittimeslot")) &&
                request.getParameter("timeslotid") != null &&
                request.getParameter("timeslotstarttime") != null &&
                request.getParameter("timeslotendtime") != null) {
            String timeslotid = request.getParameter("timeslotid");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query;
                Time timeslotstarttime = new Time(new SimpleDateFormat("HH:mm").parse(request.getParameter("timeslotstarttime")).getTime());
                Time timeslotendtime = new Time(new SimpleDateFormat("HH:mm").parse(request.getParameter("timeslotendtime")).getTime());
                if (timeslotendtime.compareTo(timeslotstarttime) < 0) {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"End time is smaller than start time!\"" +
                            "}");
                    return;
                }
                query = "select * from timeslot";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                while (res.next()) {
                    if (timeslotid.equals(res.getString("timeslotid"))) {
                        continue;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) < 0 && res.getTime("timeslotendtime").compareTo(timeslotstarttime) > 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotendtime) < 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) > 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) > 0 && res.getTime("timeslotstarttime").compareTo(timeslotendtime) < 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotendtime").compareTo(timeslotstarttime) > 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) < 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) == 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) == 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                }
                query = "update timeslot set timeslotstarttime=?, timeslotendtime=? where timeslotid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setTime(1, timeslotstarttime);
                prst.setTime(2, timeslotendtime);
                prst.setString(3, timeslotid);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Edited successfully!\"" +
                            "}");
                } else {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                }
            } catch (Exception e) {
                System.out.println(e);
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Some wrong information!\"" +
                        "}");
            }
        }

        if (request.getParameter("isdeletetimeslot") != null &&
                Boolean.valueOf(request.getParameter("isdeletetimeslot")) &&
                request.getParameter("timeslotid") != null) {
            String timeslotid = request.getParameter("timeslotid");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query;
                query = "delete from timeslot where timeslotid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, timeslotid);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Deleted successfully!\"" +
                            "}");
                } else {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                }
            } catch (Exception e) {
//                System.out.println(e);
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Some wrong information!\"" +
                        "}");
            }
        }

        if (request.getParameter("isaddtimeslot") != null &&
                Boolean.valueOf(request.getParameter("isaddtimeslot")) &&
                request.getParameter("timeslotstarttime") != null &&
                request.getParameter("timeslotendtime") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query;
                Time timeslotstarttime = new Time(new SimpleDateFormat("HH:mm").parse(request.getParameter("timeslotstarttime")).getTime());
                Time timeslotendtime = new Time(new SimpleDateFormat("HH:mm").parse(request.getParameter("timeslotendtime")).getTime());
                if (timeslotendtime.compareTo(timeslotstarttime) < 0) {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"End time is smaller than start time!\"" +
                            "}");
                    return;
                }
                query = "select * from timeslot";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int lastid = 0;
                while (res.next()) {
                    lastid = Math.max(res.getInt("id"), lastid);
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) < 0 && res.getTime("timeslotendtime").compareTo(timeslotstarttime) > 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotendtime) < 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) > 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) > 0 && res.getTime("timeslotstarttime").compareTo(timeslotendtime) < 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotendtime").compareTo(timeslotstarttime) > 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) < 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                    if (res.getTime("timeslotstarttime").compareTo(timeslotstarttime) == 0 && res.getTime("timeslotendtime").compareTo(timeslotendtime) == 0) {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Input constrains failed!\"" +
                                "}");
                        return;
                    }
                }
                query = "insert into timeslot values(?, ?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, "TIMESLOT".concat(Config.createid(lastid)));
                prst.setTime(3, timeslotstarttime);
                prst.setTime(4, timeslotendtime);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Time slot is added successfully!\"" +
                            "}");
                } else {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                }
            } catch (Exception e) {
//                System.out.println(e);
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Some wrong information!\"" +
                        "}");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
