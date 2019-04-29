package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "ClassWiseSubjectServlet", urlPatterns = {"/ClassWiseSubjectServlet"})
public class ClassWiseSubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (request.getParameter("loadclasssubjecttable") != null &&
                Boolean.valueOf(request.getParameter("loadclasssubjecttable")) &&
                request.getParameter("classroomid") != null) {
            String classroomid = request.getParameter("classroomid");
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
                String query = "select * from subjectclassroom " +
                        "left join subject on subjectclassroom.subjectid=subject.subjectid " +
                        "where subjectclassroom.classroomid=\"" + classroomid + "\" " +
                        "order by subject.subjectid asc";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                StringBuilder tablerow = new StringBuilder();
                while (res.next()) {
                    tablerow.append("<tr>");
                    tablerow.append("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow.append("<td>" + res.getString("subjectname") + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-subject\\\" onsubmit=\\\"return deleteclassroomsubject(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeleteclassroomsubject\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"classroomid\\\" value=\\\"" + res.getString("classroomid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"subjectid\\\" value=\\\"" + res.getString("subjectid") + "\\\">");
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

        if (request.getParameter("isaddsubject") != null &&
                Boolean.valueOf(request.getParameter("isaddsubject")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("subjectid") != null) {
            String classroomid = request.getParameter("classroomid");
            String subjectid = request.getParameter("subjectid");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
                return;
            }
            try {
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                String query;
                query = "select max(id) from subjectclassroom";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into subjectclassroom values(?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, subjectid);
                prst.setString(3, classroomid);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Subject is added successfully!\"" + "," +
                            "\"classroomid\":" + "\"" + classroomid + "\"" +
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

        if (request.getParameter("isdeleteclassroomsubject") != null &&
                Boolean.valueOf(request.getParameter("isdeleteclassroomsubject")) &&
                request.getParameter("classroomid") != null &&
                request.getParameter("subjectid") != null) {
            String classroomid = request.getParameter("classroomid");
            String subjectid = request.getParameter("subjectid");
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
                query = "delete from subjectclassroom where classroomid=? and subjectid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, classroomid);
                prst.setString(2, subjectid);
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
