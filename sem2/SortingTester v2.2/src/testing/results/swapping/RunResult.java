package testing.results.swapping;

public class RunResult extends testing.results.RunResult {

    private long swps;

    public RunResult(long timeMillis, long comparisons, long swaps, boolean sorted, boolean stable) {
        super(timeMillis, comparisons, sorted, stable);

        this.swps = swaps;
    }

    public long swaps() {
        return swps;
    }
}
