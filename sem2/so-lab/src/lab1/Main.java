package lab1;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int PROCESS_COUNT = 1000;
    private static final int TIME_QUANTUM = 5;
    private static final int ITERATIONS = 30;
    private static final String[] ALGORITHMS = {"FCFS", "SJF", "SRTF", "RR"};

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Map<String, Result>>> futures = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            int finalI = i;
            futures.add(executor.submit(() -> runIteration(finalI)));
        }

        Map<String, double[]> aggregated = new LinkedHashMap<>();
        for (String algo : ALGORITHMS) {
            aggregated.put(algo, new double[]{0, 0, 0});
        }

        for (Future<Map<String, Result>> future : futures) {
            Map<String, Result> iterationResults = future.get(); // blocking call
            for (String algo : ALGORITHMS) {
                Result result = iterationResults.get(algo);
                double[] sums = aggregated.get(algo);
                sums[0] += result.avgCompletionTime;
                sums[1] += result.getProcessSwitches();
                sums[2] += result.getStarvedProcesses();
                aggregated.put(algo, sums);
            }
        }

        executor.shutdown();

        Map<String, Result> finalResults = new LinkedHashMap<>();
        for (String key : aggregated.keySet()) {
            double[] sums = aggregated.get(key);
            double avgCompletionTime = sums[0] / ITERATIONS;
            int avgSwitches = (int) Math.round(sums[1] / ITERATIONS);
            int avgStarved = (int) Math.round(sums[2] / ITERATIONS);
            finalResults.put(key, new Result(avgCompletionTime, avgSwitches, avgStarved));
        }

        printResultsTable(finalResults);

        String bestAlgorithm = finalResults.entrySet().stream()
                .min(Comparator.comparingDouble(e -> e.getValue().avgCompletionTime))
                .map(Map.Entry::getKey)
                .orElse("N/A");
        System.out.println("Najlepszy algorytm: " + bestAlgorithm);
    }

    private static Map<String, Result> runIteration(int i) {
        Map<String, Result> iterationResults = new LinkedHashMap<>();

        List<Process> processes = generateProcesses();
        Result fcfsResult = new FCFS(processes).run();
        System.out.println("Iteration " + (i+1) + " FCFS done");
        iterationResults.put("FCFS", fcfsResult);
        Result sjfResult = new SJF(processes).run();
        System.out.println("Iteration " + (i+1) + " SJF done");
        iterationResults.put("SJF", sjfResult);
        Result srtfResult = new SRTF(processes).run();
        System.out.println("Iteration " + (i+1) + " SRTF done");
        iterationResults.put("SRTF", srtfResult);
        Result rrResult = new RR(processes, TIME_QUANTUM).run();
        System.out.println("Iteration " + (i+1) + " RR done");
        iterationResults.put("RR", rrResult);

        return iterationResults;
    }

    private static List<Process> generateProcesses() {
        Random rand = new Random();
        int pid = 0;
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < Main.PROCESS_COUNT; i++) {
            pid++;
            processes.add(new Process(rand.nextInt(2000), rand.nextInt(100) + 1, pid));
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