package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "ClassServlet", urlPatterns = {"/ClassServlet"})
public class ClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // load classroom table <start>
        if (request.getParameter("isloadclasstable") != null &&
                Boolean.valueOf(request.getParameter("isloadclasstable"))) {
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
                String query = "select * from classroom";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                StringBuilder tablerow = new StringBuilder();
                while (res.next()) {
                    tablerow.append("<tr>");
                    tablerow.append("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow.append("<td>" + res.getString("classroomname") + "</td>");
                    StringBuilder editform = new StringBuilder();
                    editform.append("<form class=\\\"edit-day\\\" onsubmit=\\\"return showeditform(this);\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"classroomid\\\" value=\\\"" + res.getString("classroomid") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"classroomname\\\" value=\\\"" + res.getString("classroomname") + "\\\">");
                    editform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-info btn-sm\\\" name=\\\"action\\\" value=\\\"edit\\\">Edit</button>");
                    editform.append("</form>");
                    tablerow.append("<td>" + editform + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-classroom\\\" onsubmit=\\\"return deleteclassroom(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeleteclassroom\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"classroomid\\\" value=\\\"" + res.getString("classroomid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"classroomname\\\" value=\\\"" + res.getString("classroomname") + "\\\">");
                    deleteform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-danger btn-sm\\\" name=\\\"action\\\" value=\\\"delete\\\">Delete</button>");
                    deleteform.append("</form>");
                    tablerow.append("<td>" + deleteform + "</td>");
                    tablerow.append("</tr>");
                }
                out.print("{" +
                        "\"status\":" + true + "," +
                        "\"statusText\":" + "\"" + tablerow + "\"" +
                        "}");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
            }
        }
        // load classroom table <end>

        // add classroom <start>
        if (request.getParameter("isaddclassroom") != null &&
                Boolean.valueOf(request.getParameter("isaddclassroom")) &&
                request.getParameter("classroomname") != null) {
            String classroomname = request.getParameter("classroomname");
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
                query = "select max(id) from classroom";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into classroom values(?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, "CLASSROOM".concat(Config.createid(lastid)));
                prst.setString(3, classroomname);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Class is added successfully!\"" +
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
        // add classroom <end>

        // edit classroom table <start>
        if (request.getParameter("iseditclassroom") != null &&
                Boolean.valueOf(request.getParameter("iseditclassroom")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("classroomname") != null) {
            String classroomid = request.getParameter("classroomid");
            String classroomname = request.getParameter("classroomname");
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
                query = "update classroom set classroomname=? where classroomid=?";
                Statement st = con.createStatement();
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, classroomname);
                prst.setString(2, classroomid);
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
//                System.out.println(e);
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Some wrong information!\"" +
                        "}");
            }
        }
        // edit classroom table <end>

        // delete classroom <start>
        if (request.getParameter("isdeleteclassroom") != null &&
                Boolean.valueOf(request.getParameter("isdeleteclassroom")) &&
                request.getParameter("classroomid") != null) {
            String classroomid = request.getParameter("classroomid");
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
                query = "delete from classroom where classroomid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, classroomid);
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
        // delete classroom <end>
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
