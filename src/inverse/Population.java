package inverse;

import java.util.ArrayList;

public class Population {
    private ArrayList<Schedule> Schedules = new ArrayList<>();

    public Population(int Size, Data Data) {
        for (int i = 0; i < Size; i++) {
            Schedules.add(new Schedule(Data));
            Schedules.get(i).initialize();
        }
    }

    public Population(Population population) {
        for (int i=0;i<population.Schedules.size();i++){
            this.Schedules.add(new Schedule(population.Schedules.get(i)));
        }
    }

    public Population sortByFitness() {
        Schedules.sort((schedule1, schedule2) -> {
            if (schedule1.getFitness() > schedule2.getFitness()) {
                return -1;
            } else if (schedule1.getFitness() < schedule2.getFitness()) {
                return 1;
            }
            return 0;
        });
        return this;
    }

    public ArrayList<Schedule> getSchedules() {
        return Schedules;
    }

    public void printPopulation() {
        Schedules.forEach(x -> {
            System.out.println(x.toString());
        });
    }
}
