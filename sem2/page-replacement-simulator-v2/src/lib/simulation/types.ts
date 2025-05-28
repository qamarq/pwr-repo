export type LocalPageReplacementAlgorithm =
  | 'FIFO'
  | 'LRU'
  | 'SecondChance'
  | 'RAND';

export interface PageReference {
  processId: number;
  page: number;
  timestamp: number;
}

export interface ProcessInfo {
  id: number;
  pageCount: number;
  referenceString: PageReference[];
}

export interface SimulationConfig {
  localAlgorithm: LocalPageReplacementAlgorithm;
  thrashingWindowW: number;
  thrashingThresholdEFactor: number;
  pffDeltaT: number;
  pffLowerThresholdL: number;
  pffUpperThresholdU: number;
  pffSuspendThresholdH?: number;
  wsDeltaT: number;
  wsCalculationIntervalC: number;
  wsSuspensionStrategy:
    | 'smallest_wss'
    | 'largest_wss'
    | 'lowest_priority_mock'
    | 'random_mock';
  totalFrames: number;
  numProcesses: number;
  minPageRefLength: number;
  maxPageRefLength: number;
  maxPagesPerProcess: number;
  localityFactor: number;
  localityWindowSize: number;
}

export interface AlgorithmStats {
  pageFaults: number;
  pageFaultsPerProcess: Record<number, number>;
  thrashingEvents: number;
  processSuspensions: number;
  suspendedProcesses: number[];
  finalFrameAllocations?: Record<number, number>;
  timelineData?: Array<{
    time: number;
    allocations: Record<number, number>;
    faultsToday: number;
  }>;
}

export interface FrameRange {
  start: number;
  end: number;
}

export interface BaseSimParams {
  totalSystemFrames: number;
  globalReferences: PageReference[];
  processConfigs: Array<{
    id: number;
    numVirtualPages: number;
    pageOffset: number;
    frameRange?: FrameRange;
  }>;
  localAlgorithm: LocalPageReplacementAlgorithm;
  thrashingWindowSize: number;
  thrashingThreshold: number;
}

export interface PFFParams extends BaseSimParams {
  pffDeltaT: number;
  pffLowerBound: number;
  pffUpperBound: number;
  pffHighSuspendThreshold?: number;
}

export interface WorkingSetParams extends BaseSimParams {
  wsDeltaT: number;
  wsCalculationInterval: number;
}

export interface ProcessMetrics {
  processId: number;
  pageFaults: number;
  thrashingEvents: number;
  suspensions: number;
}

export interface AllocationStrategyResult {
  strategyName: string;
  name: string;
  totalPageFaults: number;
  totalThrashingEvents: number;
  totalSuspensions: number;
  processMetrics: ProcessMetrics[];
  pageFaults: number;
  thrashing: number;
}

export interface FullSimulationResults {
  globalReferenceString: PageReference[];
  processInfos: ProcessInfo[];
  results: Array<{
    algorithmName: string;
    description: string;
    stats: AlgorithmStats;
  }>;
}
