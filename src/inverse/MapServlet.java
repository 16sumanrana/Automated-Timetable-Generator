package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "MapServlet", urlPatterns = {"/MapServlet"})
public class MapServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isload") != null &&
                Boolean.valueOf(request.getParameter("isload")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("subjectid") != null) {
            String classroomid = request.getParameter("classroomid");
            String subjectid = request.getParameter("subjectid");
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
                String query = "select * from subjectteacherclassroom " +
                        "left join teacher on subjectteacherclassroom.teacherid=teacher.teacherid " +
                        "where subjectteacherclassroom.classroomid=\"" + classroomid + "\" " +
                        "and subjectteacherclassroom.subjectid=\"" + subjectid + "\" " +
                        "order by subjectteacherclassroom.teacherid";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                StringBuilder table = new StringBuilder("<div class=\\\"table-responsive\\\">" +
                        "<table class=\\\"table table-sm table-hover table-dark table-hover table-striped table-bordered\\\">" +
                        "<caption>Teacher Table</caption>" +
                        "<thead>" +
                        "<tr>" +
                        "<th scope=\\\"col\\\">#</th>" +
                        "<th scope=\\\"col\\\">Name</th>" +
                        "<th scope=\\\"col\\\">Delete</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>");
                StringBuilder tablerow = new StringBuilder();
                while (res.next()) {
                    tablerow.append("<tr>");
                    tablerow.append("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow.append("<td>" + res.getString("teachername") + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-teacher\\\" onsubmit=\\\"return deleteclassroomsubjectteacher(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeleteclassroomsubjectteacher\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"classroomid\\\" value=\\\"" + res.getString("classroomid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"subjectid\\\" value=\\\"" + res.getString("subjectid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacherid\\\" value=\\\"" + res.getString("subjectteacherclassroom.teacherid") + "\\\">");
                    deleteform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-danger btn-sm\\\" name=\\\"action\\\" value=\\\"delete\\\">Delete</button>");
                    deleteform.append("</form>");
                    tablerow.append("<td>" + deleteform + "</td>");
//                    tablerow.append("<td><button type=\\\"button\\\" class=\\\"btn btn-outline-danger btn-sm\\\">Delete</button></td>");
                    tablerow.append("</tr>");
                }
                table.append(tablerow);
                table.append("</tbody>" +
                        "</table>" +
                        "</div>");
                out.print("{" +
                        "\"status\":" + true + "," +
                        "\"statusText\":" + "\"" + table.toString() + "\"" + "," +
                        "\"classroomid\":" + "\"" + classroomid + "\"" + "," +
                        "\"subjectid\":" + "\"" + subjectid + "\"" +
                        "}");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + e.getMessage() +
                        "}");
            }
        }

        if (request.getParameter("isaddteacher") != null &&
                Boolean.valueOf(request.getParameter("isaddteacher")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("subjectid") != null &&
                request.getParameter("teacherid") != null) {
            String classroomid = request.getParameter("classroomid");
            String subjectid = request.getParameter("subjectid");
            String teacherid = request.getParameter("teacherid");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                System.err.println(e.getMessage());
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query;
                query = "select max(id) from subjectteacherclassroom";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into subjectteacherclassroom values(?, ?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, subjectid);
                prst.setString(3, teacherid);
                prst.setString(4, classroomid);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Teacher is added successfully!\"" + "," +
                            "\"classroomid\":" + "\"" + classroomid + "\"" + "," +
                            "\"subjectid\":" + "\"" + subjectid + "\"" +
                            "}");
                } else {
                    System.out.println("sql error");
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Some wrong information!\"" +
                        "}");
            }
        }

        if (request.getParameter("isdeleteclassroomsubjectteacher") != null &&
                Boolean.valueOf(request.getParameter("isdeleteclassroomsubjectteacher")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("subjectid") != null &&
                request.getParameter("teacherid") != null) {
            String classroomid = request.getParameter("classroomid");
            String subjectid = request.getParameter("subjectid");
            String teacherid = request.getParameter("teacherid");
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
                query = "delete from subjectteacherclassroom where classroomid=? and subjectid=? and teacherid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, classroomid);
                prst.setString(2, subjectid);
                prst.setString(3, teacherid);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Deleted Successfully\"" + "," +
                            "\"classroomid\":" + "\"" + classroomid + "\"" + "," +
                            "\"subjectid\":" + "\"" + subjectid + "\"" +
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
