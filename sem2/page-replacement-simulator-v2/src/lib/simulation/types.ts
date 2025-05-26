import type { z } from 'zod';
import type { simulationConfigSchema } from '@/components/simulation-config-form';

export type SimulationConfig = z.infer<typeof simulationConfigSchema>;

export interface PageReference {
  processId: number;
  page: number;
  timestamp: number; // Global timestamp
}

export interface ProcessInfo {
  id: number;
  pageCount: number; // si - number of unique pages used by this process
  referenceString: PageReference[]; // The local reference string portion for this process
  initialFrames?: number; // Used for algorithms like PFF
}

export interface AlgorithmStats {
  pageFaults: number;
  pageFaultsPerProcess: Record<number, number>; // { processId: faultCount }
  thrashingEvents: number;
  processSuspensions: number;
  suspendedProcesses: number[]; // list of process IDs
  finalFrameAllocations?: Record<number, number>; // { processId: frameCount } for static, or a snapshot for dynamic
  // Additional detailed data for charts, e.g., frame allocation over time
  timelineData?: Array<{
    time: number;
    allocations: Record<number, number>;
    faultsToday: number;
  }>;
}

export interface SimulationResult {
  algorithmName: string;
  description: string;
  stats: AlgorithmStats;
}

export interface FullSimulationResults {
  globalReferenceString: PageReference[];
  processInfos: ProcessInfo[];
  results: SimulationResult[];
}

// Placeholder for actual algorithm function signature
export type FrameAllocationAlgorithm = (
  globalReferences: PageReference[],
  processes: ProcessInfo[],
  totalFrames: number,
  config: SimulationConfig
) => AlgorithmStats;
