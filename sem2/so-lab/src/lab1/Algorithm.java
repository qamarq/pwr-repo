package lab1;

import java.util.*;
import java.util.stream.Collectors;

abstract class Algorithm {
    protected int currentTime = 0;
    protected Process currentProcess = null;
    protected int processSwitches = 0;
    private Process previousProcess = null;
    protected List<Process> queue = new ArrayList<>();
    protected Set<Process> starvedProcesses = new HashSet<>();
    protected List<Process> processes;
    protected int tickDuration;

    public Algorithm(List<Process> processes, int tickDuration) {
        this.processes = new ArrayList<>();
        for (Process p : processes) {
            this.processes.add(p.clone());
        }

        this.tickDuration = tickDuration;
    }

    protected abstract Process selectCurrentProcess();

    public void tick() {
        queue = getQueuedProcesses(processes, currentTime);
        currentTime++;
        currentProcess = selectCurrentProcess();

        for (Process process : queue) {
            process.tick(tickDuration);
            if (process.getWaitTime() - process.getBurstTime() > 10000) {
                starvedProcesses.add(process);
            }
        }

        if (previousProcess != currentProcess) {
            if (currentProcess == null) {
                System.out.println("Running null after running process");
            } else {
                processSwitches++;
                previousProcess = currentProcess;
            }
        }

        if (currentProcess != null) {
            currentProcess.process(tickDuration);
            if (currentProcess.isComplete()) {
                currentProcess = null;
            }
        }
    }

    public Result run() {
//        clearProcesses(processes);
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

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
        return processes.stream().filter(p -> p.getArrivalTime() <= currentTime && !p.isComplete()).collect(Collectors.toList());
    }
}

class FCFS extends Algorithm {
    public FCFS(List<Process> processes) {
        super(processes, 1);
    }

    @Override
    protected Process selectCurrentProcess() {
        return queue.isEmpty() ? null : queue.getFirst();
    }
}

class SJF extends Algorithm {
    public SJF(List<Process> processes) {
        super(processes, 1);
    }

    @Override
    protected Process selectCurrentProcess() {
        return currentProcess == null ? queue.isEmpty() ? null : queue.stream().min(Comparator.comparingInt(Process::getBurstTime)).orElse(null) : currentProcess;
    }
}

class RR extends Algorithm {
    private int lastIndex = 0;

    public RR(List<Process> processes, int timeQuantum) {
        super(processes, timeQuantum);
    }

    @Override
    protected Process selectCurrentProcess() {
        if (queue.isEmpty()) return null;
        lastIndex = (lastIndex + 1) % queue.size();
        return queue.get(lastIndex);
    }
}

class SRTF extends Algorithm {
    public SRTF(List<Process> processes) {
        super(processes, 1);
    }

    @Override
    protected Process selectCurrentProcess() {
        if (queue.isEmpty())
            return null;

        Process selected = queue.stream()
                .min(java.util.Comparator.comparingInt(Process::getRemainingTime))
                .orElse(null);

        if (currentProcess != null && !currentProcess.isComplete()) {
            if (currentProcess.getRemainingTime() <= selected.getRemainingTime()) {
                return currentProcess;
            }
        }
        return selected;
    }
}