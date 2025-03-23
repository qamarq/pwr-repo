package lab1;

public class Result {
    final double avgCompletionTime;
    private final int processSwitches;
    private final int starvedProcesses;

    public Result(double avgCompletionTime, int processSwitches, int starvedProcesses) {
        this.avgCompletionTime = avgCompletionTime;
        this.processSwitches = processSwitches;
        this.starvedProcesses = starvedProcesses;
    }

    @Override
    public String toString() {
        return String.format("Średni czas zakończenia: %.2f, Zmiany procesu: %d, Zagłodzone: %d", avgCompletionTime, processSwitches, starvedProcesses);
    }
}
