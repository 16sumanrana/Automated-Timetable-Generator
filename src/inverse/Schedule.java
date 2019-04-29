package inverse;

import assets.ClassRoom;
import assets.Day;
import assets.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Schedule {
    private ArrayList<Period> Periods = new ArrayList<>();
    private Data Data;
    private int NoOfConflicts = 0;
    private double Fitness = -1;
    private boolean IsFitnessChanged = true;

    public Schedule(Data data) {
        this.Data = new Data(data);
    }

    public Schedule(Schedule schedule) {
        for (int i = 0; i < schedule.Periods.size(); i++) {
            this.Periods.add(new Period(schedule.Periods.get(i)));
        }
        this.Data = new Data(schedule.Data);
        this.NoOfConflicts = schedule.NoOfConflicts;
        this.Fitness = schedule.Fitness;
        this.IsFitnessChanged = schedule.IsFitnessChanged;
    }

    public void initialize() {
        for (int i = 0; i < Data.getClassRooms().size(); i++) {                                                         // select class
            for (int j = 0; j < Data.getDays().size(); j++) {                                                           // select day
                for (int k = 0; k < Data.getDays().get(j).getPeriods(); k++) {                                          // select time
                    Subject TempSubject = Data.getSubjects().get(Data.getClassRooms().get(i).getId()).get(new Random().nextInt(Main.BOUND_OF_RANDOM) % Data.getSubjects().get(Data.getClassRooms().get(i).getId()).size());
                    Periods.add(new Period(
                            Data.getClassRooms().get(i),
                            Data.getDays().get(j),
                            Data.getTimes().get(k),
                            TempSubject,
                            TempSubject.getTeachers().get((new Random().nextInt(Main.BOUND_OF_RANDOM)) % TempSubject.getTeachers().size())
                    ));
                }
            }
        }
    }

    private double calculateFitness() {
        int NoOfConflicts = 0;
        // map to store all periods of a specific class map<class,periods>
        Map<String, ArrayList<Period>> ClassRoomSchedule = new HashMap<>();
        for (int i = 0; i < Periods.size(); i++) {
            ClassRoomSchedule.putIfAbsent(Periods.get(i).getClassRoom().getId(), new ArrayList<>());
            ClassRoomSchedule.get(Periods.get(i).getClassRoom().getId()).add(Periods.get(i));
            // check if two consecutive period is same i.e. same subject
            if (i != Periods.size() - 1 && Periods.get(i).getSubject().getId().equals(Periods.get(i + 1).getSubject().getId())) {
                NoOfConflicts++;
            }
            for (int j = i + 1; j < Periods.size(); j++) {
                // check if two period is same i.e. same class, same day, same time, same subject, same teacher
                if (Periods.get(i).getClassRoom().getId().equals(Periods.get(j).getClassRoom().getId()) &&              // same class
                        Periods.get(i).getDay().getId().equals(Periods.get(j).getDay().getId()) &&                      // same day
                        Periods.get(i).getTime().getId().equals(Periods.get(j).getTime().getId()) &&                    // same time
                        Periods.get(i).getSubject().getId().equals(Periods.get(j).getSubject().getId()) &&              // same subject
                        Periods.get(i).getTeacher().getId().equals(Periods.get(j).getTeacher().getId())                 // same teacher
                ) {
                    NoOfConflicts++;
                }
                // check if same teacher is allotted to different class at same day and same time
                if (!Periods.get(i).getClassRoom().getId().equals(Periods.get(j).getClassRoom().getId()) &&             // different class
                        Periods.get(i).getDay().getId().equals(Periods.get(j).getDay().getId()) &&                      // same day
                        Periods.get(i).getTime().getId().equals(Periods.get(j).getTime().getId()) &&                    // same time
                        Periods.get(i).getTeacher().getId().equals(Periods.get(j).getTeacher().getId())                 // same teacher
                ) {
                    NoOfConflicts++;
                }
                // check if at same class, same day, same time two different teacher or two different subject
                if (Periods.get(i).getClassRoom().getId().equals(Periods.get(j).getClassRoom().getId()) &&              // same class
                        Periods.get(i).getDay().getId().equals(Periods.get(j).getDay().getId()) &&                      // same day
                        Periods.get(i).getTime().getId().equals(Periods.get(j).getTime().getId()) &&                    // same time
                        (!Periods.get(i).getSubject().getId().equals(Periods.get(j).getSubject().getId()) ||            // different subject or
                                !Periods.get(i).getTeacher().getId().equals(Periods.get(j).getTeacher().getId()))       // different teacher
                ) {
                    NoOfConflicts++;
                }
            }
        }
        // for every classroom
        for (ClassRoom ClassRoom : Data.getClassRooms()) {
            // map to store number of classes for a teacher for specific class map<teacher, no of classes>
            Map<String, Integer> TeacherMap = new HashMap<>();
            // map to store periods for specific day map<day, periods>
            Map<String, ArrayList<Period>> DayPeriodMap = new HashMap<>();
            for (Period period : ClassRoomSchedule.get(ClassRoom.getId())) {
                DayPeriodMap.putIfAbsent(period.getDay().getId(), new ArrayList<>());
                DayPeriodMap.get(period.getDay().getId()).add(period);
            }
            // for each day
            for (Day day : Data.getDays()) {
                Map<String, Integer> TeacherPeriodMap = new HashMap<>();
                // for each period
                for (Period period : DayPeriodMap.get(day.getId())) {
                    TeacherPeriodMap.putIfAbsent(period.getTeacher().getId(), 0);
                    TeacherPeriodMap.put(period.getTeacher().getId(), TeacherPeriodMap.get(period.getTeacher().getId()) + 1);
                }
                for (Map.Entry<String, Integer> teacher : TeacherPeriodMap.entrySet()) {
                    if (teacher.getValue() > 2) {                                                                       // max 1 class per day for a teacher
                        NoOfConflicts++;
                    }
                }
            }
//            for (Period Period : ClassRoomSchedule.get(ClassRoom.getId())) {
//                TeacherMap.putIfAbsent(Period.getTeacher().getId(), 0);
//                TeacherMap.put(Period.getTeacher().getId(), TeacherMap.get(Period.getTeacher().getId()) + 1);
//            }
//            for (Map.Entry<String, Integer> Teacher : TeacherMap.entrySet()) {
//                if (Teacher.getValue() > 4) {                                                                           // max 4 classes per week
//                    NoOfConflicts++;
//                } else if (Teacher.getValue() < 0) {                                                                    // min 1 class per week
//                    NoOfConflicts++;
//                }
//            }
        }
        this.Fitness = (double) 1 / (NoOfConflicts + 1);
        this.NoOfConflicts = NoOfConflicts;
        return Fitness;
    }

    public void setFitnessChanged(boolean value) {
        this.IsFitnessChanged = value;
    }

    public ArrayList<Period> getPeriods() {
        IsFitnessChanged = true;
        return Periods;
    }

    public Data getData() {
        return Data;
    }

    public int getNoOfConflicts() {
        if (IsFitnessChanged) {
            IsFitnessChanged = false;
            calculateFitness();
            return NoOfConflicts;
        }
        return NoOfConflicts;
    }

    public double getFitness() {
        if (IsFitnessChanged) {
            IsFitnessChanged = false;
            return calculateFitness();
        }
        return Fitness;
    }

    public String toString() {
        String ReturnValue = "[ SCHEDULE #: ";
//        ReturnValue = ReturnValue.concat(Id);
//        ReturnValue = ReturnValue.concat(", ");
//        for (Period x : Periods) {
//            ReturnValue = ReturnValue.concat("" + x.toString() + ",");
//        }
        ReturnValue = ReturnValue.concat(getFitness() + ", " + getNoOfConflicts());
        ReturnValue = ReturnValue.concat("]");
        return ReturnValue;
    }
}
