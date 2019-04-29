package assets;

public class Day {
    private String Id;
    private String Name;
    private int Periods;

    public Day(String Id, String Name, int Periods) {
        this.Id = Id;
        this.Name = Name;
        this.Periods = Periods;
    }

    public Day(Day day) {
        this.Id = day.Id;
        this.Name = day.Name;
        this.Periods = day.Periods;
    }

    public String getId() { return Id; }

    public String getName() {
        return Name;
    }

    public int getPeriods() {
        return Periods;
    }

    public String toString() {
        return "[ " + Id + ", " + Name + " ]";
    }
}
