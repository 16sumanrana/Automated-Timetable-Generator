package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "DayServlet", urlPatterns = {"/DayServlet"})
public class DayServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isloaddaytable") != null &&
                Boolean.valueOf(request.getParameter("isloaddaytable"))) {
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
                String query = "select * from day";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                int cnt = 1;
                StringBuilder tablerow = new StringBuilder();
                while (res.next()) {
                    tablerow.append("<tr>");
                    tablerow.append("<th scope=\\\"row\\\">" + cnt++ + "</th>");
                    tablerow.append("<td>" + res.getString("dayname") + "</td>");
                    tablerow.append("<td>" + res.getString("dayperiods") + "</td>");
                    StringBuilder editform = new StringBuilder();
                    editform.append("<form class=\\\"edit-day\\\" onsubmit=\\\"return showeditform(this);\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"dayid\\\" value=\\\"" + res.getString("dayid") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"dayname\\\" value=\\\"" + res.getString("dayname") + "\\\">");
                    editform.append("<input type=\\\"hidden\\\" name=\\\"dayperiods\\\" value=\\\"" + res.getString("dayperiods") + "\\\">");
                    editform.append("<button type=\\\"submit\\\" class=\\\"btn btn-outline-info btn-sm\\\" name=\\\"action\\\" value=\\\"edit\\\">Edit</button>");
                    editform.append("</form>");
                    tablerow.append("<td>" + editform + "</td>");
                    StringBuilder deleteform = new StringBuilder();
                    deleteform.append("<form class=\\\"delete-day\\\" onsubmit=\\\"return deleteday(this);\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"isdeleteday\\\" value=\\\"" + true + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"dayid\\\" value=\\\"" + res.getString("dayid") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"dayname\\\" value=\\\"" + res.getString("dayname") + "\\\">");
                    deleteform.append("<input type=\\\"hidden\\\" name=\\\"dayperiods\\\" value=\\\"" + res.getString("dayperiods") + "\\\">");
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

        if (request.getParameter("isaddday") != null &&
                Boolean.valueOf(request.getParameter("isaddday")) &&
                request.getParameter("dayname") != null &&
                request.getParameter("dayperiods") != null) {
            String dayname = request.getParameter("dayname");
            String dayperiods = request.getParameter("dayperiods");
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
                query = "select max(id) from day";
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);
                res.next();
                int lastid = res.getInt("max(id)");
                query = "insert into day values(?, ?, ?, ?)";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setInt(1, ++lastid);
                prst.setString(2, "DAY".concat(Config.createid(lastid)));
                prst.setString(3, dayname);
                prst.setInt(4, Integer.parseInt(dayperiods));
                int count = prst.executeUpdate();
                if (count > 0) {
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"Day is added successfully!\"" +
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

        if (request.getParameter("iseditday") != null &&
                Boolean.valueOf(request.getParameter("iseditday")) &&
                request.getParameter("dayid") != null &&
                request.getParameter("dayname") != null &&
                request.getParameter("dayperiods") != null) {
            String dayid = request.getParameter("dayid");
            String dayname = request.getParameter("dayname");
            String dayperiods = request.getParameter("dayperiods");
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
                query = "update day set dayname=?, dayperiods=? where dayid=?";
                Statement st = con.createStatement();
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, dayname);
                prst.setInt(2, Integer.parseInt(dayperiods));
                prst.setString(3, dayid);
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

        if (request.getParameter("isdeleteday") != null &&
                Boolean.valueOf(request.getParameter("isdeleteday")) &&
                request.getParameter("dayid") != null) {
            String dayid = request.getParameter("dayid");
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
                query = "delete from day where dayid=?";
                PreparedStatement prst = con.prepareStatement(query);
                prst.setString(1, dayid);
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
