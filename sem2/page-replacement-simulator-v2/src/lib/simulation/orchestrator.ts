import type {
  SimulationConfig,
  FullSimulationResults,
  ProcessInfo,
  PageReference,
  AlgorithmStats,
} from '@/lib/simulation/types';
import { generateSimulationData } from '@/lib/simulation/page-generator';
import { runAlgorithm } from '@/lib/simulation/engine'; // Placeholder for actual algorithm implementations

const ALGORITHMS = [
  {
    name: 'Equal Allocation',
    description: 'Frames are divided equally among all processes.',
  },
  {
    name: 'Proportional Allocation',
    description:
      'Frames are allocated proportionally to process size (number of pages).',
  },
  {
    name: 'PFF Control',
    description:
      'Frame allocation is dynamically adjusted based on Page Fault Frequency.',
  },
  {
    name: 'Working Set Model',
    description:
      'Processes are allocated frames based on their Working Set Size.',
  },
];

// This function will call the specific algorithm implementations.
// For now, it uses the placeholder `runAlgorithm` from engine.ts.
async function executeAlgorithm(
  algorithmName: string,
  globalReferences: PageReference[],
  processes: ProcessInfo[],
  totalFrames: number,
  config: SimulationConfig
): Promise<AlgorithmStats> {
  // In a real scenario, you'd have separate files/functions for each algorithm.
  // e.g., import { runEqualAllocation, runProportionalAllocation, ... } from './algorithms';
  // switch (algorithmName) { case "Equal Allocation": return runEqualAllocation(...); ... }
  return runAlgorithm(
    algorithmName,
    globalReferences,
    processes,
    totalFrames,
    config
  );
}

export async function runFullSimulation(
  config: SimulationConfig
): Promise<FullSimulationResults> {
  const { globalReferenceString, processInfos } =
    generateSimulationData(config);

  const results: FullSimulationResults = {
    globalReferenceString,
    processInfos,
    results: [],
  };

  for (const alg of ALGORITHMS) {
    // Simulate a delay to make the loading state more visible
    await new Promise((resolve) =>
      setTimeout(resolve, 250 + Math.random() * 250)
    );

    const stats = await executeAlgorithm(
      alg.name,
      globalReferenceString,
      processInfos, // Pass a deep copy if algorithms modify it: processInfos.map(p => ({...p, referenceString: [...p.referenceString]}))
      config.totalFrames,
      config
    );
    results.results.push({
      algorithmName: alg.name,
      description: alg.description,
      stats,
    });
  }

  return results;
}
