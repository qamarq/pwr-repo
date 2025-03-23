package lab1;

class Process {
    private final int arrivalTime;
    private final int burstTime;
    private int remainingTime;
    private int waitTime = 0;
    private boolean complete = false;

    public Process(int arrivalTime, int burstTime) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public void reset() {
        this.remainingTime = burstTime;
        this.waitTime = 0;
        this.complete = false;
    }

    public void tick(int time) {
        if (!complete) waitTime += time;
    }

    public void process(int time) {
        remainingTime = Math.max(0, remainingTime - time);
        complete = remainingTime == 0;
    }

    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getWaitTime() { return waitTime; }
    public boolean isComplete() { return complete; }
}
