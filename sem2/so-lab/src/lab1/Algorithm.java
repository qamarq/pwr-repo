package lab1;

import java.util.*;
import java.util.stream.Collectors;

abstract class Algorithm {
    protected int currentTime = 0;
    protected Process currentProcess = null;
    protected int processSwitches = 0;
    protected Integer previousProcessId = null;
    protected List<Process> queue = new ArrayList<>();
    protected Set<Process> starvedProcesses = new HashSet<>();
    protected List<Process> processes;
    protected int tickDuration;
    protected int starvationThreshold;

    public Algorithm(List<Process> processes, int tickDuration, int starvationThreshold) {
        this.processes = new ArrayList<>(processes);
        this.tickDuration = tickDuration;
        this.starvationThreshold = starvationThreshold;
    }

    protected abstract Process selectCurrentProcess();

    public void tick() {
        queue = getQueuedProcesses(processes, currentTime);
        currentTime++;
        currentProcess = selectCurrentProcess();

        for (Process process : queue) {
            process.tick(tickDuration);
            if (process.getWaitTime() - process.getBurstTime() > starvationThreshold) {
                starvedProcesses.add(process);
//                process.setComplete(true); idk czy to killowac czy nie
            }
        }

        if (previousProcessId == null || previousProcessId != System.identityHashCode(currentProcess)) {
            processSwitches++;
            previousProcessId = currentProcess != null ? System.identityHashCode(currentProcess) : null;
        }

        if (currentProcess != null) {
            currentProcess.process(tickDuration);
            if (currentProcess.isComplete()) {
                currentProcess = null;
            }
        }
    }

    public Result run() {
        clearProcesses(processes);
        int totalTime = processes.stream().mapToInt(Process::getBurstTime).sum() * 2;

        while (hasIncompleteProcesses(processes)) {
            tick();
        }

        return new Result(calcAvgCompletionTime(processes), processSwitches, starvedProcesses.size());
    }

    private boolean hasIncompleteProcesses(List<Process> processes) {
        return processes.stream().anyMatch(p -> !p.isComplete());
    }

    private double calcAvgCompletionTime(List<Process> processes) {
        return processes.stream().mapToInt(Process::getWaitTime).average().orElse(0);
    }

    private List<Process> getQueuedProcesses(List<Process> processes, int currentTime) {
        List<Process> queuedProcesses = new ArrayList<>();
        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && !process.isComplete()) {
                queuedProcesses.add(process);
            }
        }
        return queuedProcesses;
    }

    private void clearProcesses(List<Process> processes) {
        for (Process p : processes) {
            p.reset();
        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
    }
}

class FCFS extends Algorithm {
    public FCFS(List<Process> processes) {
        super(processes, 1, 1000);
    }

    @Override
    protected Process selectCurrentProcess() {
        return queue.isEmpty() ? null : queue.get(0);
    }
}

class SJF extends Algorithm {
    public SJF(List<Process> processes) {
        super(processes, 1, 1000);
    }

    @Override
    protected Process selectCurrentProcess() {
        return queue.isEmpty() ? null : queue.stream().min(Comparator.comparingInt(Process::getBurstTime)).orElse(null);
    }
}

class RR extends Algorithm {
    private int lastIndex = 0;

    public RR(List<Process> processes, int timeQuantum) {
        super(processes, timeQuantum, 1000);
    }

    @Override
    protected Process selectCurrentProcess() {
        if (queue.isEmpty()) return null;
        lastIndex = (lastIndex + 1) % queue.size();
        return queue.get(lastIndex);
    }
}