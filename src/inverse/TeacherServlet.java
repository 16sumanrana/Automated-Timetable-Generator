package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "TeacherServlet", urlPatterns = {"/TeacherServlet"})
public class TeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isloadteachertable") != null &&
                Boolean.valueOf(request.getParameter("isloadteachertable"))) {
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
                String query = "select * from teacher";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                String tablerow = "";
                while (res.next()) {
                    tablerow = tablerow.concat("<tr>");
                    tablerow = tablerow.concat("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow = tablerow.concat("<td>" + res.getString("teachername") + "</td>");
//                    tablerow = tablerow.concat("<td>" + "" + "</td>");
                    tablerow = tablerow.concat("<td>" + res.getString("teachersex") + "</td>");
                    tablerow = tablerow.concat("<td>" + res.getInt("teacherage") + "</td>");
                    tablerow = tablerow.concat("<td>" + res.getString("teacherphone") + "</td>");
                    tablerow = tablerow.concat("<td>" + res.getString("teacheremail") + "</td>");
                    tablerow = tablerow.concat("<td>" + res.getString("teacherinfo") + "</td>");
                    StringBuilder editform = new StringBuilder();
                    editform.append("<form class=\\\"edit-teacher\\\" onsubmit=\\\"return showeditform(this);\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teacherid\\\" value=\\\"" + res.getString("teacherid") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teachername\\\" value=\\\"" + res.getString("teachername") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teachersex\\\" value=\\\"" + res.getString("teachersex") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teacherage\\\" value=\\\"" + res.getString("teacherage") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teacherphone\\\" value=\\\"" + res.getString("teacherphone") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teacheremail\\\" value=\\\"" + res.getString("teacheremail") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"teacherinfo\\\" value=\\\"" + res.getString("teacherinfo") + "\\\">");
                    editform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-info btn-sm\\\" name=\\\"action\\\" value=\\\"edit\\\">Edit</button>");
                    editform.append("</form>");
                    tablerow = tablerow.concat("<td>" + editform + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-teacher\\\" onsubmit=\\\"return deleteteacher(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeleteteacher\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacherid\\\" value=\\\"" + res.getString("teacherid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teachername\\\" value=\\\"" + res.getString("teachername") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teachersex\\\" value=\\\"" + res.getString("teachersex") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacherage\\\" value=\\\"" + res.getString("teacherage") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacherphone\\\" value=\\\"" + res.getString("teacherphone") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacheremail\\\" value=\\\"" + res.getString("teacheremail") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"teacherinfo\\\" value=\\\"" + res.getString("teacherinfo") + "\\\">");
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

        if (request.getParameter("isaddteacher") != null &&
                Boolean.valueOf(request.getParameter("isaddteacher")) &&
                request.getParameter("teachername") != null &&
                request.getParameter("teachersex") != null &&
                request.getParameter("teacherage") != null &&
                request.getParameter("teacherphone") != null) {
            String teachername = request.getParameter("teachername");
            String teachersex = request.getParameter("teachersex");
            String teacherage = request.getParameter("teacherage");
            String teacherphone = request.getParameter("teacherphone");
            String teacheremail = request.getParameter("teacheremail");
            String teacherinfo = request.getParameter("teacherinfo");
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
                query = "select max(id) from teacher";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into teacher values(?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, "TEACHER".concat(Config.createid(lastid)));
                prst.setString(3, teachername);
                prst.setString(4, teachersex);
                prst.setInt(5, Integer.parseInt(teacherage));
                prst.setString(6, teacherphone);
                prst.setString(7, teacheremail);
                prst.setString(8, teacherinfo);
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Teacher is added successfully!\"" +
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

        if (request.getParameter("iseditteacher") != null &&
                Boolean.valueOf(request.getParameter("iseditteacher")) &&
                request.getParameter("teacherid") != null &&
                request.getParameter("teachername") != null &&
                request.getParameter("teachersex") != null &&
                request.getParameter("teacherage") != null &&
                request.getParameter("teacherphone") != null) {
            String teacherid = request.getParameter("teacherid");
            String teachername = request.getParameter("teachername");
            String teachersex = request.getParameter("teachersex");
            String teacherage = request.getParameter("teacherage");
            String teacherphone = request.getParameter("teacherphone");
            String teacheremail = request.getParameter("teacheremail");
            String teacherinfo = request.getParameter("teacherinfo");
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
                query = "update teacher set teachername=?, teachersex=?, teacherage=?, teacherphone=?, teacheremail=?, teacherinfo=? where teacherid=?";
                Statement st = con.createStatement();
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, teachername);
                prst.setString(2, teachersex);
                prst.setInt(3, Integer.parseInt(teacherage));
                prst.setString(4, teacherphone);
                prst.setString(5, teacheremail);
                prst.setString(6, teacherinfo);
                prst.setString(7, teacherid);
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

        if (request.getParameter("isdeleteteacher") != null &&
                Boolean.valueOf(request.getParameter("isdeleteteacher")) &&
                request.getParameter("teacherid") != null) {
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
                query = "delete from teacher where teacherid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, teacherid);
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
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
