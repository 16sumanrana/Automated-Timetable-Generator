package inverse;

import java.util.Random;

public class GeneticAlgorithm {
    private Data Data;

    public GeneticAlgorithm(Data data) {
        this.Data = new Data(data);
    }

    public Population executeGeneticAlgorithm(Population Population) {
        Population NextPopulation = new Population(Population.getSchedules().size() * 2, Data);
//        System.out.println("Just Entered Into executeGeneticAlgorithm:");
//        Population.printPopulation();
        Population CrossoverPopulation = crossoverPopulation(Population);
//        System.out.println("After Crossover: Initial Population");
//        Population.printPopulation();
//        System.out.println("After Crossover: crossover Population");
//        CrossoverPopulation.printPopulation();
        Population MutatePopulation = mutatePopulation(Population);
//        System.out.println("After Mutation: Initial Population");
//        Population.printPopulation();
//        System.out.println("After Mutation: mutation Population");
//        MutatePopulation.printPopulation();
        Population NewPopulation = new Population(Population.getSchedules().size() - (CrossoverPopulation.getSchedules().size() + MutatePopulation.getSchedules().size()), Data);
//        System.out.println("After New: Initial Population");
//        Population.printPopulation();
//        System.out.println("After New: new Population");
//        NewPopulation.printPopulation();
        for (int i = 0; i < NextPopulation.getSchedules().size(); i++) {
            if (i < Population.getSchedules().size()) {
                NextPopulation.getSchedules().set(i, Population.getSchedules().get(i));
            } else if (i < Population.getSchedules().size() + CrossoverPopulation.getSchedules().size()) {
                NextPopulation.getSchedules().set(i, CrossoverPopulation.getSchedules().get(i - Population.getSchedules().size()));
            } else if (i < Population.getSchedules().size() + CrossoverPopulation.getSchedules().size() + MutatePopulation.getSchedules().size()) {
                NextPopulation.getSchedules().set(i, MutatePopulation.getSchedules().get(i - (Population.getSchedules().size() + CrossoverPopulation.getSchedules().size())));
            } else {
                NextPopulation.getSchedules().set(i, NewPopulation.getSchedules().get(i - (Population.getSchedules().size() + CrossoverPopulation.getSchedules().size() + MutatePopulation.getSchedules().size())));
            }
        }
//        System.out.println("After Copying:");
//        Population.printPopulation();
//        System.out.println("Large Next Population:");
//        NextPopulation.printPopulation();
//        System.out.println();
        NextPopulation.sortByFitness();
//        System.out.println("Large Next Population After Sorting:");
//        NextPopulation.printPopulation();
        NextPopulation.getSchedules().subList(Main.NUMBER_OF_SCHEDULES, NextPopulation.getSchedules().size()).clear();
//        System.out.println("Large Next Population After removing:");
//        NextPopulation.printPopulation();
        return NextPopulation;
    }

    private Population selectTournamentPopulation(Population Population) {
        Population TournamentPopulation = new Population(Main.NUMBER_OF_TOURNAMENT_SELECTION, Data);
        for (int i = 0; i < Main.NUMBER_OF_TOURNAMENT_SELECTION; i++) {
            TournamentPopulation.getSchedules().set(i, new Schedule(Population.getSchedules().get((new Random().nextInt(Main.BOUND_OF_RANDOM)) % Population.getSchedules().size())));
        }
        return TournamentPopulation;
    }

    public Population crossoverPopulation(Population Population) {
        int crossoverPopulationSize = (int) (Main.PERCENTAGE_OF_CROSSOVER * Population.getSchedules().size());
        Population crossoverPopulation = new Population(Population);
//        System.out.println("Before" + crossoverPopulation.getSchedules().size());
//        crossoverPopulation.getSchedules().subList(crossoverPopulationSize, Population.getSchedules().size()).clear();
//        System.out.println("After" + crossoverPopulation.getSchedules().size());
//        crossoverPopulation.getSchedules().clear();
        double fitnesssum = 0;
        for (int i = 0; i < Population.getSchedules().size(); i++) {
            fitnesssum += Population.getSchedules().get(i).getFitness();
        }
        Population tempPopulation = new Population(0, Data);
        for (int i = 0; i < crossoverPopulation.getSchedules().size(); i++) {
            int x, y, k = 0;
            double roullete = Math.random() * fitnesssum;
            double fatherfitness = 0, motherfitness = 0;
            while (fatherfitness < roullete) {
                fatherfitness += Population.getSchedules().get(k).getFitness();
            }
            x = k;
            roullete = Math.random() * fitnesssum;
            k = 0;
            while (motherfitness < roullete) {
                motherfitness += Population.getSchedules().get(k).getFitness();
            }
            y = k;
//            int x = new Random().nextInt(Main.BOUND_OF_RANDOM) % crossoverPopulationSize;
//            int y = new Random().nextInt(Main.BOUND_OF_RANDOM) % crossoverPopulationSize;
//            for (int k = i + 1; k < crossoverPopulation.getSchedules().size(); k++) {
//                int x = i, y = k;
            int ChangesLimit = new Random().nextInt(Main.BOUND_OF_RANDOM) % crossoverPopulation.getSchedules().get(x).getPeriods().size();
//                int ChangesLimit = 2;
//                System.out.println("Changes " + x + " " + y + " " + ChangesLimit);
            for (int j = ChangesLimit; j < crossoverPopulation.getSchedules().get(x).getPeriods().size(); j++) {
                Period TempPeriod = crossoverPopulation.getSchedules().get(x).getPeriods().get(j);
                crossoverPopulation.getSchedules().get(x).getPeriods().set(j, crossoverPopulation.getSchedules().get(y).getPeriods().get(j));
                crossoverPopulation.getSchedules().get(y).getPeriods().set(j, TempPeriod);
            }
//                System.out.println("crossover");
//                crossoverPopulation.printPopulation();
            if (crossoverPopulation.getSchedules().get(x).getFitness() >= crossoverPopulation.getSchedules().get(y).getFitness()) {
                tempPopulation.getSchedules().add(new Schedule(crossoverPopulation.getSchedules().get(x)));
            } else {
                tempPopulation.getSchedules().add(new Schedule(crossoverPopulation.getSchedules().get(y)));
            }
//            tempPopulation.getSchedules().add(new Schedule(crossoverPopulation.getSchedules().get(x)));
//            tempPopulation.getSchedules().add(new Schedule(crossoverPopulation.getSchedules().get(y)));
//            }
        }
//        System.out.println("Size:" + tempPopulation.getSchedules().size());
//        tempPopulation.printPopulation();
        tempPopulation.sortByFitness().getSchedules().subList(crossoverPopulationSize, tempPopulation.getSchedules().size()).clear();
        crossoverPopulation = tempPopulation;
//        crossoverPopulation.sortByFitness().getSchedules().subList(crossoverPopulationSize, crossoverPopulation.getSchedules().size()).clear();
//        System.out.println("temp population");
//        tempPopulation.printPopulation();
//        System.out.println("After Crossover Function");
//        crossoverPopulation.printPopulation();
//        System.out.println("After Crossover crossoverpopulation");
        return crossoverPopulation;
    }

    public Schedule crossoverSchedule(Schedule schedule1, Schedule schedule2) {
        Schedule crossoverSchedule = new Schedule(schedule1.getData());
        crossoverSchedule.initialize();
        int ChangesLimit = new Random().nextInt(Main.BOUND_OF_RANDOM) % schedule1.getPeriods().size();
        for (int i = 0; i < ChangesLimit; i++) {
            crossoverSchedule.getPeriods().set(i, schedule1.getPeriods().get(i));
        }
        for (int i = ChangesLimit; i < schedule1.getPeriods().size(); i++) {
            crossoverSchedule.getPeriods().set(i, schedule2.getPeriods().get(i));
        }
        return crossoverSchedule;
    }

    public Population mutatePopulation(Population Population) {
        int mutatePopulationSize = (int) (Main.PERCENTAGE_OF_MUTATION * Population.getSchedules().size());
        Population mutatePopulation = new Population(Population);
        for (int i = 0; i < mutatePopulation.getSchedules().size(); i++) {
            mutatePopulation.getSchedules().set(i, mutateSchedule(mutatePopulation.getSchedules().get(i)));
        }
//        System.out.println("In mutatePopulation function:");
//        mutatePopulation.printPopulation();
//        for (int i = 0; i < mutatePopulationSize; i++) {
//            mutatePopulation.getSchedules().set(i, new Schedule(mutateSchedule(selectTournamentPopulation(Population).sortByFitness().getSchedules().get(0))));
//        }
        mutatePopulation.sortByFitness().getSchedules().subList(mutatePopulationSize, Population.getSchedules().size()).clear();
        return mutatePopulation;
    }

    public Schedule mutateSchedule(Schedule mutateSchedule) {
        Schedule schedule = new Schedule(mutateSchedule.getData());
        schedule.initialize();
        if (schedule.getFitness() > mutateSchedule.getFitness()) {
            return schedule;
        }
        double oldfitness = mutateSchedule.getFitness();
        int changes = 0;
        while (mutateSchedule.getFitness() <= oldfitness) {
            int i = new Random().nextInt(Main.BOUND_OF_RANDOM) % mutateSchedule.getPeriods().size();
            mutateSchedule.getPeriods().set(i, schedule.getPeriods().get(i));
            mutateSchedule.setFitnessChanged(true);
            changes++;
            if (changes > Main.MAXIMUM_MUTATION_CHANGES) {
                break;
            }
        }
        return mutateSchedule;
    }
}
