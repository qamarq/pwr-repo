package lab1;

import java.util.*;
import java.util.stream.Collectors;

abstract class Algorithm {
    protected int currentTime = 0;
    protected int processSwitches = 0;
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
        queue = getQueuedProcesses();
        currentTime++;
        Process currentProcess = selectCurrentProcess();

        for (Process process : queue) {
            process.tick(tickDuration);
            if (process.getWaitTime() - process.getBurstTime() > starvationThreshold) {
                starvedProcesses.add(process);
            }
        }

        if (currentProcess != null) {
            processSwitches++;
            currentProcess.process(tickDuration);
        }
    }

    public Result run() {
        clearProcesses();
        while (processes.stream().anyMatch(p -> !p.isComplete())) {
            tick();
        }
        return new Result(processes.stream().mapToInt(Process::getWaitTime).average().orElse(0), processSwitches, starvedProcesses.size());
    }

    private List<Process> getQueuedProcesses() {
        List<Process> queuedProcesses = new ArrayList<>();
        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && !process.isComplete()) {
                queuedProcesses.add(process);
            }
        }
        return queuedProcesses;
    }

    private void clearProcesses() {
        processes.forEach(Process::reset);
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
        return queue.isEmpty() ? null : Collections.min(queue, Comparator.comparingInt(Process::getBurstTime));
    }
}

class RR extends Algorithm {
    private int lastIndex = -1;

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