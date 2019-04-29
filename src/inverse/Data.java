package inverse;

import assets.*;
import assets.Time;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Data {
    private ArrayList<Day> Days = new ArrayList<>();
    private ArrayList<Time> Times = new ArrayList<>();
    private ArrayList<ClassRoom> ClassRooms = new ArrayList<>();
    private Map<String, ArrayList<Subject>> Subjects = new HashMap<>();                                                 // map<classroomid, ArrayList<Subject>>
    private ArrayList<Teacher> Teachers = new ArrayList<>();

    public Data(Data data) {
        this.Days = new ArrayList<>(data.Days);
        this.Times = new ArrayList<>(data.Times);
        this.ClassRooms = new ArrayList<>(data.ClassRooms);
        this.Subjects = new HashMap<>(data.Subjects);
        this.Teachers = new ArrayList<>(data.Teachers);
//        for (int i = 0; i < data.Days.size(); i++) {
//            this.Days.add(new Day(data.Days.get(i)));
//        }
//        for (int i = 0; i < data.Times.size(); i++) {
//            this.Times.add(new Time(data.Times.get(i)));
//        }
//        for (int i = 0; i < data.ClassRooms.size(); i++) {
//            this.ClassRooms.add(new ClassRoom(data.ClassRooms.get(i)));
//        }
//        for (int i = 0; i < data.Subjects.size(); i++) {
//            this.Subjects.add(new Subject(data.Subjects.get(i)));
//        }
//        for (int i = 0; i < data.Teachers.size(); i++) {
//            this.Teachers.add(new Teacher(data.Teachers.get(i)));
//        }
    }

    public Data() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {

        }
        try {
            String url = "jdbc:mysql://localhost:3306/automatictimetablegenerator";
            String username = "root";
            String password = "Rana@16102000";
            String query;
            Connection con = DriverManager.getConnection(url, username, password);
            query = "select * from day";
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(query);
            while (res.next()) {
                Days.add(new Day(res.getString("dayid"), res.getString("dayname"), res.getInt("dayperiods")));
            }
            query = "select * from timeslot";
            res = st.executeQuery(query);
            while (res.next()) {
                Times.add(new Time(res.getString("timeslotid"), res.getString("timeslotstarttime") + "-" + res.getString("timeslotendtime")));
            }
            query = "select * from classroom";
            res = st.executeQuery(query);
            while (res.next()) {
                ClassRooms.add(new ClassRoom(res.getString("classroomid"), res.getString("classroomname")));
                query = "select * from subjectteacherclassroom " +
                        "left join subject on subjectteacherclassroom.subjectid=subject.subjectid " +
                        "left join teacher on subjectteacherclassroom.teacherid=teacher.teacherid " +
                        "where classroomid=\"" + res.getString("classroomid") + "\" " +
                        "order by subjectteacherclassroom.subjectid";
                Statement st1 = con.createStatement();
                ResultSet rs = st1.executeQuery(query);
                Map<String, ArrayList<Teacher>> teachers = new HashMap<>();
                Map<String, String> subname = new HashMap<>();
                while (rs.next()) {
                    subname.putIfAbsent(rs.getString("subjectid"), rs.getString("subjectname"));
                    teachers.putIfAbsent(rs.getString("subjectid"), new ArrayList<>());
                    teachers.get(rs.getString("subjectid")).add(new Teacher(rs.getString("teacherid"), rs.getString("teachername")));
                }
                rs.close();
                st1.close();
                for (Map.Entry<String, ArrayList<Teacher>> techer : teachers.entrySet()) {
                    Subjects.putIfAbsent(res.getString("classroomid"), new ArrayList<>());
                    Subjects.get(res.getString("classroomid")).add(new Subject(techer.getKey(), subname.get(techer.getKey()), techer.getValue()));
                }
            }
            query = "select * from teacher";
            res = st.executeQuery(query);
            while (res.next()) {
                Teachers.add(new Teacher(res.getString("teacherid"), res.getString("teachername")));
            }
        } catch (Exception e) {
            System.err.println(e);
//            out.print("{" +
//                    "\"status\":" + false + "," +
//                    "\"statusText\":" + "\"Some wrong information!\"" +
//                    "}");
        }
//        String DayId[] = {"DAY00", "DAY01", "DAY02", "DAY03", "DAY04"};
//        String DayName[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
//        for (int i = 0; i < Main.NUMBER_OF_DAYS; i++) {
//            Days.add(new Day(DayId[i], DayName[i]));
//        }
//        String TimeId[] = {"TIME00", "TIME01", "TIME02", "TIME03", "TIME04", "TIME05", "TIME06", "TIME07"};
//        String TimeDuration[] = {"10:45-11:30", "11:30-12:10", "12:20-13:00", "13:00-13:40", "14:00-14:35", "14:35-15:10", "15:10-15:45", "15:45-16:15"};
//        for (int i = 0; i < Main.NUMBER_OF_TIME_PER_DAY; i++) {
//            Times.add(new Time(TimeId[i], TimeDuration[i]));
//        }
//        String ClassRoomId[] = {"CLASS00", "CLASS01", "CLASS02", "CLASS03", "CLASS04", "CLASS05"};
//        String ClassRoomName[] = {"CLASS 05", "CLASS 06", "CLASS 07", "CLASS 08", "CLASS 09", "CLASS 10"};
//        for (int i = 0; i < Main.NUMBER_OF_CLASS; i++) {
//            ClassRooms.add(new ClassRoom(ClassRoomId[i], ClassRoomName[i]));
//        }
//        String TeacherName[] = {"Mr. js", "Mr. hs", "Ms. te", "Mr. yt", "Mr. ew", "Mr. iu", "Mr. ty", "Mr. st", "Mr. tc", "Mr. sd", "Mr. qw", "Mr. cd", "Mr. ur", "Mr. op", "Mr. ng", "Mr. gh", "Mr. bt", "Mr. xe"};
//        String SubjectName[] = {"BENGALI", "ENGLISH", "MATHEMATICS", "SCIENCE", "HISTORY", "GEOGRAPHY"};
//        for (int i = 0; i < Main.NUMBER_OF_SUBJECTS_PER_CLASS; i++) {
//            Subjects.add(new Subject(createSubjectId(i), SubjectName[i], new ArrayList<>(
//                    Arrays.asList(new Teacher(createTeacherId(i * 3), TeacherName[i * 3]),
//                            new Teacher(createTeacherId(i * 3 + 1), TeacherName[i * 3 + 1]),
//                            new Teacher(createTeacherId(i * 3 + 2), TeacherName[i * 3 + 2]))
//            )));
//        }
//        for (int i = 0; i < Main.NUMBER_OF_CLASS * Main.NUMBER_OF_SUBJECTS_PER_CLASS; i++) {
//            int x = i % SubjectName.length;
//            x *= 3;
//            Subjects.add(new Subject(createSubjectId(i), SubjectName[i % SubjectName.length], new ArrayList<>(
//                    Arrays.asList(new Teacher(createTeacherId(x), TeacherName[x]),
//                            new Teacher(createTeacherId(x + 1), TeacherName[x + 1]),
//                            new Teacher(createTeacherId(x + 2), TeacherName[x + 2]))
//            )));
//        }
//        for (int i = 0; i < Main.NUMBER_OF_TEACHERS; i++) {
//            Teachers.add(new Teacher(createTeacherId(i), TeacherName[i]));
//        }
    }

    private String createSubjectId(int i) {
        if (i < 10) {
            return "SUBJECT".concat("0" + Integer.toString(i));
        }
        return "SUBJECT".concat(Integer.toString(i));
    }

    private String createTeacherId(int i) {
        if (i < 10) {
            return "TEACHER".concat("0" + Integer.toString(i));
        }
        return "TEACHER".concat(Integer.toString(i));
    }

    public void printData() {
        System.out.println("Available Days:");
        Days.forEach(x -> {
            System.out.print(x);
        });
        System.out.println();
        System.out.println("Available Times:");
        Times.forEach(x -> {
            System.out.print(x);
        });
        System.out.println();
        System.out.println("Available ClassRooms:");
        ClassRooms.forEach(x -> {
            System.out.print(x);
        });
        System.out.println();
        System.out.println("Available Subjects:");
        for (Map.Entry<String, ArrayList<Subject>> subject : Subjects.entrySet()) {
            System.out.print(subject.getKey() + "->");
            System.out.println(subject.getValue());
        }
        System.out.println();
        System.out.println("Available Teachers:");
        Teachers.forEach(x -> {
            System.out.print(x);
        });
        System.out.println();
    }

    public ArrayList<Day> getDays() {
        return Days;
    }

    public ArrayList<Time> getTimes() {
        return Times;
    }

    public ArrayList<ClassRoom> getClassRooms() {
        return ClassRooms;
    }

    public Map<String, ArrayList<Subject>> getSubjects() {
        return Subjects;
    }

    public ArrayList<Teacher> getTeachers() {
        return Teachers;
    }
}
