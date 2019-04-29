package assets;

public class ClassRoom {
    private String Id;
    private String Name;

    public ClassRoom(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public ClassRoom(ClassRoom classRoom) {
        this.Id = classRoom.Id;
        this.Name = classRoom.Name;
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
