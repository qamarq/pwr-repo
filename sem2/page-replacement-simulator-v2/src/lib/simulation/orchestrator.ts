import type {
  SimulationConfig,
  FullSimulationResults,
  ProcessInfo,
  PageReference,
  AlgorithmStats,
} from '@/lib/simulation/types';
import { generateSimulationData } from '@/lib/simulation/page-generator';
import { runAlgorithm } from '@/lib/simulation/engine';
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
async function executeAlgorithm(
  algorithmName: string,
  globalReferences: PageReference[],
  processes: ProcessInfo[],
  totalFrames: number,
  config: SimulationConfig
): Promise<AlgorithmStats> {
  const result = await runAlgorithm(
    algorithmName,
    globalReferences,
    processes,
    totalFrames,
    config
  );
  return {
    pageFaults: result.pageFaults,
    pageFaultsPerProcess: Object.fromEntries(
      result.processMetrics.map((m) => [m.processId, m.pageFaults])
    ),
    thrashingEvents: result.thrashing,
    processSuspensions: result.totalSuspensions,
    suspendedProcesses: result.processMetrics
      .filter((m) => m.suspensions > 0)
      .map((m) => m.processId),
  };
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
    await new Promise((resolve) =>
      setTimeout(resolve, 250 + Math.random() * 250)
    );
    const stats = await executeAlgorithm(
      alg.name,
      globalReferenceString,
      processInfos,
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
