package inverse;

public class Config {
    public static final String URL = "jdbc:mysql://localhost:3306/automatictimetablegenerator";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Rana@16102000";
    public static final String BASEURL = "http://localhost:8080/Automated_Timetable_Generator_war_exploded";
    public static final String STATUSFILEPATH = "C:\\Users\\Suman Rana\\IdeaProjects\\Automated-Timetable-Generator\\src\\inverse\\output.txt";

    static String createid(int x) {
        StringBuilder sb = new StringBuilder(Integer.toString(x));
        StringBuilder sb1 = new StringBuilder();
        for (int i = sb.length(); i < 10; i++) {
            sb1.append(0);
        }
        sb1.append(sb);
        return sb1.toString();
    }
}
