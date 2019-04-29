package assets;

public class Time {
    private String Id;
    private String Duration;

    public Time(String Id, String Duration) {
        this.Id = Id;
        this.Duration = Duration;
    }

    public Time(Time time) {
        this.Id = time.Id;
        this.Duration = time.Duration;
    }

    public String getId() {
        return Id;
    }

    public String getDuration() {
        return Duration;
    }

    public String toString() {
        return "[ " + Id + ", " + Duration + " ]";
    }
}
