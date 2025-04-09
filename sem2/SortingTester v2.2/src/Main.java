
import java.util.Comparator;

import core.AbstractSwappingSortingAlgorithm;
import testing.*;
import testing.comparators.*;
import testing.generation.*;
import testing.generation.conversion.*;

public class Main {

	public static void main(String[] args) {
		Comparator<MarkedValue<Integer>> markedComparator = new MarkedValueComparator<Integer>(new IntegerComparator());
		
		Generator<MarkedValue<Integer>> generator = new MarkingGenerator<Integer>(new RandomIntegerArrayGenerator(10));

		AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm = new BubbleSort<MarkedValue<Integer>>(markedComparator);
		
		testing.results.swapping.Result result = Tester.runNTimes(algorithm, generator, 1000, 50);
		
		printStatistic("time [ms]", result.averageTimeInMilliseconds(), result.timeStandardDeviation());
		printStatistic("comparisons", result.averageComparisons(), result.comparisonsStandardDeviation());
		printStatistic("swaps", result.averageSwaps(), result.swapsStandardDeviation());
		
		System.out.println("always sorted: " + result.sorted());
		System.out.println("always stable: " + result.stable());
	}
	
	private static void printStatistic(String label, double average, double stdDev) {
		System.out.println(label + ": " + double2String(average) + " +- " + double2String(stdDev));
	}
	
	private static String double2String(double value) {
		return String.format("%.12f", value);
	}
}
