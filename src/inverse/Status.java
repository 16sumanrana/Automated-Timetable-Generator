package inverse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "Status", urlPatterns = {"/Status"})
public class Status extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            File file = new File(Config.STATUSFILEPATH);
            FileReader fstream = new FileReader(file);
            BufferedReader statusfile = new BufferedReader(fstream);
            String status = statusfile.readLine();
            statusfile.close();
            out.print("{" +
                    "\"status\":" + true + "," +
                    "\"statusText\":" + "\"" + status + "\"" +
                    "}");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            out.print("{" +
                    "\"status\":" + true + "," +
                    "\"statusText\":" + "\"" + e.getMessage() + "\"" +
                    "}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
