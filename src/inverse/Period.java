package inverse;

import assets.*;

public class Period {
    private Day Day;
    private Time Time;
    private ClassRoom ClassRoom;
    private Subject Subject;
    private Teacher Teacher;

    public Period(ClassRoom ClassRoom, Day Day, Time Time, Subject Subject, Teacher Teacher) {
        this.Day = new Day(Day);
        this.Time = new Time(Time);
        this.ClassRoom = new ClassRoom(ClassRoom);
        this.Subject = new Subject(Subject);
        this.Teacher = new Teacher(Teacher);
    }

    public Period(Period period) {
        this.Day = new Day(period.Day);
        this.Time = new Time(period.Time);
        this.ClassRoom = new ClassRoom(period.ClassRoom);
        this.Subject = new Subject(period.Subject);
        this.Teacher = new Teacher(period.Teacher);
    }

    public Day getDay() {
        return Day;
    }

    public Time getTime() {
        return Time;
    }

    public ClassRoom getClassRoom() {
        return ClassRoom;
    }

    public Subject getSubject() {
        return Subject;
    }

    public Teacher getTeacher() {
        return Teacher;
    }

//    public String toString() {
//        return "[ " + Day.toString() + ", " + Time.toString() + ", " + ClassRoom.toString() + ", [" + Subject.getId() + ", " + Subject.getName() + "], " + Teacher.toString() + " ]";
//    }
}
