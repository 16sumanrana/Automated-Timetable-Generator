package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet(name = "SubjectServlet", urlPatterns = {"/SubjectServlet"})
public class SubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // load subject table <start>
        if (request.getParameter("isloadsubjecttable") != null &&
                Boolean.valueOf(request.getParameter("isloadsubjecttable"))) {
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
                String query = "select * from subject";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                String tablerow = "";
                while (res.next()) {
                    tablerow = tablerow.concat("<tr>");
                    tablerow = tablerow.concat("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow = tablerow.concat("<td>" + res.getString("subjectname") + "</td>");
                    StringBuilder editform = new StringBuilder();
                    editform.append("<form class=\\\"edit-subject\\\" onsubmit=\\\"return showeditform(this);\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"subjectid\\\" value=\\\"" + res.getString("subjectid") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"subjectname\\\" value=\\\"" + res.getString("subjectname") + "\\\">");
                    editform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-info btn-sm\\\" name=\\\"action\\\" value=\\\"edit\\\">Edit</button>");
                    editform.append("</form>");
                    tablerow = tablerow.concat("<td>" + editform + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-subject\\\" onsubmit=\\\"return deletesubject(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeletesubject\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"subjectid\\\" value=\\\"" + res.getString("subjectid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"subjectname\\\" value=\\\"" + res.getString("subjectname") + "\\\">");
                    deleteform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-danger btn-sm\\\" name=\\\"action\\\" value=\\\"delete\\\">Delete</button>");
                    deleteform.append("</form>");
                    tablerow = tablerow.concat("<td>" + deleteform + "</td>");
                    tablerow = tablerow.concat("</tr>");
                }
                out.print("{" +
                        "\"status\":" + true + "," +
                        "\"statusText\":" + "\"" + tablerow + "\"" +
                        "}");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + e.getMessage() +
                        "}");
            }
        }
        // load subject table <end>

        // add subject <start>
        if (request.getParameter("isaddsubject") != null &&
                Boolean.valueOf(request.getParameter("isaddsubject")) &&
                request.getParameter("subjectname") != null) {
            String subjectname = request.getParameter("subjectname");
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
                query = "select max(id) from subject";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into subject values(?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, "SUBJECT".concat(Config.createid(lastid)));
                prst.setString(3, subjectname);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Subject is added successfully!\"" +
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
        // add subject <end>

        // edit subject <start>
        if (request.getParameter("iseditsubject") != null &&
                Boolean.valueOf(request.getParameter("iseditsubject")) &&
                request.getParameter("subjectid") != null &&
                request.getParameter("subjectname") != null) {
            String subjectname = request.getParameter("subjectname");
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
                query = "update subject set subjectname=? where subjectid=?";
                Statement st = con.createStatement();
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, subjectname);
                prst.setString(2, subjectid);
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
        // edit subject <end>

        // delete subject <start>
        if (request.getParameter("isdeletesubject") != null &&
                Boolean.valueOf(request.getParameter("isdeletesubject")) &&
                request.getParameter("subjectid") != null) {
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
                query = "delete from subject where subjectid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, subjectid);
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
        // delete subject <end>
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
