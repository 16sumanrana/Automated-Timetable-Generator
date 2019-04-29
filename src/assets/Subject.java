package assets;

import java.util.ArrayList;

public class Subject {
    private String Id;
    private String Name;
    private ArrayList<Teacher> Teachers = new ArrayList<>();

    public Subject(String Id, String Name, ArrayList<Teacher> Teachers) {
        this.Id = Id;
        this.Name = Name;
        this.Teachers = new ArrayList<>(Teachers);
    }

    public Subject(Subject subject) {
        this.Id = subject.Id;
        this.Name = subject.Name;
        for (int i = 0; i < subject.Teachers.size(); i++) {
            this.Teachers.add(new Teacher(subject.Teachers.get(i)));
        }
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Teacher> getTeachers() {
        return Teachers;
    }

    public String toString() {
        return "[ " + Id + ", " + Name + ", " + Teachers.toString() + " ]";
    }
}
