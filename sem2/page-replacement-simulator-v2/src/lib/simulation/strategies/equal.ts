import type {
  BaseSimParams,
  AllocationStrategyResult,
} from '@/lib/simulation/types';
import {
  ProcessExecutionState,
  computeThrashing,
} from '../processExecutionState';
export function simulateEqualAllocation(
  params: BaseSimParams
): AllocationStrategyResult {
  const {
    totalSystemFrames,
    globalReferences,
    processConfigs,
    localAlgorithm,
    thrashingWindowSize,
    thrashingThreshold,
  } = params;
  const n = processConfigs.length;
  if (!n) {
    return {
      strategyName: 'Equal',
      name: 'Equal',
      totalPageFaults: 0,
      totalThrashingEvents: 0,
      totalSuspensions: 0,
      processMetrics: [],
      pageFaults: 0,
      thrashing: 0,
    };
  }

  const states = processConfigs.map(
    (p) =>
      new ProcessExecutionState(
        p.id,
        { pageOffset: p.pageOffset },
        p.frameRange
          ? Math.min(1, p.frameRange.end - p.frameRange.start + 1)
          : 1,
        localAlgorithm,
        p.frameRange
      )
  );
  globalReferences.forEach((ref) => {
    const s = states.find((x) => x.id === ref.processId);
    if (s && !s.isSuspended) {
      s.accessPage(ref.page, ref.timestamp);
    } else if (s) {
      s.faultHistory.push(false);
    }
  });
  let totalPF = 0;
  let totalThr = 0;
  const pm = states.map((s) => {
    const thr = computeThrashing(
      s.faultHistory,
      thrashingWindowSize,
      thrashingThreshold
    );
    totalPF += s.pageFaults;
    totalThr += thr;
    return {
      processId: s.id,
      pageFaults: s.pageFaults,
      thrashingEvents: thr,
      suspensions: s.suspensionCount,
    };
  });
  return {
    strategyName: 'Equal',
    name: 'Equal',
    totalPageFaults: totalPF,
    totalThrashingEvents: totalThr,
    totalSuspensions: states.reduce((a, s) => a + s.suspensionCount, 0),
    processMetrics: pm,
    pageFaults: totalPF,
    thrashing: totalThr,
  };
}
