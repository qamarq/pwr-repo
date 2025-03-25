package lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int processCount = 1000;
        int timeQuantum = 2;
        int iterations = 30;

        String[] algorithms = {"FCFS", "SJF", "SRTF", "RR"};
        Map<String, double[]> aggregated = new LinkedHashMap<>();
        for (String algo : algorithms) {
            aggregated.put(algo, new double[]{0, 0, 0});
        }

        for (int i = 0; i < iterations; i++) {
            List<Process> processes = generateProcesses(processCount);

            // run each algorithm for current iteration and update the aggregated sums
            Result fcfsResult = new FCFS(processes).run();
            updateAggregated(aggregated, "FCFS", fcfsResult);
            System.out.println("Iteration " + (i+1) + " FCFS done");

            // generate processes again to reset the state
            processes = generateProcesses(processCount);
            Result sjfResult = new SJF(processes).run();
            updateAggregated(aggregated, "SJF", sjfResult);
            System.out.println("Iteration " + (i+1) + " SJF done");

            processes = generateProcesses(processCount);
            Result srtfResult = new SRTF(processes).run();
            updateAggregated(aggregated, "SRTF", srtfResult);
            System.out.println("Iteration " + (i+1) + " SRTF done");

            processes = generateProcesses(processCount);
            Result rrResult = new RR(processes, timeQuantum).run();
            updateAggregated(aggregated, "RR", rrResult);
            System.out.println("Iteration " + (i+1) + " RR done");
        }

        // compute averages for each algorithm
        Map<String, Result> finalResults = new LinkedHashMap<>();
        for (String key : aggregated.keySet()) {
            double[] sums = aggregated.get(key);
            double avgCompletionTime = sums[0] / iterations;
            int avgSwitches = (int) Math.round(sums[1] / iterations);
            int avgStarved = (int) Math.round(sums[2] / iterations);
            finalResults.put(key, new Result(avgCompletionTime, avgSwitches, avgStarved));
        }

        printResultsTable(finalResults);

        String bestAlgorithm = finalResults.entrySet().stream()
                .min(Comparator.comparingDouble(e -> e.getValue().avgCompletionTime))
                .map(Map.Entry::getKey)
                .orElse("N/A");
        System.out.println("Najlepszy algorytm: " + bestAlgorithm);
    }

    private static void updateAggregated(Map<String, double[]> aggregated, String key, Result result) {
        double[] sums = aggregated.get(key);
        sums[0] += result.avgCompletionTime;
        sums[1] += result.getProcessSwitches();
        sums[2] += result.getStarvedProcesses();
        aggregated.put(key, sums);
    }

    private static List<Process> generateProcesses(int count) {
        Random rand = new Random();
        int pid = 0;
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            pid++;
            processes.add(new Process(rand.nextInt(20), rand.nextInt(100) + 1, pid));
        }
        return processes;
    }

    private static void printResultsTable(Map<String, Result> results) {
        String line = "+------------+----------------------+-----------------+--------------------+";
        System.out.println(line);
        System.out.printf("| %-10s | %-20s | %-15s | %-18s |\n",
                "Algorithm", "Avg Completion Time", "Switches", "Starved Processes");
        System.out.println(line);

        for (Map.Entry<String, Result> entry : results.entrySet()) {
            System.out.printf("| %-10s | %-20.2f | %-15d | %-18d |\n",
                    entry.getKey(),
                    entry.getValue().avgCompletionTime,
                    entry.getValue().getProcessSwitches(),
                    entry.getValue().getStarvedProcesses());
        }
        System.out.println(line);
    }
}
