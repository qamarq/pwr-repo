package lab1;

import java.util.*;
import java.util.stream.Collectors;

public class MainTests {

    static class TestCase {
        int processCount;
        int avgArrival;
        int maxBurst;
        String description;

        public TestCase(int processCount, int avgArrival, int maxBurst, String description) {
            this.processCount = processCount;
            this.avgArrival = avgArrival;
            this.maxBurst = maxBurst;
            this.description = description;
        }

        @Override
        public String toString() {
            return processCount + " processes, avg arrival=" + avgArrival
                    + ", max burst=" + maxBurst + " : " + description;
        }
    }

    private static final TestCase[] TEST_CASES = new TestCase[]{
            new TestCase(5, 1, 5, "Few short jobs arriving quickly."),
            new TestCase(5, 10, 50, "Few long jobs arriving slowly."),
            new TestCase(10, 2, 10, "Moderate number of mixed jobs arriving frequently."),
            new TestCase(10, 10, 5, "Many short jobs but arriving sparsely."),
            new TestCase(10, 5, 20, "Balanced mix of short and long jobs."),
            new TestCase(20, 1, 5, "Many very short jobs, frequent arrivals (stress test SJF/SRTF)."),
            new TestCase(20, 5, 50, "Many long jobs with moderate arrivals."),
            new TestCase(20, 10, 20, "Even distribution of jobs, moderate scheduling challenge."),
            new TestCase(20, 15, 50, "Long jobs with a few arriving late."),
            new TestCase(50, 1, 5, "Very high process count, all short jobs, rapid arrivals."),
            new TestCase(50, 5, 50, "High process count with varied burst times."),
            new TestCase(50, 10, 10, "Many small jobs arriving in a controlled manner."),
            new TestCase(50, 20, 50, "Large batch of mostly long jobs, challenging RR."),
            new TestCase(10, 1, 50, "Few jobs, one very long job early (tests starvation risk)."),
            new TestCase(10, 10, 1, "All jobs are extremely short, with moderate spacing."),
            new TestCase(20, 10, 50, "Mixed job sizes, but spaced arrivals."),
            new TestCase(20, 1, 50, "One long job among many short ones (tests RR effectiveness)."),
            new TestCase(5, 5, 5, "Small balanced case, baseline performance check."),
            new TestCase(10, 3, 30, "Mid-range challenge with varied burst times."),
            new TestCase(50, 10, 5, "Many short jobs but slow arrivals, testing FCFS fairness.")
    };

    private static final int ITERATIONS = 30;
    private static final String[] ALGORITHMS = {"FCFS", "SJF", "SRTF", "RR"};

    public static void main(String[] args) {
        // Map to count wins per algorithm
        Map<String, List<Integer>> wins = new LinkedHashMap<>();
        for (String algo : ALGORITHMS) {
            wins.put(algo, new ArrayList<>());
        }

        // Process each test case
        for (int t = 0; t < TEST_CASES.length; t++) {
            TestCase tc = TEST_CASES[t];
            // Initialize aggregated sums for each algorithm:
            // [totalAvgCompletionTime, totalProcessSwitches, totalStarvedProcesses]
            Map<String, double[]> aggregated = new LinkedHashMap<>();
            for (String algo : ALGORITHMS) {
                aggregated.put(algo, new double[]{0, 0, 0});
            }

            // Run ITERATIONS for the current test case
            for (int i = 0; i < ITERATIONS; i++) {
                Result fcfsResult = new FCFS(generateProcesses(tc.processCount, tc.avgArrival, tc.maxBurst)).run();
                updateAggregated(aggregated, "FCFS", fcfsResult);
                Result sjfResult = new SJF(generateProcesses(tc.processCount, tc.avgArrival, tc.maxBurst)).run();
                updateAggregated(aggregated, "SJF", sjfResult);
                Result srtfResult = new SRTF(generateProcesses(tc.processCount, tc.avgArrival, tc.maxBurst)).run();
                updateAggregated(aggregated, "SRTF", srtfResult);
                // Fixed time quantum for RR set to 5
                Result rrResult = new RR(generateProcesses(tc.processCount, tc.avgArrival, tc.maxBurst), 5).run();
                updateAggregated(aggregated, "RR", rrResult);
            }

            // Compute average results per algorithm for this test case
            Map<String, Result> finalResults = new LinkedHashMap<>();
            for (String key : aggregated.keySet()) {
                double[] sums = aggregated.get(key);
                double avgCompletionTime = sums[0] / ITERATIONS;
                int avgSwitches = (int) Math.round(sums[1] / ITERATIONS);
                int avgStarved = (int) Math.round(sums[2] / ITERATIONS);
                finalResults.put(key, new Result(avgCompletionTime, avgSwitches, avgStarved));
            }

            // Determine best algorithm based on lowest avgCompletionTime
            Map.Entry<String, Result> bestEntry = finalResults.entrySet().stream()
                    .min(Comparator.comparingDouble(e -> e.getValue().avgCompletionTime))
                    .orElse(null);
            String bestAlgorithm = (bestEntry == null) ? "N/A" : bestEntry.getKey();
            double bestAvgCompletion = (bestEntry == null) ? 0 : bestEntry.getValue().avgCompletionTime;
            // Record win for summary table
            wins.get(bestAlgorithm).add(t);

            // Print detailed table for current test case
            System.out.println("\nTest Case " + t + ": " + tc);
            printResultsTable(finalResults);
            System.out.println("Winner: " + bestAlgorithm + " with Avg Completion Time = " + String.format("%.2f", bestAvgCompletion));
        }

        // After processing all test cases, print summary table
        printSummaryTable(wins);
    }

    // Updates aggregated sums for a given algorithm
    private static void updateAggregated(Map<String, double[]> aggregated, String key, Result result) {
        double[] sums = aggregated.get(key);
        sums[0] += result.avgCompletionTime;
        sums[1] += result.getProcessSwitches();
        sums[2] += result.getStarvedProcesses();
        aggregated.put(key, sums);
    }

    // Generates a list of processes based on test case parameters.
    // Arrival time is generated using an exponential generator to reflect avgArrival.
    // Burst time is in the range [1, maxBurst].
    private static List<Process> generateProcesses(int count, int avgArrival, int maxBurst) {
        Random rand = new Random();
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int pid = i + 1;
            int arrivalTime = exponentialGenerator(avgArrival);
            int burstTime = rand.nextInt(maxBurst) + 1;
            processes.add(new Process(arrivalTime, burstTime, pid));
        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        return processes;
    }

    // Exponential generator for arrival times
    private static int exponentialGenerator(int avgArrival) {
        Random rand = new Random();
        double lambda = 1.0 / avgArrival;
        return (int) Math.round(-Math.log(1 - rand.nextDouble()) / lambda);
    }

    // Prints a results table with columns Algorithm, Avg Completion Time, Switches, Starved Processes.
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

    // Prints a summary table showing, for each algorithm, in which test cases it was the winner.
    private static void printSummaryTable(Map<String, List<Integer>> wins) {
        System.out.println("\nSummary of Test Wins per Algorithm:");
        String line = "+------------+----------------------+-------------------------+";
        System.out.println(line);
        System.out.printf("| %-10s | %-20s | %-23s |\n",
                "Algorithm", "Wins (Test IDs)", "Total Wins");
        System.out.println(line);
        for (String algo : ALGORITHMS) {
            List<Integer> testIds = wins.get(algo);
            String ids = testIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
            // Truncate wins string if too long
            String displayIds = ids.isEmpty() ? "-" : ids;
            if (displayIds.length() > 20) {
                displayIds = displayIds.substring(0, 17) + "...";
            }
            System.out.printf("| %-10s | %-20s | %-23d |\n",
                    algo, displayIds, testIds.size());
        }
        System.out.println(line);
    }
}