package assets;

public class Teacher {
    private String Id;
    private String Name;

    public Teacher(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public Teacher(Teacher teacher) {
        this.Id = teacher.Id;
        this.Name = teacher.Name;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String toString() {
        return "[ " + Id + ", " + Name + " ]";
    }
}
