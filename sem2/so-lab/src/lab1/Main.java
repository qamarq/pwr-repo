package lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int processCount = 10000;
        int timeQuantum = 2;

        List<Process> processes = generateProcesses(processCount);
        Map<String, Result> results = new LinkedHashMap<>();

        results.put("FCFS", new FCFS(processes).run());
        results.put("SJF", new SJF(processes).run());
        results.put("RR", new RR(processes, timeQuantum).run());

        results.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(r -> r.avgCompletionTime)))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("Najlepszy algorytm: " + results.entrySet().stream().min(Map.Entry.comparingByValue(Comparator.comparingDouble(r -> r.avgCompletionTime))).get().getKey());
    }

    private static List<Process> generateProcesses(int count) {
        Random rand = new Random();
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            processes.add(new Process(rand.nextInt(201), rand.nextInt(10) + 1));
        }
        return processes;
    }
}
