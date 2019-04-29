package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "IndexServlet", urlPatterns = {"/IndexServlet"})
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isload") != null &&
                Boolean.valueOf(request.getParameter("isload"))) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Class file not found!\"" +
                        "}");
                return;
            }
            try {
                String query;
                Connection con = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
                query = "select * from timetable where id=1";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    byte[] buf = rs.getBytes("timetableobject");
                    ObjectInputStream objectIn = null;
                    if (buf != null)
                        objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
                    Object deSerializedObject = objectIn.readObject();
                    StringBuilder timetable = (StringBuilder) deSerializedObject;
//                    System.out.println(timetable);
                    out.print("{" +
                            "\"status\":" + true + "," +
                            "\"statusText\":" + "\"" + timetable.toString() + "\"" +
                            "}");
                } else {
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Doesn't find any data!\"" +
                            "}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                out.print("{" +
                        "\"status\":" + false + "," +
                        "\"statusText\":" + "\"Something went wrong!\"" +
                        "}");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
