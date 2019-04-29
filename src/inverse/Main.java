package inverse;

import assets.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Main", urlPatterns = {"/Main"})
public class Main extends HttpServlet {
    public static final int NUMBER_OF_SCHEDULES = 10;
    public static final int NUMBER_OF_TOURNAMENT_SELECTION = 1;
    public static final int BOUND_OF_RANDOM = 10000;
    public static final double PERCENTAGE_OF_CROSSOVER = 0.5;
    public static final double PERCENTAGE_OF_MUTATION = 0.25;
    public static final int MAXIMUM_NUMBER_OF_GENERATION = 50000;
    public static final int MAXIMUM_MUTATION_CHANGES = 1000;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (request.getParameter("isGenerate") != null &&
                Boolean.valueOf(request.getParameter("isGenerate"))) {
            try {
                Data Data = new Data();
//                Data.printData();
                Population Population = new Population(NUMBER_OF_SCHEDULES, Data).sortByFitness();
                GeneticAlgorithm GeneticAlgorithm = new GeneticAlgorithm(Data);
                int NumberOfGeneration = 0;
                while (NumberOfGeneration < MAXIMUM_NUMBER_OF_GENERATION && Population.getSchedules().get(0).getFitness() != 1.0) {
                    NumberOfGeneration++;
                    try {
                        File file = new File(Config.STATUSFILEPATH);
                        FileWriter fstream = new FileWriter(file, false);
                        BufferedWriter fileout = new BufferedWriter(fstream);
                        fileout.write("[ Generation #" + NumberOfGeneration + ": " + Population.getSchedules().get(0).getFitness() + ", " + Population.getSchedules().get(0).getNoOfConflicts() + " ]");
                        fileout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e);
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Something went wrong!\"" +
                                "}");
                        return;
                    }
//                    Population.printPopulation();
                    Population = GeneticAlgorithm.executeGeneticAlgorithm(Population);
                }
                try {
                    File file = new File(Config.STATUSFILEPATH);
                    FileWriter fstream = new FileWriter(file, false);
                    BufferedWriter fileout = new BufferedWriter(fstream);
                    fileout.write("After Termination Of Genetic Algorithm." + " Generation #" + NumberOfGeneration);
                    fileout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                    return;
                }
                try {
                    if (Population.getSchedules().get(0).getFitness() == 1.0) {
                        StringBuilder timetable = new StringBuilder();
                        for (int i = 0; i < Data.getClassRooms().size(); i++) {
                            String ClassRoomId = Data.getClassRooms().get(i).getId();
                            StringBuilder returnTable = new StringBuilder("<div class=\\\"table-responsive\\\">" +
                                    "<table class=\\\"table table-sm table-hover table-dark table-hover table-striped table-bordered\\\">" +
                                    "<caption>" +
                                    Data.getClassRooms().get(i).getName() +
                                    "</caption>");
                            returnTable.append("<thead><tr>");
                            returnTable.append("<th scope=\\\"col\\\">" + "Day/Time" + "</th>");
                            for (Time time : Data.getTimes()) {
                                returnTable.append("<th scope=\\\"col\\\">" + time.getDuration() + "</th>");
                            }
                            returnTable.append("</tr></thead>");
                            Map<String, String> DayTable = new HashMap<>();
                            for (int j = 0; j < Population.getSchedules().get(0).getPeriods().size(); j++) {
                                if (Population.getSchedules().get(0).getPeriods().get(j).getClassRoom().getId().equals(ClassRoomId)) {
                                    DayTable.putIfAbsent(Population.getSchedules().get(0).getPeriods().get(j).getDay().getId(), "");
                                    DayTable.put(Population.getSchedules().get(0).getPeriods().get(j).getDay().getId(),
                                            DayTable.get(Population.getSchedules().get(0).getPeriods().get(j).getDay().getId()).concat("<td>" +
                                                    Population.getSchedules().get(0).getPeriods().get(j).getSubject().getName() + "<br>" +
                                                    Population.getSchedules().get(0).getPeriods().get(j).getTeacher().getName() + "</td>"));

                                }
                            }
                            for (int j = 0; j < Data.getDays().size(); j++) {
                                returnTable.append("<tr>" +
                                        "<th scope=\\\"row\\\">" + Data.getDays().get(j).getName() + "</th>" +
                                        DayTable.get(Data.getDays().get(j).getId()) +
                                        "</tr>");
                            }
                            returnTable.append("</table></div>");
                            System.out.println("\"" + returnTable.toString() + "\"");
                            timetable.append(returnTable);
                        }
                        System.out.println(timetable);
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
                            String url = "jdbc:mysql://localhost:3306/automatictimetablegenerator";
                            String username = "root";
                            String password = "Rana@16102000";
                            String query;
                            Connection con = DriverManager.getConnection(url, username, password);
                            query = "update timetable set timetableobject=? where id=?";
                            Statement st = con.createStatement();
                            PreparedStatement prst = con.prepareStatement(query);
                            prst.setObject(1, timetable);
                            prst.setInt(2, Integer.parseInt("1"));
                            int count = prst.executeUpdate();
                            if (count > 0) {
                                out.print("{" +
                                        "\"status\":" + true + "," +
                                        "\"statusText\":" + "\"" + timetable.toString() + "\"" +
                                        "}");
                            } else {
                                out.print("{" +
                                        "\"status\":" + false + "," +
                                        "\"statusText\":" + "\"Something went wrong!\"" +
                                        "}");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e);
                            out.print("{" +
                                    "\"status\":" + false + "," +
                                    "\"statusText\":" + "\"Some wrong information!\"" +
                                    "}");
                        }
                    } else {
                        out.print("{" +
                                "\"status\":" + false + "," +
                                "\"statusText\":" + "\"Failed to create error less Table! Try again:(\"" +
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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                try {
                    File file = new File(Config.STATUSFILEPATH);
                    FileWriter fstream = new FileWriter(file, false);
                    BufferedWriter fileout = new BufferedWriter(fstream);
                    fileout.write(e.getMessage());
                    fileout.close();
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(e2);
                    out.print("{" +
                            "\"status\":" + false + "," +
                            "\"statusText\":" + "\"Something went wrong!\"" +
                            "}");
                }
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
